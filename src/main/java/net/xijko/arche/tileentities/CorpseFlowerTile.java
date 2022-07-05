package net.xijko.arche.tileentities;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class CorpseFlowerTile  extends TileEntity implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    public CorpseFlowerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public CorpseFlowerTile() {
        this(ModTileEntities.CORPSE_FLOWER_TILE.get());
    }

    @Override
    public void tick() {
    }
}
