package net.xijko.arche.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.xijko.arche.Arche;
import org.lwjgl.opengl.GL11;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;

public class MontanaHatItem extends ArcheArtifactItem implements ICurioItem {
    private static final ResourceLocation HAT_TEXTURE = new ResourceLocation(Arche.MOD_ID,
            "textures/equipment/montana_hat.png");
    private Object model;

        public MontanaHatItem() {
            super(new Properties().maxStackSize(1).group(ModItemGroup.ARCHE_GROUP),29,0 // the item will appear on the Miscellaneous tab in creative
            );
        }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        //return new ICurio.SoundInfo(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 0.6f);
        return new ICurio.SoundInfo(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 0.6f);
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


        if (!(this.model instanceof MontanaHatModel)) {
            this.model = new MontanaHatModel<>();
        }
        MontanaHatModel<?> hatModel = (MontanaHatModel<?>) this.model;
        ICurio.RenderHelper.followHeadRotations(living, hatModel.bipedHead);
        IVertexBuilder vertexBuilder = ItemRenderer
                .getBuffer(renderTypeBuffer, hatModel.getRenderType(HAT_TEXTURE), false,
                        stack.hasEffect());

        IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(HAT_TEXTURE));
        GL11.glEnable(GL11.GL_BLEND);
        hatModel.bipedHead
                .render(matrixStack, ivertexbuilder, light, LivingRenderer.getPackedOverlay(living, 0.0F), 1.0F, 1.0F, 1.0F,
                        1F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 20 == 0) {
            livingEntity
                    .addPotionEffect(new EffectInstance(Effects.HASTE, 41, -1, true, false, false));
        }
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return false;
    }

//look at https://forums.minecraftforge.net/topic/88077-1161-custom-armor-model-textures-not-working-properly/
}

