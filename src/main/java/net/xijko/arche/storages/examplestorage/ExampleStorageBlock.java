package net.xijko.arche.storages.examplestorage;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.xijko.arche.inits.TileEntityInit;

import javax.annotation.Nullable;

public class ExampleStorageBlock extends Block {
    public ExampleStorageBlock() {
        super(AbstractBlock.Properties.create(Material.IRON));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityInit.STORAGE_TE.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof ExampleStorageTE && player instanceof ServerPlayerEntity){
            ExampleStorageTE te = (ExampleStorageTE) tile;
            IContainerProvider provider = ExampleStorageContainer.getServerContainerProvider(te,pos);
            INamedContainerProvider namedProvider = new SimpleNamedContainerProvider(provider,ExampleStorageContainer.TITLE);
            NetworkHooks.openGui((ServerPlayerEntity) player,namedProvider);
            player.addStat(Stats.INTERACT_WITH_FURNACE);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }
}
