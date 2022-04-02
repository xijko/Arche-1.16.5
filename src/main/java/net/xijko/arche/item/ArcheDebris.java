package net.xijko.arche.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.Arche;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

public class ArcheDebris extends Item {


    public static int archeTier = 1;

    public ArcheDebris(Properties properties, int archeTier) {
        super(properties);
        this.archeTier = archeTier;
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        

        //if (playerIn.getHeldItemOffhand().getTag() != null && playerIn.getHeldItemOffhand().getTag().contains("arche_sieves")){
                playerIn.getHeldItem(handIn).shrink(1);
                Minecraft.getInstance().player.sendChatMessage("Valid offhand detected: Debris tier: " + this.archeTier);
                worldIn.addEntity(new ItemEntity(worldIn,playerIn.getPosX(),playerIn.getPosY()+1,playerIn.getPosZ(),rollDebrisLoot(worldIn, playerIn)));

                //debug
                //String resourceName = "arche:items/debris" + String.valueOf(archeTier);
                //String resourceLocation = String.valueOf(new ResourceLocation(resourceName));
                //Minecraft.getInstance().player.sendChatMessage(resourceName + " > " + resourceLocation);

                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));

            //}else {

                //Minecraft.getInstance().player.sendChatMessage("Invalid offhand detected: Debris tier: " + this.archeTier);
                //return ActionResult.resultPass(playerIn.getHeldItem(handIn));
            //}


    }
    public ItemStack rollDebrisLoot(World worldIn, PlayerEntity playerIn){
        String resourceName = "arche:items/debris" + String.valueOf(archeTier);
        LootTable table = ServerLifecycleHooks.getCurrentServer().getLootTableManager().getLootTableFromLocation(new ResourceLocation(resourceName)); // resolves to /assets/mymod/loot_tables/my_table.json
        LootContext ctx = new LootContext.Builder(ServerLifecycleHooks.getCurrentServer().func_241755_D_())
                //[<parameter minecraft:direct_killer_entity>,
                // <parameter minecraft:this_entity>,
                // <parameter minecraft:explosion_radius>,
                // <parameter minecraft:tool>,
                // <parameter minecraft:origin>,
                // <parameter minecraft:damage_source>,
                // <parameter minecraft:killer_entity>,
                // <parameter minecraft:last_damage_player>,
                // <parameter minecraft:block_state>,
                // <parameter minecraft:block_entity>]
                /*.withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY,null)
                .withNullableParameter(LootParameters.THIS_ENTITY,null)
                .withNullableParameter(LootParameters.EXPLOSION_RADIUS,null)
                .withNullableParameter(LootParameters.TOOL,null)
                .withNullableParameter(LootParameters.ORIGIN,null)
                .withNullableParameter(LootParameters.DAMAGE_SOURCE,null)
                .withNullableParameter(LootParameters.KILLER_ENTITY,null)
                .withNullableParameter(LootParameters.LAST_DAMAGE_PLAYER,null)
                .withNullableParameter(LootParameters.BLOCK_STATE,null)
                .withNullableParameter(LootParameters.BLOCK_ENTITY,null)*/
                .withLuck(playerIn.getLuck())// adjust luck, commonly EntityPlayer.getLuck()
                .build(LootParameterSets.EMPTY);
        return table.generate(ctx).get(0);



    }
}
