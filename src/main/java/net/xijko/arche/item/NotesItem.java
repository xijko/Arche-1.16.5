package net.xijko.arche.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NotesItem extends Item {
    public NotesItem(Properties properties) {
        super(properties);
        properties.containerItem(this);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack item = itemStack.copy();
        return item;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
}
