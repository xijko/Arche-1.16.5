package net.xijko.arche.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;

import java.util.Set;

public class ArcheSieves extends ToolItem {
    private int archTier;

    public ArcheSieves(Properties properties, int archTier) {
        super(1,1, ItemTier.WOOD,ImmutableSet.of(),new Item.Properties().group(ModItemGroup.ARCHE_GROUP));
        this.archTier = archTier;
    }

    public int getArcheTier() {
        return this.archTier;
    }
}
