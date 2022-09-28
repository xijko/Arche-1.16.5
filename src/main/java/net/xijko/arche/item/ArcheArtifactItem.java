package net.xijko.arche.item;

import net.minecraft.item.Item;
import net.xijko.arche.block.MuseumCatalogBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcheArtifactItem extends Item {
    public int slot;
    private static final Logger LOGGER = LogManager.getLogger();

    public ArcheArtifactItem(Properties p_i48487_1_, int slot) {
        super(p_i48487_1_);
        this.slot = slot;
    }


}
