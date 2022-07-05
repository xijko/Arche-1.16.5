package net.xijko.arche.network;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.xijko.arche.tileentities.CorpseFlowerTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class CorpseFlowerDespawnMessage {
    private static final Logger LOGGER = LogManager.getLogger();
    private static LivingEntity entity;
    private static CorpseFlowerTile flowerTile;
    private static ServerPlayerEntity player;

    public CorpseFlowerDespawnMessage(LivingEntity e, CorpseFlowerTile c, ServerPlayerEntity p) {
        super();
        entity = e;
        flowerTile = c;
        player = p;
    }

    public static void encode(CorpseFlowerDespawnMessage message, PacketBuffer buffer) {
    }

    public static CorpseFlowerDespawnMessage decode(PacketBuffer buffer) {
        return new CorpseFlowerDespawnMessage(entity, flowerTile, player);
    }

    public static void handle(CorpseFlowerDespawnMessage message, Supplier<NetworkEvent.Context> ctx) {
        if(!ctx.get().getPacketHandled()) {
            LOGGER.warn("received packet from "+ctx.get());
            ctx.get().enqueueWork(() -> {
                //ServerPlayerEntity player = ctx.get().getSender();
                //if (player == null) return;
                removalParticle(entity,flowerTile,player);

                ctx.get().setPacketHandled(true);
            });
        }
    }

    public static void removalParticle(LivingEntity e, CorpseFlowerTile c, ServerPlayerEntity p){

        ServerWorld world = (ServerWorld) p.getEntityWorld();
        world.spawnParticle(
                p,
                ParticleTypes.ITEM_SLIME,
                true,
                c.getPos().getX()+0.5,
                c.getPos().getY(),
                c.getPos().getZ()+0.5,
                1,
                0.5,
                0.5,
                0.5,
                0.2

        );

        world.playSound(
                null,
                c.getPos(),
                SoundEvents.BLOCK_SOUL_SOIL_STEP,
                SoundCategory.NEUTRAL,
                0.1F,
                5F
        );

        world.spawnParticle(
                p,
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                true,
                e.getPosX(),
                e.getPosY()+1,
                e.getPosZ(),
                1,
                0.5,
                0.5,
                0.5,
                0.2

        );
    }

}
