package net.xijko.arche.tileentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.screen.DisplayPedestalScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class DisplayPedestalTile extends TileEntity {
    public final ItemStackHandler itemHandler = createHandler();
    public final ItemStackHandler outputItemHandler = createOutputHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final Random random = new Random();
    public ItemStack cachedItem;

    public DisplayPedestalTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DisplayPedestalTile() {
        this(ModTileEntities.DISPLAY_PEDESTAL_TILE.get());
    }

    /*@Override
    protected void itemHandler.onContentsChanged(int slot) {

        if (slot==4){
            super(this.)
        }

    }*/

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    /*
      Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
      many blocks change at once.
     */
     //This compound comes back to you clientside in {@link handleUpdateTag}
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.write(compound);
    }

    /*
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public ItemStack getCachedEntity() {
        if (this.cachedItem == null) {
            this.cachedItem = EntityType.loadEntityAndExecute(this.spawnData.getNbt(), this.getWorld(), Function.identity());
            if (this.itemHandler.getStackInSlot(0).getItem() != Blocks.AIR.asItem() && this.itemHandler.serializeNBT().contains("inv", 8) && this.cachedEntity instanceof MobEntity) {
            }
        }

        return this.cachedItem;
    }
*/
    public ItemStack getItem(){
        //TileEntity.readTileEntity(this.getBlockState(),);
        this.validate();
        LazyOptional<IItemHandler> handler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).cast();
        if(!handler.isPresent()){
            createHandler();
        }
        return this.itemHandler.getStackInSlot(0);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return super.getStackInSlot(slot);
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
             return true;

            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                    return 1;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void onLoad() {
        super.onLoad();

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            //if(side == Direction.DOWN){return outputHandler.cast();}
                return handler.cast();
        }
        return super.getCapability(cap, side);
    }

}
