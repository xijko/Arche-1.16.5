package net.xijko.arche.tileentities;

import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.xijko.arche.block.ArcheDeposit;

public class DirtDepositTile extends DepositTile{
    public static final BooleanProperty GENERATED = DirtDepositTile.GENERATED;

    public DirtDepositTile() {
        super(ModTileEntities.DIRT_DEPOSIT_TILE.get());
    }
}