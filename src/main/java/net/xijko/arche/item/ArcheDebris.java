package net.xijko.arche.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.xijko.arche.util.ArcheTags;

import java.lang.reflect.Array;
import java.util.List;

public class ArcheDebris extends Item {


    public static int archeTier = 1;

    public ArcheDebris(Properties properties, int archeTier) {
        super(properties);
        this.archeTier = archeTier;
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        if (
                handIn == Hand.MAIN_HAND &&
                playerIn.getHeldItemOffhand().getTag() != null &&
                playerIn.getHeldItemOffhand().getItem().isIn(ArcheTags.Items.ARCHE_SIEVES)){

                playerIn.getHeldItem(handIn).shrink(1);

                if(playerIn.getHeldItemOffhand().isDamageable()){
                    playerIn.getHeldItemOffhand().damageItem(1,playerIn,playerEntity -> {
                        playerEntity.sendBreakAnimation(Hand.OFF_HAND);
                    });
                }
            assert Minecraft.getInstance().player != null;
            List<ItemStack> lootOutcome = rollDebrisLoot(worldIn, playerIn);
            //Minecraft.getInstance().player.sendChatMessage("Loot: " + lootOutcome);
            for(ItemStack itemstack : lootOutcome)
                {
                ItemEntity entityitem = new ItemEntity(worldIn,playerIn.getPosX(),playerIn.getPosY()+1,playerIn.getPosZ(),itemstack);
                worldIn.addEntity(entityitem);
                }
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));

            }else {

                Minecraft.getInstance().player.sendChatMessage("Invalid offhand detected- tag:"+playerIn.getHeldItemOffhand().getTag()+", Debris tier: " + this.archeTier);
                return ActionResult.resultPass(playerIn.getHeldItem(handIn));
            }



    }

    public List<ItemStack> rollDebrisLoot(World worldIn, PlayerEntity playerIn){
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
        return table.generate(ctx);


    }

}
