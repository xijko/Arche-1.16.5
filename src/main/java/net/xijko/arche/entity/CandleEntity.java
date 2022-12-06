package net.xijko.arche.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.xijko.arche.block.CandleLanternBlock;
import net.xijko.arche.block.LightBlock;
import net.xijko.arche.block.ModBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import static net.xijko.arche.block.CandleLanternBlock.LIT;

public class CandleEntity extends TameableEntity implements IAnimatable {

    private PlayerEntity player;
    private int lastLightX, lastLightY, lastLightZ;
    private static final Logger LOGGER = LogManager.getLogger();
    public int lanternX;
    public int lanternY;
    public int lanternZ;

    public AnimationFactory factory = new AnimationFactory(this);

    public CandleEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap setAttributes(){
        return AnimalEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH,1)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED,0.4)
                .create();
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        deserializeLanternPos(compound);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("lanternx",this.lanternX);
        compound.putInt("lanterny",this.lanternY);
        compound.putInt("lanternz",this.lanternZ);
    }

    public void initLanternPos(BlockPos pos){
        this.lanternX = pos.getX();
        this.lanternY = pos.getY();
        this.lanternZ = pos.getZ();
    }

    public void deserializeLanternPos(CompoundNBT compound){
        this.lanternX = compound.getInt("lanternx");
        this.lanternY = compound.getInt("lanterny");
        this.lanternZ = compound.getInt("lanternz");
    }

    @Override
    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        setOwnerId(playerIn.getUniqueID());
        return ActionResultType.CONSUME;
    }

    public void checkLantern(){
        BlockPos lanternPos = new BlockPos(this.lanternX,this.lanternY,this.lanternZ);
        if(lanternPos.equals(new BlockPos(0, 0, 0))) return;
        if(world.getBlockState(lanternPos).getBlock() instanceof CandleLanternBlock){
            if(world.getBlockState(lanternPos).get(LIT)){
                this.removeStoredLightBlock();
                this.setHealth(0);
            }
        }else{
            this.removeStoredLightBlock();
            this.setHealth(0);
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        spawnLightBlock();
        checkLantern();
        if(!this.world.isRemote()) return;
        spawnFlame();
    }

    @Override
    public boolean canChangeDimension() {
        return false;
    }

    private void spawnFlame(){
        if(ticksExisted%4>0) return;
        if(Math.random()*100>0) {
            Vector3d pos = this.getPositionVec();
            world.addParticle(
                    ParticleTypes.FLAME,
                    pos.getX() + Math.random() * 0,
                    pos.getY() + 0.5 + Math.random() * 0.0625,
                    pos.getZ() + Math.random() * 0,
                    0.0,
                    0.01,
                    0.0
            );
        }
    }

    private void spawnLightBlock(){
        if(world.isRemote()) return;
            Vector3d pos = this.getPositionVec();

            int lx = this.lastLightX;
            int ly = this.lastLightY;
            int lz = this.lastLightZ;

                int nlx = MathHelper.floor(pos.getX());
                int nly = MathHelper.floor(pos.getY());
                int nlz = MathHelper.floor(pos.getZ());
                boolean setLightBlock = false;

                BlockPos nextLight = new BlockPos(nlx,nly,nlz);

                if(lx*ly*lz == 0){
                    this.lastLightX = MathHelper.floor(pos.getX());
                    this.lastLightY = MathHelper.floor(pos.getY());
                    this.lastLightZ = MathHelper.floor(pos.getZ());
                }

                if(!this.isAlive()){
                    removeStoredLightBlock();
                    return;
                }

                if(nlx != lx || nly != ly || nlz != lz) {
                    int d = (nlx - lx)*(nlx - lx)+(nly - ly)*(nly - ly)+(nlz - lz)*(nlz - lz);
                    if(world.getLightValue(nextLight) < 7) {
                        if(d >= 1) {
                            if(ly >= 0 && ly < 256) {
                                removeStoredLightBlock();
                            }
                            setLightBlock = true;
                        }
                        else {
                            if(!(world.getBlockState(nextLight).getBlock() instanceof LightBlock)) {
                                    setLightBlock = true;
                            }
                        }
                    }

                    if(setLightBlock && nly >= 0 && nly < 256) {
                        if(world.isAirBlock(nextLight)) world.setBlockState(nextLight, ModBlocks.LIGHT_BLOCK.get().getDefaultState());
                        if(world.isAirBlock(nextLight.add(0,1,0))) world.setBlockState(nextLight.add(0,1,0), ModBlocks.LIGHT_BLOCK.get().getDefaultState());
                        this.lastLightX = nlx;
                        this.lastLightY = nly;
                        this.lastLightZ = nlz;
                    }
                }
        }


    @Override
    public boolean isInWater() {
        if (super.isInWater() && this.isAlive()){
            this.setHealth(0);
            this.onDeath(DamageSource.DROWN);
        }
        return super.isInWater();
    }

    private void removeStoredLightBlock(){
        BlockPos lastPos = new BlockPos(this.lastLightX,this.lastLightY,this.lastLightZ);
        removeLightBlock(lastPos);
    }

    private void removeLightBlock(BlockPos posBottomLight){
        BlockPos posTopLight = posBottomLight.add(0,1,0);
        if(world.getBlockState(posBottomLight).getBlock() instanceof LightBlock) {
            world.setBlockState(posBottomLight, Blocks.AIR.getDefaultState());
        }
        if(world.getBlockState(posTopLight).getBlock() instanceof LightBlock) {
            world.setBlockState(posTopLight, Blocks.AIR.getDefaultState());
        }
    }

    private void closeLantern(){
        BlockPos lanternPos = new BlockPos(this.lanternX,this.lanternY,this.lanternZ);
        BlockState state = world.getBlockState(lanternPos);
        if(state.getBlock() instanceof CandleLanternBlock){
            world.setBlockState(lanternPos,state.with(LIT,Boolean.TRUE));
        }
    }

    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity mate) {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource source){
        return SoundEvents.BLOCK_HONEY_BLOCK_HIT;
    }

    protected SoundEvent getDeathSound(){
        return SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT;
    }

    protected void registerGoals(){
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this,1,0,2,true));
        this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
    }



    //these are geckolib specific

    private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event){
        if (event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.candle.walk", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.candle.idle", true));
        return PlayState.CONTINUE;

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"controller",0,this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void dropExperience() {
        return;
    }

    @Override
    public void onDeath(DamageSource cause) {
        removeStoredLightBlock();
        closeLantern();
        super.onDeath(cause);
    }
}
