package net.xijko.arche.events;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xijko.arche.Arche;
import net.xijko.arche.block.crops.CorpseFlower;
import net.xijko.arche.network.CorpseFlowerDespawnMessage;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.RestoreTableRestoreMessage;
import net.xijko.arche.tileentities.CorpseFlowerTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Arche.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnEvents {


    private static final Logger LOGGER = LogManager.getLogger();
    private static final int dist = (int) Math.pow(128, 2);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void spawnEntity(LivingSpawnEvent.CheckSpawn event) {
        LivingEntity e = (LivingEntity) event.getEntity();
        if(e instanceof ZombieEntity || e instanceof SkeletonHorseEntity || e instanceof AbstractSkeletonEntity ){
            //LOGGER.warn("spawn detected - "+e);
            for(TileEntity te : e.world.tickableTileEntities){
                Vector3d zomPos = e.getPositionVec();
                if(te instanceof CorpseFlowerTile && te.getPos().distanceSq(zomPos, true) <= dist && te.getWorld() != null){
                    World world = te.getWorld();
                    BlockState state = world.getBlockState(te.getPos());
                    CorpseFlower block = (CorpseFlower) state.getBlock();
                    if(block.isMaxAge(state)){
                        sendParticleMessageToNearPlayers(e);
                        //LOGGER.warn("particle - "+te.getPos() + " "+te.getWorld());
                        event.setResult(Event.Result.DENY);
                        //event.setCanceled(true);
                        e.remove();
                        //LOGGER.warn("spawn ELIMINATED - "+e);
                        return;
                    }
                }
            }
        }
        /*
        LivingEntity entity = event.getEntityLiving();
        BlockPos thisFlower = this.thisBlockPos;
        double deltaX = entity.getPosX() - thisFlower.getX();
        double deltaY = entity.getPosY() - thisFlower.getY();
        double deltaZ = entity.getPosZ() - thisFlower.getZ();

        double distance = (Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ)));

        if (distance < 10 &&
                (
                        entity instanceof ZombieEntity || entity instanceof SkeletonHorseEntity || entity instanceof AbstractSkeletonEntity
                )
        ) {
            blockedEntityParticle(entity, this.thisBlockPos);
            entity.remove();
            //entity.preventEntitySpawning = true;
        }*/
    }

    private static void sendParticleMessageToNearPlayers(LivingEntity zomb){
        List<ServerPlayerEntity> players = (List<ServerPlayerEntity>) zomb.getEntityWorld().getPlayers();
        players.forEach(p ->{
            Vector3d pPos = p.getPositionVec();
            for(TileEntity te : p.world.tickableTileEntities){
                if(te instanceof CorpseFlowerTile && te.getPos().distanceSq(pPos, true)<=dist){
                    ModNetwork.sendToClient(p,new CorpseFlowerDespawnMessage(zomb, (CorpseFlowerTile) te,p));
                }
            }
        });

    }
        /*
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void preventTeleport(EnderTeleportEvent e){
        if(e.getEntity() instanceof EndermanEntity){
            int RANGE_SQUARED = (int) Math.pow(ESConfig.brazierRange.get(), 2);
            for(TileEntity te : e.getEntity().getCommandSenderWorld().tickableBlockEntities){
                Vector3d entPos = e.getEntity().position();
                if(te instanceof BrazierTileEntity && te.getBlockPos().distSqr(entPos, true) <= RANGE_SQUARED && te.getLevel() != null){
                    BlockState state = te.getBlockState();
                    if(state.getBlock() == ESBlocks.brazier && state.getValue(ESProperties.BRAZIER_CONTENTS) == 6){
                        e.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }
     */
}
