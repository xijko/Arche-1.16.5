package net.xijko.arche.block.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CorpseFlower extends CropsBlock {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_7;

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
        return null;
    }

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        worldIn.setBlockState(pos, this.withAge(i), 2);

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
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return super.getShape(state, worldIn, pos, context);
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

    /*@SubscribeEvent
    public static void spawnEntity(LivingSpawnEvent event) {
        LivingEntity entity = event.getEntityLiving();
        double deltaX = entity.getPosX() - 50;
        double deltaY = entity.getPosY() - 4;
        double deltaZ = entity.getPosZ() - 50;

        double distance = (Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ)));

        if (distance < 100) {
            entity.remove();
        } else {
        }
    }*/

}
