package net.xijko.arche.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.extensions.IForgeItem;

public class MattockHeadTemplate extends Item implements IForgeItem {


    public MattockHeadTemplate(Properties properties) {
        super(properties);
        properties.containerItem(this);
        properties.group(ModItemGroup.ARCHE_GROUP);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;

    }
}
