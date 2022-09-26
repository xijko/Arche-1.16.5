package net.xijko.arche.item;

import net.minecraft.item.Item;

public class ArcheArtifactItem extends Item {
    public int slot;

    public ArcheArtifactItem(Properties p_i48487_1_, int slot) {
        super(p_i48487_1_);
        this.slot = slot;
        ModItems.setArtifactSlot(slot, this);
    }
}
