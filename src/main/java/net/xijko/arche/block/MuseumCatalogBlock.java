package net.xijko.arche.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
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
import net.xijko.arche.Arche;
import net.xijko.arche.container.MuseumCatalogContainer;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.item.ArcheDebug;
import net.xijko.arche.tileentities.ModTileEntities;
import net.xijko.arche.tileentities.MuseumCatalogTile;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;

public class MuseumCatalogBlock extends Block {

    public static final BooleanProperty MUSEUM_OWNED = BooleanProperty.create("museum_owned");

    public MuseumCatalogBlock(Properties p_i48440_1_) {
        super(p_i48440_1_.notSolid());
        setDefaultState(getDefaultState().with(MUSEUM_OWNED,true));
        //initializeArtifactList();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
                                             PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote()) {
            MuseumCatalogTile tileEntity = (MuseumCatalogTile) worldIn.getTileEntity(pos);
            INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

            if(tileEntity != null) {
                if(!player.isCrouching()) {
                    if(player.getHeldItemMainhand().getItem() instanceof ArcheDebug){
                        MuseumCatalogTile te = (MuseumCatalogTile) worldIn.getTileEntity(pos);
                        assert te != null;
                        te.onLoad();
                        state = state.with(MUSEUM_OWNED,false);
                        return ActionResultType.CONSUME;

                    }else if(player.getHeldItemOffhand().getItem() instanceof ArcheDebug){
                        return ActionResultType.CONSUME;
                    }else {
                        NetworkHooks.openGui(((ServerPlayerEntity) player), containerProvider, tileEntity.getPos());
                    }
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
                return new TranslationTextComponent("screen.arche.museum_catalog");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new MuseumCatalogContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.MUSEUM_CATALOG_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(12, 2, 7.5, 15, 4, 8.5),
            Block.makeCuboidShape(1, 0, 5, 4, 1, 9),
            Block.makeCuboidShape(1, 0, 7, 4, 1, 11),
            Block.makeCuboidShape(12, 0, 7, 15, 1, 11),
            Block.makeCuboidShape(12, 0, 5, 15, 1, 9),
            Block.makeCuboidShape(15, 4, 7, 16, 16, 9),
            Block.makeCuboidShape(1, 5, 7.5, 15, 15, 8.5),
            Block.makeCuboidShape(0, 4, 7, 1, 16, 9),
            Block.makeCuboidShape(1, 15, 7, 15, 16, 9),
            Block.makeCuboidShape(1, 4, 6, 15, 5, 10),
            Block.makeCuboidShape(1, 2, 7.5, 4, 4, 8.5),
            Block.makeCuboidShape(6, 4, 7.4, 9, 8, 7.4),
            Block.makeCuboidShape(10.38060233744357, 10.913417161825448, 7.4, 13.38060233744357, 14.913417161825448, 7.4),
            Block.makeCuboidShape(1.3806023374435696, 9.7, 7.4, 4.38060233744357, 14.7, 7.4),
            Block.makeCuboidShape(12, 6.300000000000001, 7.4, 14, 10.3, 7.4),
            Block.makeCuboidShape(4, 5.913417161825448, 7.4, 6, 8.913417161825448, 7.4),
            Block.makeCuboidShape(5.38060233744357, 10.2, 7.4, 8.38060233744357, 13.2, 7.4),
            Block.makeCuboidShape(10, 5.913417161825448, 8.6, 12, 8.913417161825448, 8.6),
            Block.makeCuboidShape(2, 6.300000000000001, 8.6, 4, 10.3, 8.6),
            Block.makeCuboidShape(2.6193976625564304, 10.913417161825448, 8.6, 5.61939766255643, 14.913417161825448, 8.6),
            Block.makeCuboidShape(5.930151265314969, 5.378492794482931, 8.6, 8.930151265314969, 9.37849279448293, 8.6),
            Block.makeCuboidShape(7.61939766255643, 10.2, 8.6, 10.61939766255643, 13.2, 8.6),
            Block.makeCuboidShape(11.61939766255643, 9.7, 8.6, 14.61939766255643, 14.7, 8.6)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_S = SHAPE_N;

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(7.5, 2, 1, 8.5, 4, 4),
            Block.makeCuboidShape(2.207572779645723, 1.7489225134140143, 12, 6.207572779645723, 2.7489225134140143, 15),
            Block.makeCuboidShape(9.79242722035428, 1.7489225134140143, 12, 13.79242722035428, 2.7489225134140143, 15),
            Block.makeCuboidShape(9.79242722035428, 1.7489225134140143, 1, 13.79242722035428, 2.7489225134140143, 4),
            Block.makeCuboidShape(2.207572779645723, 1.7489225134140143, 1, 6.207572779645723, 2.7489225134140143, 4),
            Block.makeCuboidShape(7, 4, 0, 9, 16, 1),
            Block.makeCuboidShape(7.5, 5, 1, 8.5, 15, 15),
            Block.makeCuboidShape(7, 4, 15, 9, 16, 16),
            Block.makeCuboidShape(7, 15, 1, 9, 16, 15),
            Block.makeCuboidShape(6, 4, 1, 10, 5, 15),
            Block.makeCuboidShape(7.5, 2, 12, 8.5, 4, 15),
            Block.makeCuboidShape(7.4, 5.218160335914552, 5.124105564484488, 7.4, 9.218160335914552, 8.124105564484488),
            Block.makeCuboidShape(7.4, 10.913417161825448, 2.6193976625564304, 7.4, 14.913417161825448, 5.61939766255643),
            Block.makeCuboidShape(7.4, 9.7, 11.61939766255643, 7.4, 14.7, 14.61939766255643),
            Block.makeCuboidShape(7.4, 6.300000000000001, 2, 7.4, 10.3, 4),
            Block.makeCuboidShape(7.4, 5.913417161825448, 10, 7.4, 8.913417161825448, 12),
            Block.makeCuboidShape(7.4, 10.2, 7.61939766255643, 7.4, 13.2, 10.61939766255643),
            Block.makeCuboidShape(8.6, 5.913417161825448, 4, 8.6, 8.913417161825448, 6),
            Block.makeCuboidShape(8.6, 6.300000000000001, 12, 8.6, 10.3, 14),
            Block.makeCuboidShape(8.6, 10.913417161825448, 10.38060233744357, 8.6, 14.913417161825448, 13.38060233744357),
            Block.makeCuboidShape(8.6, 5.218160335914549, 7.875894435515512, 8.6, 9.218160335914549, 10.875894435515512),
            Block.makeCuboidShape(8.6, 10.2, 5.38060233744357, 8.6, 13.2, 8.38060233744357),
            Block.makeCuboidShape(8.6, 9.7, 1.3806023374435696, 8.6, 14.7, 4.38060233744357)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_W = SHAPE_E;


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(HORIZONTAL_FACING)) {
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
        }
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
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    public static ArcheArtifactItem[] getArtifactItemList(){
        return Arche.ARTIFACT_ITEM_LISTS;
    }

    public static void initializeArtifact(ArcheArtifactItem item, int slot){
        /*String artifacts = "";

        Item.
        for (Item i : itemGroup.get
             ) {
            if(i.get().getClass().isInstance(ArcheArtifactItem.class)){
                int slot = ((ArcheArtifactItem) i.get()).slot;
                artifactItemsList[slot] = (ArcheArtifactItem) i.get();
                artifacts += i.get().toString();
            }
        }
         */
        //artifactItemsList[slot] = item;
    }


}
