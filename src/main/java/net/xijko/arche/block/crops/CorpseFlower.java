package net.xijko.arche.block.crops;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xijko.arche.Arche;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.tileentities.ModTileEntities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Random;

public class CorpseFlower extends CropsBlock {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_7;

    private BlockPos thisBlockPos;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    public CorpseFlower(Properties builder) {
        super(builder);
    }

    protected IItemProvider getSeedsItem(){
        return ModItems.CORPSE_FLOWER_SEED.get();
    }

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
        int j = this.getMaxAge();
        if (i >= j) {
            i = j;
        }
        worldIn.setBlockState(pos, this.withAge(i), 2);

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        this.thisBlockPos = pos;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.CORPSE_FLOWER_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.add(0, 0, -1)).matchesBlock(Blocks.SOUL_SOIL) &&
                state.matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(0, 0, 1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(1, 0, -1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(1, 0, 0)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(1, 0, 1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(-1, 0, -1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(-1, 0, 0)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(-1, 0, 1)).matchesBlock(Blocks.SOUL_SOIL);
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if(entityIn instanceof ItemEntity){
            ItemEntity itemEntity = (ItemEntity) entityIn;
            ItemStack thrownItemStack = itemEntity.getItem();
            Item thrownItem = thrownItemStack.getItem();
         if(thrownItem == Items.ZOMBIE_HEAD || thrownItem == Items.SKELETON_SKULL || thrownItem == Items.WITHER_SKELETON_SKULL ){
             this.grow(worldIn, pos, state);
             thrownItemStack.shrink(1);
         }
            super.onEntityCollision(state, worldIn, pos, entityIn);
        }else{
            super.onEntityCollision(state, worldIn, pos, entityIn);
        }
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        float chance = 0.08F;
        if(chance < rand.nextFloat()){
         worldIn.addParticle(
                 ParticleTypes.MYCELIUM,
                 pos.getX() + rand.nextDouble(),
                 pos.getY() + rand.nextDouble(),
                 pos.getZ() + rand.nextDouble(),
                 0,
                 0,
                 1
         );
        }
    }

    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        LOGGER.warn("Max age is " + this.getMaxAge());
        LOGGER.warn("Current age is " + this.getAge(state));
        return !this.isMaxAge(state) &&
                worldIn.getBlockState(pos.add(0, -1, -1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(0, -1, 0)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(0, -1, 1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(1, -1, -1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(1, -1, 0)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(1, -1, 1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(-1, -1, -1)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(-1, -1, 0)).matchesBlock(Blocks.SOUL_SOIL) &&
                worldIn.getBlockState(pos.add(-1, -1, 1)).matchesBlock(Blocks.SOUL_SOIL)
                ;
    }

    public void blockedEntityParticle(LivingEntity entity,BlockPos block){
        entity.world.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    entity.getPosX(),
                    entity.getPosY(),
                    entity.getPosZ(),
                    0,
                    0,
                    1
        );

        entity.world.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                block.getX(),
                    block.getY()+1,
                    block.getZ(),
                    0,
                    1,
                    0
        );

    }
}
