package net.xijko.arche.item;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.xijko.arche.entity.ModEntityTypes;
import net.xijko.arche.entity.WhipProjectileEntity;
import net.xijko.arche.item.client.MontanaWhipItemRenderer;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.Predicate;

import static net.xijko.arche.Arche.LOGGER;

public class MontanaWhipItem extends BowItem implements IAnimatable {
    private final AnimationFactory manager = new AnimationFactory(this);
    private boolean isDrawing = false;
    private boolean isPriming = false;
    private boolean isAttacking = false;
    private WhipProjectileEntity arrow;
    public static final Predicate<ItemStack> NOTHIN = (stack) -> true;

    public MontanaWhipItem() {
        super(new Item.Properties().group(ModItemGroup.ARCHE_GROUP).setISTER(() -> MontanaWhipItemRenderer::new));
    }

    /*
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        boolean flag = entity instanceof ClientPlayerEntity;
        LOGGER.warn(entity.swingProgressInt);
        if(entity.swingProgressInt<=0 && animationStart){
            this.animationStart=true;
            return super.onEntitySwing(stack, entity);
        }else{
            this.animationStart=false;
            return false;
        }

    }*/

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }


    /*
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (this.isDrawing) {
            this.isDrawing=false;
            this.isAttacking=true;
            return ActionResult.resultConsume(itemstack);
        } else if (!this.isDrawing) {
                this.isDrawing=false;
                this.isAttacking=false;
            return ActionResult.resultConsume(itemstack);
        }
        return ActionResult.resultConsume(itemstack);
    }
    */

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if(this.isPriming){
            this.isAttacking=true;
            this.isDrawing=false;
        }
        if (entityLiving instanceof PlayerEntity) {
            //PlayerEntity playerentity = (PlayerEntity)entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i < 0) return;
                float f = getArrowVelocity(i);
                if (!((double)f < 0.1D)) {
                    boolean flag1 = true;
                    createProjectile(worldIn, entityLiving,f, (int) Math.floor((double) i /7));
                    worldIn.playSound(null,entityLiving.getPosition(),SoundEvents.ENTITY_EGG_THROW,SoundCategory.PLAYERS,3.0F,8.0F);
                    /*
                    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                    if (j > 0) {
                        abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double)j * 0.5D + 0.5D);
                    }
                    */
                }

        }
    }

    private static AbstractArrowEntity createArrow(World worldIn, LivingEntity shooter) {
        AbstractArrowEntity abstractarrowentity = new WhipProjectileEntity(ModEntityTypes.WHIP_PROJECTILE.get(),worldIn);
        if (shooter instanceof PlayerEntity) {
            abstractarrowentity.setIsCritical(true);
        }

        abstractarrowentity.setHitSound(SoundEvents.ITEM_CROSSBOW_HIT);
        abstractarrowentity.setPosition(shooter.getPosX(), shooter.getPosYEye() - (double)0.1F, shooter.getPosZ());
        abstractarrowentity.setShooter(shooter);
        //abstractarrowentity.setShotFromCrossbow(true);

        return abstractarrowentity;
    }


    public void createProjectile(World world, LivingEntity shooter, Float velocity, int reach){

        if (!world.isRemote) {
            WhipProjectileEntity projectileentity;
                projectileentity = (WhipProjectileEntity) createArrow(world, shooter);
            projectileentity.setPosition(shooter.getPosX(), shooter.getPosYEye() - (double)0.15F, shooter.getPosZ());
                LOGGER.warn(shooter instanceof ICrossbowUser);
            projectileentity.setShooter(shooter);
            if (shooter instanceof ICrossbowUser) {
                ICrossbowUser icrossbowuser = (ICrossbowUser)shooter;
                icrossbowuser.fireProjectile(icrossbowuser.getAttackTarget(), shooter.getHeldItem(Hand.MAIN_HAND), projectileentity, 0.0F);
                LOGGER.warn("Shot by iCrossbow");
            } else {
                Vector3d vector3d1 = shooter.getUpVector(1.0F);
                LOGGER.warn("vector is: "+vector3d1);
                Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), 0.0F, true);
                Vector3d vector3d = shooter.getLook(1.0F);
                Vector3f vector3f = new Vector3f(vector3d);
                vector3f.transform(quaternion);
                projectileentity.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(), reach, 0);
                projectileentity.setReach(reach);
            }

            world.addEntity(projectileentity);
            LOGGER.warn("position is: "+projectileentity.getPosition());
            world.playSound((PlayerEntity)null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1);
        }
    /*
        Items.CROSSBOW
        Vector3d vec = shooter.getLookVec();
        //WhipProjectileEntity projectile = new WhipProjectileEntity(ModEntityTypes.WHIP_PROJECTILE.get(),world);
        //projectile = projectile.newWhipProjectileEntity(ModEntityTypes.WHIP_PROJECTILE.get(),shooter,world);
        double shootX = shooter.getPosX() + vec.x*1.5;
        double shootY = shooter.getPosY() + vec.y*1.5;
        double shootZ = shooter.getPosZ() + vec.z*1.5;
        AbstractArrowEntity projectile = new ModEntityTypes.WHIP_PROJECTILE.get()newWhipProjectileEntity(ModEntityTypes.WHIP_PROJECTILE.get(),shooter,world);
        //projectile.setPosition(shootX,shootY,shootZ);
        //projectile.setDirectionAndMovement(shooter, shooter.rotationPitch, shooter.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);
        if(!world.isRemote()){
            world.addEntity(projectile);
        }
    */
    }





    @Override
    public int getUseDuration(ItemStack stack) {
        return 150;
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        super.onUse(worldIn, livingEntityIn, stack, count);
        float f = (float)(stack.getUseDuration() - count) / (float)getUseDuration(stack);
        if (f > 0.025F) {
            this.isDrawing = true;
            this.isPriming = true;
        }
        if(f>=1F){
            this.isDrawing = false;
            livingEntityIn.stopActiveHand();
        }
    }


    @Override
    public Predicate<ItemStack> getAmmoPredicate() {
        return NOTHIN;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){

        if(!this.isAttacking && this.isDrawing) {
            event.getController().setAnimation(new AnimationBuilder().playOnce("animation.montana_whip_item.draw"));
            return PlayState.CONTINUE;
        }
        else if(this.isAttacking){
            event.getController().setAnimationSpeed(2);
            event.getController().setAnimation(new AnimationBuilder().playOnce("animation.montana_whip_item.attack"));
            //event.getController().setAnimation(new AnimationBuilder().loop("animation.montana_whip_item.idle"));
            this.isDrawing=false;
            this.isAttacking=false;
            return PlayState.CONTINUE;
        }
        if(event.getController().getCurrentAnimation() == null){
            event.getController().markNeedsReload();
            event.getController().setAnimationSpeed(1);
            event.getController().setAnimation(new AnimationBuilder().loop("animation.montana_whip_item.idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"controller",0,this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.manager;
    }
}
