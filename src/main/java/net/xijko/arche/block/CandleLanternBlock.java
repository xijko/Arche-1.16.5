package net.xijko.arche.block;

import com.google.gson.JsonParseException;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.entity.CandleEntity;
import net.xijko.arche.entity.ModEntityTypes;
import net.xijko.arche.item.ArcheDebug;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import net.xijko.arche.tileentities.ModTileEntities;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;

public class CandleLanternBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public CandleLanternBlock() {
        super(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).hardnessAndResistance(1f).setLightLevel(getLightValueLit(12)));
        this.setDefaultState(this.getDefaultState().with(LIT, Boolean.valueOf(true)));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.CANDLE_LANTERN_TILE.get().create();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    private static final VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(12, 0, 12, 4, 14, 4)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(stateIn.get(LIT)){
            worldIn.addParticle(
                    ParticleTypes.FLAME,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    0.0,
                    0.01,
                    0.0
            );
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
                                             PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        LOGGER.warn("Cycled -> "+state.get(LIT));
        worldIn.setBlockState(pos, state.cycleValue(LIT));
        if(state.get(LIT)){
            summonCandle(state,worldIn,pos,player);
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT,HORIZONTAL_FACING);
    }

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.get(BlockStateProperties.LIT) ? lightValue : 0;
        };
    }

    private void summonCandle(BlockState state, World world, BlockPos pos,PlayerEntity player){
        if(world.isRemote())return;
        ServerWorld serverWorld = (ServerWorld) world;
        int rot;
        double dX = 0;
        double dZ = 0;
        Direction dir = state.get(HORIZONTAL_FACING);
        LOGGER.warn(dir);
        switch (dir) {
            case NORTH:
                dZ = 0.5;
                rot = 0;
            case SOUTH:
                dZ = -0.5;
                rot = 180;
            case EAST:
                dX = 0.5;
                rot = 90;
            case WEST:
                dX = -0.5;
                rot = 270;
            default:
                dZ = 0.5;
                rot = 0;
        }
        CandleEntity candle = ModEntityTypes.CANDLE.get().create(
            serverWorld);
        candle.setPositionAndRotation(pos.getX()+0.5,pos.getY()+0.01,pos.getZ()+0.5,rot,0);
        //candle.setPosition(pos.getX()+0.5,pos.getY(),pos.getZ()+0.5);
        //candle.setVelocity(dX,0,dZ);
        //candle.setPositionAndRotation(dX,0,dZ);
        candle.setOwnerId(player.getUniqueID());
        CompoundNBT compoundnbt = serverWorld.getTileEntity(pos).serializeNBT().getCompound("Name");
        LOGGER.warn(compoundnbt);
        if (compoundnbt != null && compoundnbt.contains("Name", 8)) {
                ITextComponent itextcomponent = ITextComponent.Serializer.getComponentFromJson(compoundnbt.getString("Name"));
            LOGGER.warn(itextcomponent);
            candle.setCustomName(itextcomponent);
        }
        candle.initLanternPos(pos);
        ((ServerWorld) world).summonEntity(candle);
    }

}
