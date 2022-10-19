package net.xijko.arche.block.crops;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.xijko.arche.item.ModItems;

import java.util.function.Supplier;

public class SoilBag extends BlockItem {
private Block soil;

    public SoilBag(Block blockIn, int capacity) {

        super(blockIn,new Properties().maxDamage(capacity).rarity(Rarity.UNCOMMON));
        this.soil = blockIn;
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context) {
        BlockItemUseContext blockitemusecontext = this.getBlockItemUseContext(context);
        ItemStack itemstack = blockitemusecontext.getItem();
        int damage = itemstack.getDamage();
        ActionResultType result = super.tryPlace(blockitemusecontext);
        if(!result.isSuccessOrConsume() || context.getPlayer().isCreative()){
            return result;
        }
        if(damage<this.getMaxDamage(itemstack)-1){
            itemstack.setDamage(damage+1);
            blockitemusecontext.getItem().grow(1);
        }else {
            World world = context.getWorld();
            PlayerEntity player = context.getPlayer();
            context.getWorld().addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ() + 0.5, new ItemStack(ModItems.UNLABELED_SOILBAG.get(), 1)));
        }
        return result;
    }
}
