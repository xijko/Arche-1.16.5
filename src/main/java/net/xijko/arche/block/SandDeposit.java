package net.xijko.arche.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.xijko.arche.tileentities.ModTileEntities;

import javax.annotation.Nullable;
import java.util.List;

public class SandDeposit extends ArcheDeposit{
    public SandDeposit(Properties properties, int archeTier, @Nullable List<Structure> structures, @Nullable List<Block> blocks, @Nullable boolean generated) {
        super(properties, archeTier, structures, blocks, generated);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.SAND_DEPOSIT_TILE.get().create();
    }



}
