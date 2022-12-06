package net.xijko.arche.block;

import com.google.gson.JsonParseException;
import com.mojang.brigadier.Message;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.entity.CandleEntity;
import net.xijko.arche.entity.ModEntityTypes;
import net.xijko.arche.item.ArcheDebug;
import net.xijko.arche.tileentities.CandleLanternTile;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import net.xijko.arche.tileentities.ModTileEntities;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;

public class CandleLanternBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

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
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        try{
            CandleLanternTile tile = (CandleLanternTile) worldIn.getTileEntity(pos);
            LOGGER.warn(placer);
            if(placer!=null && placer instanceof PlayerEntity){

                PlayerEntity player = (PlayerEntity) placer.getEntity();
                UUID uuid = player.getUniqueID();
                tile.setUUID(uuid.toString());
                LOGGER.warn("set uuid");
            }
            if(stack.hasDisplayName()){
                tile.setDisplayName(stack.getDisplayName().getString());
            }
        }catch(Error e){
            LOGGER.warn("There's no candle lantern entity, here! Weird!");
        }
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
        if(!player.isSneaking()) {
            if (!worldIn.isRemote) {  // server only!
                worldIn.setBlockState(pos, state.cycleValue(LIT));
                    if (state.get(LIT)) {
                        summonCandle(state, worldIn, pos, player);
                    }
            } if (worldIn.isRemote()){
                worldIn.setBlockState(pos, state.cycleValue(LIT));
                playHingeSound(state,worldIn,pos,player);
            }
        }else{
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof CandleLanternTile && !worldIn.isRemote){
                CandleLanternTile tile = (CandleLanternTile) te;
                breakLantern(tile,state,worldIn,pos,player);
            }
        }

        return ActionResultType.SUCCESS;
    }

    private void playHingeSound(BlockState state, World worldIn, BlockPos pos,
                                PlayerEntity player){
        if (state.get(LIT)){
            worldIn.playSound(player,pos,
                    SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS,1F,2);
        }else{
            worldIn.playSound(player,pos,
                    SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS,1F,2);
        }

    }

    public void breakLantern(CandleLanternTile tile, BlockState state, World worldIn, BlockPos pos,
                             PlayerEntity player){
        ItemStack drop = new ItemStack(state.getBlock());
        if(!tile.displayName.equals("")){
            player.inventory.addItemStackToInventory(drop.setDisplayName( TextComponentUtils.toTextComponent(() -> tile.displayName)
            ));}
        else {
            player.inventory.addItemStackToInventory(drop);
        }
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
    }


    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean flag = worldIn.isBlockPowered(pos);
            if (state.get(POWERED) != flag) {
                worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)).with(LIT, Boolean.valueOf(!flag)), 2);
                if (!state.get(POWERED)&&state.get(LIT)) summonCandle(state,worldIn,pos,null);
            }
        }
        playHingeSound(state,worldIn,pos,null);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(POWERED,false);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT,HORIZONTAL_FACING,POWERED);
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

        CandleLanternTile tile = (CandleLanternTile) world.getTileEntity(pos);
        UUID uuid;
        assert tile != null;
        if(!tile.uuid.equals("")){
            uuid = UUID.fromString(tile.uuid);
        }else{
            uuid = player.getUniqueID();

        }
        candle.setOwnerId(uuid);
        CompoundNBT compoundnbt = tile.serializeNBT();
        LOGGER.warn(compoundnbt);
        if (compoundnbt != null && compoundnbt.contains("name", 8) && !compoundnbt.getString("name").equals("")) {
                ITextComponent itextcomponent = ITextComponent.getTextComponentOrEmpty(compoundnbt.getString("name"));
            LOGGER.warn(itextcomponent);
            candle.setCustomName(itextcomponent);
        }
        candle.initLanternPos(pos);
        ((ServerWorld) world).summonEntity(candle);
    }

}
