package net.xijko.arche.block.artifact;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.xijko.arche.Arche;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.item.ModItemGroup;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.item.ToolBeltModel;
import net.xijko.arche.item.model.VillageRingModel;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;

public class VillageRingItem extends ArcheArtifactItem implements ICurioItem {
    private static final ResourceLocation VILLAGE_RING_TEXTURE = new ResourceLocation(Arche.MOD_ID,
            "textures/entity/village_ring.png");
    private Object model;

    public VillageRingItem() {
        super(new Properties().maxStackSize(1).group(ModItemGroup.ARCHE_GROUP).rarity(Rarity.RARE), 0 // the item will appear on the Miscellaneous tab in creative
        );
    }


    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 1.0f, 1.0f);
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
        if (!(this.model instanceof VillageRingModel)) {
            this.model = new VillageRingModel();
        }
        VillageRingModel model = (VillageRingModel) this.model;
        model.setLivingAnimations(living, limbSwing, limbSwingAmount, partialTicks);
        model.setRotationAngles(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
                headPitch);
        ICurio.RenderHelper.followBodyRotations(living, model);
        IVertexBuilder vertexBuilder = ItemRenderer
                .getBuffer(renderTypeBuffer, model.getRenderType(VILLAGE_RING_TEXTURE), false,
                        stack.hasEffect());
        model
                .render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                        1.0F);
    }
    }
