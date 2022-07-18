package net.xijko.arche.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.xijko.arche.Arche;
import net.xijko.arche.storages.toolbelt.ToolBeltCapabilityProvider;
import net.xijko.arche.storages.toolbelt.ToolBeltContainer;
import net.xijko.arche.storages.toolbelt.ToolBeltItemStackHandler;
import org.lwjgl.opengl.GL11;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScubaItem extends Item implements ICurioItem {
    private static final ResourceLocation SCUBA_TEXTURE = new ResourceLocation(Arche.MOD_ID,
            "textures/equipment/scuba.png");
    private Object model;

        public ScubaItem() {
            super(new Properties().maxStackSize(1).group(ModItemGroup.ARCHE_GROUP) // the item will appear on the Miscellaneous tab in creative
            );
        }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        //return new ICurio.SoundInfo(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 0.6f);
        return new ICurio.SoundInfo(SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 0.6f);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slot, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
        return true;
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack,
                       IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity living,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch, ItemStack stack) {


        if (!(this.model instanceof ScubaModel)) {
            this.model = new ScubaModel<>();
        }
        ScubaModel<?> scubaModel = (ScubaModel<?>) this.model;
        ICurio.RenderHelper.followHeadRotations(living, scubaModel.bipedHead);
        IVertexBuilder vertexBuilder = ItemRenderer
                .getBuffer(renderTypeBuffer, scubaModel.getRenderType(SCUBA_TEXTURE), false,
                        stack.hasEffect());

        IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(SCUBA_TEXTURE));
        GL11.glEnable(GL11.GL_BLEND);
        scubaModel.bipedHead
                .render(matrixStack, ivertexbuilder, light, LivingRenderer.getPackedOverlay(living, 0.0F), 1.0F, 1.0F, 1.0F,
                        1F);
        GL11.glDisable(GL11.GL_BLEND);

        ICurio.RenderHelper.translateIfSneaking(matrixStack, living);
        ICurio.RenderHelper.rotateIfSneaking(matrixStack, living);
        scubaModel.bipedBody
                .render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                        1.0F);



    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 20 == 0 && livingEntity.isInWater()) {
            livingEntity
                    .addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 20, -1, true, false));
        }
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return false;
    }

//look at https://forums.minecraftforge.net/topic/88077-1161-custom-armor-model-textures-not-working-properly/
}

