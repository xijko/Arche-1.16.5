package net.xijko.tutorialmod.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class PoopDeposit extends OreBlock {

    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(8, 0, 4, 9, 1, 5), Block.makeCuboidShape(10, 0, 9, 13, 3, 12), Block.makeCuboidShape(5, 0, 6, 7, 2, 8), Block.makeCuboidShape(9, 0, 6, 11, 2, 8), Block.makeCuboidShape(6, 0, 11, 9, 2, 14), Block.makeCuboidShape(4, 0, 5, 7, 1, 7), Block.makeCuboidShape(7, 2, 12, 8, 3, 13)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(11, 0, 8, 12, 1, 9), Block.makeCuboidShape(4, 0, 10, 7, 3, 13), Block.makeCuboidShape(8, 0, 5, 10, 2, 7), Block.makeCuboidShape(8, 0, 9, 10, 2, 11), Block.makeCuboidShape(2, 0, 6, 5, 2, 9), Block.makeCuboidShape(9, 0, 4, 11, 1, 7), Block.makeCuboidShape(3, 2, 7, 4, 3, 8)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.makeCuboidShape(7, 0, 11, 8, 1, 12), Block.makeCuboidShape(3, 0, 4, 6, 3, 7), Block.makeCuboidShape(9, 0, 8, 11, 2, 10), Block.makeCuboidShape(5, 0, 8, 7, 2, 10), Block.makeCuboidShape(7, 0, 2, 10, 2, 5), Block.makeCuboidShape(9, 0, 9, 12, 1, 11), Block.makeCuboidShape(8, 2, 3, 9, 3, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.makeCuboidShape(4, 0, 7, 5, 1, 8), Block.makeCuboidShape(9, 0, 3, 12, 3, 6), Block.makeCuboidShape(6, 0, 9, 8, 2, 11), Block.makeCuboidShape(6, 0, 5, 8, 2, 7), Block.makeCuboidShape(11, 0, 7, 14, 2, 10), Block.makeCuboidShape(5, 0, 9, 7, 1, 12), Block.makeCuboidShape(12, 2, 8, 13, 3, 9)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING,context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)){
            case NORTH:
                return SHAPE_N;
            case EAST:
                return SHAPE_E;
            case SOUTH:
                return SHAPE_S;
            case WEST:
                return SHAPE_W;
            default:
                return SHAPE_N;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING,rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.6f; //0=dark, 1=noshadow
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public PoopDeposit() {
        super(AbstractBlock.Properties.create(Material.CLAY)
                .hardnessAndResistance(1f,1f)
                .sound(SoundType.SLIME)
                .harvestLevel(0)
                .harvestTool(ToolType.SHOVEL)
        );
    }
}
