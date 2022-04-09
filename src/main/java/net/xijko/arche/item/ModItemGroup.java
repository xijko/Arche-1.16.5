package net.xijko.arche.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup ARCHE_GROUP = new ItemGroup("archeTab") {
        @Override
        public ItemStack createIcon() { //this determines the icon that appears in the creative inventory
            return new ItemStack(ModItems.DIRT_SIEVE.get());
        }
    };

}
