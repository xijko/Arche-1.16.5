package net.xijko.arche.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;

import java.util.Set;

public class ArcheSieves extends ToolItem {
    public static int archTier = 1;
    public ArcheSieves(int archTier, Properties builderIn) {
        super(0.25f, 1.4f, ItemTier.WOOD, ImmutableSet.of(), builderIn);
        this.archTier = archTier;
    }
}
