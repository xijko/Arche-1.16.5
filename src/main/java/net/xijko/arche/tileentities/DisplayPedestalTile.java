package net.xijko.arche.tileentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
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

public class DisplayPedestalTile extends TileEntity {
    public final ItemStackHandler itemHandler = createHandler();
    public final ItemStackHandler outputItemHandler = createOutputHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final Random random = new Random();

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

    public ItemStack getItem(){
        return this.itemHandler.getStackInSlot(0);
    }

    private ItemStackHandler createHandler() {
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            //if(side == Direction.DOWN){return outputHandler.cast();}
                return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public void renderPedestal(DisplayPedestalScreen screen, int x, int y){
        ItemStack stack = new ItemStack(ModBlocks.DISPLAY_PEDESTAL.get(),1);
        IBakedModel bakedmodel = screen.getItemRenderer().getItemModelWithOverrides(stack, screen.getContainer().tileEntity.getWorld(), null);

        float angle = 30;
        Quaternion rotation = new Quaternion(20,angle,0,true);
        //Quaternion rot = new Quaternion(20,angle,0,true);

        RenderSystem.pushMatrix();
        screen.getItemRenderer().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        screen.getItemRenderer().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)x, (float)y+100, 100.0F + screen.getItemRenderer().zLevel);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.rotate(rotation);
        matrixstack.scale(8,8,8);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        boolean flag = !bakedmodel.isSideLit();
        if (flag) {
            RenderHelper.setupGuiFlatDiffuseLighting();
        }

        screen.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupGui3DDiffuseLighting();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

    public void renderArtifact(DisplayPedestalScreen screen, ItemStack stack, int x, int y, IBakedModel bakedmodel) {
        float angle = (System.currentTimeMillis() / 50) % 360;
        Quaternion rotation = new Quaternion(20,angle,0,true);
        //Quaternion rot = new Quaternion(20,angle,0,true);

        RenderSystem.pushMatrix();
        screen.getItemRenderer().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        screen.getItemRenderer().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)x, (float)y, 100.0F + screen.getItemRenderer().zLevel);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.rotate(rotation);
        matrixstack.scale(8,8,8);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        boolean flag = !bakedmodel.isSideLit();
        if (flag) {
            RenderHelper.setupGuiFlatDiffuseLighting();
        }

        screen.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupGui3DDiffuseLighting();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
        renderPedestal(screen,x,y);

    }

}
