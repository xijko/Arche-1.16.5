package net.xijko.arche.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.container.DisplayPedestalContainer;
import net.xijko.arche.container.RestoreTableContainer;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import net.xijko.arche.tileentities.ModTileEntities;
import net.xijko.arche.tileentities.RestoreTableTile;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;

public class DisplayPedestalBlock extends BreakableBlock {

    public static final BooleanProperty MUSEUM_OWNED = BooleanProperty.create("museum_owned");
    public static final BooleanProperty MUSEUM_COMPLETED = BooleanProperty.create("museum_completed");
    public static final IntegerProperty MUSEUM_SLOT = IntegerProperty.create("museum_slot",0,32);

    public DisplayPedestalBlock(Properties p_i48440_1_) {
        super(p_i48440_1_.notSolid());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
                                             PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote()) {
            DisplayPedestalTile tileEntity = (DisplayPedestalTile) worldIn.getTileEntity(pos);
            INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

            if(tileEntity != null) {
                if(!player.isCrouching()) {

                    NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getPos());
                }else {
                    ItemStackHandler handler = tileEntity.itemHandler;
                    ItemStack playerHeldStack = player.getHeldItem(handIn);
                    ItemStack pedestalStack = handler.getStackInSlot(0);
                    ItemStack stackOut;
                    ItemStack stackIn;

                    if(playerHeldStack == pedestalStack){
                        LOGGER.warn("stacks are the same - no action!");
                        return ActionResultType.PASS;
                    }else{
                        if(playerHeldStack.isEmpty() || pedestalStack.isEmpty()){
                            stackOut = handler.extractItem(0,1,false);
                            stackIn = ItemHandlerHelper.copyStackWithSize(playerHeldStack,1);
                            if(!playerHeldStack.isEmpty()){
                                playerHeldStack.shrink(1);
                            }
                            if(playerHeldStack.isEmpty()){
                                player.setHeldItem(handIn,stackOut);
                            }else{
                                ItemHandlerHelper.giveItemToPlayer(player,stackOut);
                            }

                            handler.insertItem(0, stackIn,false);
                        }
                    }
                    tileEntity.markDirty();

                    /*if(stack.isEmpty()){
                        if(playerHeldStack.isEmpty()){

                        }
                        handler.insertItem(0,playerHeldStack.copy(),false);
                        playerHeldStack.shrink(1);
                        return ActionResultType.SUCCESS;
                    }else{
                        ItemStack stackOut = handler.extractItem(0,1,false);
                        if(playerHeldStack.isEmpty()){
                            player.setHeldItem(handIn,stackOut);
                            return ActionResultType.SUCCESS;
                        }else{
                            player.addItemStackToInventory(stackOut);
                            return ActionResultType.SUCCESS;
                        }
                    }*/
                }

            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.arche.display_pedestal");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new DisplayPedestalContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.DISPLAY_PEDESTAL_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(0, 11, 0, 16, 13, 16),
            Block.makeCuboidShape(1, 0, 1, 15, 4, 15),
            Block.makeCuboidShape(2, 4, 2, 14, 11, 14),
            Block.makeCuboidShape(2, 13, 2, 14, 14, 14)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        /*switch (state.get(HORIZONTAL_FACING)) {
            case NORTH:
                return SHAPE_N;
            case SOUTH:
                return SHAPE_S;
            case WEST:
                return SHAPE_W;
            case EAST:
                return SHAPE_E;
            default:
                return SHAPE_N;
        }*/
        return SHAPE_N;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return !state.get(MUSEUM_OWNED);
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return !state.get(MUSEUM_OWNED);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
        builder.add(MUSEUM_OWNED);
        builder.add(MUSEUM_COMPLETED);
        builder.add(MUSEUM_SLOT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
}
