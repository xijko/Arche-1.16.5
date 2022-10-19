package net.xijko.arche.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class CandleEntity extends TameableEntity implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    protected CandleEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }



    public static AttributeModifierMap setAttributes(){
        return AnimalEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH,2)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 12)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED,0.2)
                .create();
    }

    @Override
    public ActionResultType getEntityInteractionResult(PlayerEntity playerIn, Hand hand) {
        setOwnerId(playerIn.getUniqueID());
        return ActionResultType.CONSUME;
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
        return SoundEvents.BLOCK_HONEY_BLOCK_BREAK;
    }

    protected void registerGoals(){
        this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this,1));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this,1,1,8,true));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
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

}
