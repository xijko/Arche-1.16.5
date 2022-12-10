package net.xijko.arche.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class WhipProjectileEntity extends AbstractArrowEntity{
    public static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Byte> PIERCE_LEVEL = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Byte> FLAGS = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.BYTE);
    public static final DataParameter<Integer> AIR = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.VARINT);
    public static final DataParameter<Optional<ITextComponent>> CUSTOM_NAME = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.OPTIONAL_TEXT_COMPONENT);
    public static final DataParameter<Boolean> CUSTOM_NAME_VISIBLE = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> SILENT = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Boolean> NO_GRAVITY = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Pose> POSE = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.POSE);
    public static final DataParameter<Integer> REACH = EntityDataManager.createKey(WhipProjectileEntity.class, DataSerializers.VARINT);
    @Nullable
    private BlockState inBlockState;
    protected boolean inGround;
    protected int timeInGround;
    public AbstractArrowEntity.PickupStatus pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
    public int arrowShake;
    private int ticksInGround;
    private double damage = 2.0D;
    private int knockbackStrength;
    private SoundEvent hitSound = this.getHitEntitySound();
    private IntOpenHashSet piercedEntities;
    private List<Entity> hitEntities;


    public WhipProjectileEntity(EntityType<? extends AbstractArrowEntity> whipProjectileEntityEntityType, World world) {
        super(whipProjectileEntityEntityType,world);
        this.dataManager.register(FLAGS, (byte)0);
        this.dataManager.register(AIR, this.getMaxAir());
        this.dataManager.register(CUSTOM_NAME_VISIBLE, false);
        this.dataManager.register(CUSTOM_NAME, Optional.empty());
        this.dataManager.register(SILENT, false);
        this.dataManager.register(NO_GRAVITY, true);
        this.dataManager.register(POSE, Pose.STANDING);
        this.dataManager.register(CRITICAL, (byte)0);
        this.dataManager.register(PIERCE_LEVEL, (byte)0);
        this.dataManager.register(REACH, 0);
    }


    public WhipProjectileEntity newWhipProjectileEntity(EntityType<WhipProjectileEntity> type, LivingEntity shooter, World worldIn){
        WhipProjectileEntity projectile = new WhipProjectileEntity(type, worldIn);
        projectile.setPosition(shooter.getPosX(), shooter.getPosYEye() - (double)0.1F, shooter.getPosZ());
        projectile.setShooter(shooter);
        return projectile;
    }


    public BlockRayTraceResult getHitVector(){
        Vector3d vector3d = this.getMotion();
        Vector3d vector3d2 = this.getPositionVec();
        BlockRayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d2, vector3d2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        return raytraceresult;
    }

    @Nullable
    @Override
    public ItemEntity entityDropItem(ItemStack stack) {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        LOGGER.warn(this.getPosition());
        if(this.ticksExisted > 1 || this.inGround){
            impactActivation();
            this.remove();
        }

    }

    @Override
    protected void func_230299_a_(BlockRayTraceResult result) {
            this.inBlockState = this.world.getBlockState(result.getPos());
            Vector3d vector3d = result.getHitVec().subtract(this.getPosX(), this.getPosY(), this.getPosZ());
            this.setMotion(vector3d);
            Vector3d vector3d1 = vector3d.normalize().scale((double)0.05F);
            this.setRawPosition(this.getPosX() - vector3d1.x, this.getPosY() - vector3d1.y, this.getPosZ() - vector3d1.z);
            this.setHitSound(SoundEvents.ENTITY_FISHING_BOBBER_THROW);
            if (this.hitEntities != null) {
                this.hitEntities.clear();
            }

            if (this.piercedEntities != null) {
                this.piercedEntities.clear();
            }
    }

    @Override
    public boolean getIsCritical() {
        return false;
    }

    @Override
    public byte getPierceLevel() {
        return 0;
    }

    protected void impactActivation() {
        if(!this.world.isRemote()){
            PlayerEntity player = (PlayerEntity) this.getShooter();
            Direction impactFace = this.getHitVector().getFace();
            BlockPos position = this.getPosition();
            //activateBlock(position, player);
            switch(impactFace){
                case NORTH:
                    position.add(0,0,-1);
                    activateBlock(position, player);
                    break;
                case EAST:
                    position.add(1,0,0);
                    activateBlock(position, player);
                    break;
                case SOUTH:
                    position.add(0,0,1);
                    activateBlock(position, player);
                    break;
                case WEST:
                    position.add(-1,0,0);
                    activateBlock(position, player);
                    break;
                case UP:
                    position.add(0,1,0);
                    activateBlock(position, player);
                    break;
                case DOWN:
                    position.add(0,-1,0);
                    activateBlock(position, player);
                    break;
            }
        }
    }

    private void activateBlock(BlockPos position, PlayerEntity player){
        BlockState state = this.world.getBlockState(position);
        state.onBlockActivated(this.world,player,player.swingingHand,this.getHitVector());
        LOGGER.warn("Activated " + state + " at: " + position);
    }

    @Override
    public void setIsCritical(boolean critical) {}

    public void setReach(int reach){
        if (reach >5){
            reach = 5;
        }
        this.dataManager.set(REACH,reach);
    }

    private void setArrowFlag(int p_203049_1_, boolean p_203049_2_) {
        byte b0 = this.dataManager.get(CRITICAL);
        if (p_203049_2_) {
            this.dataManager.set(CRITICAL, (byte)(b0 | p_203049_1_));
        } else {
            this.dataManager.set(CRITICAL, (byte)(b0 & ~p_203049_1_));
        }

    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        float f = (float)this.getMotion().length();
        int i = MathHelper.ceil(MathHelper.clamp((double)f * this.damage, 0.0D, 2.147483647E9D));
        if (this.getPierceLevel() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }

            if (this.hitEntities == null) {
                this.hitEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }

            this.piercedEntities.add(entity.getEntityId());
        }

        if (this.getIsCritical()) {
            long j = (long)this.rand.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }

        Entity entity1 = this.getShooter();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = DamageSource.causeArrowDamage(this, this);
        } else {
            damagesource = DamageSource.causeArrowDamage(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastAttackedEntity(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getFireTimer();
        if (this.isBurning() && !flag) {
            entity.setFire(5);
        }

        if (entity.attackEntityFrom(damagesource, (float)i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;

                if (this.knockbackStrength > 0) {
                    Vector3d vector3d = this.getMotion().mul(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockbackStrength * 0.6D);
                    if (vector3d.lengthSquared() > 0.0D) {
                        livingentity.addVelocity(vector3d.x, 0.1D, vector3d.z);
                    }
                }

                if (!this.world.isRemote && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingentity, entity1);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity)entity1, livingentity);
                }

                this.arrowHit(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)entity1).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.HIT_PLAYER_ARROW, 0.0F));
                }

                if (!entity.isAlive() && this.hitEntities != null) {
                    this.hitEntities.add(livingentity);
                }
            }
        } else {
            entity.forceFireTicks(k);
            this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
        }
        LOGGER.warn("Hit: "+entity);

        this.remove();

    }

    @Override
    protected ItemStack getArrowStack() {
        return null;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        Entity entity = this.getShooter();
        return new SSpawnObjectPacket(entity);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }
}
