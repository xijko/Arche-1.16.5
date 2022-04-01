package net.xijko.arche.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.Arche;

public class ArcheDebris extends Item {


    public static int archeTier = 0;

    public ArcheDebris(Properties properties, int archeTier) {
        super(properties);
        this.archeTier = archeTier;
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        if (playerIn.getHeldItemOffhand().getTag() != null && playerIn.getHeldItemOffhand().getTag().contains("arche_sieves")){
                playerIn.getHeldItem(handIn).shrink(1);
                Minecraft.getInstance().player.sendChatMessage("Valid offhand detected: Debris tier: " + this.archeTier);

                //drop itself, for now.
                worldIn.addEntity(new ItemEntity(worldIn,playerIn.getPosX(),playerIn.getPosY(),playerIn.getPosY(),new ItemStack(this.getItem(),1)));

                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));

            }else {

                Minecraft.getInstance().player.sendChatMessage("Invalid offhand detected: Debris tier: " + this.archeTier);
                return ActionResult.resultPass(playerIn.getHeldItem(handIn));
            }


    }
}
