package net.xijko.tutorialmod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup TUTORIAL_GROUP = new ItemGroup("tutorialModTab") {
        @Override
        public ItemStack createIcon() { //this determines the icon that appears in the creative inventory
            return new ItemStack(ModItems.AMETHYST.get());
        }
    };

}
