package net.xijko.arche.equipment;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import javax.annotation.Nullable;
import java.util.Optional;

public class ToolBeltEquipment extends ArmorItem{
    public ToolBeltEquipment(IArmorMaterial materialIn, Properties builderIn) {
        super(ArmorMaterial.LEATHER, EquipmentSlotType.CHEST, builderIn);
    }

    @Nullable
    @Override
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {


        //PlayerModel model = new PlayerModel(1,false);
        model.bipedBodyWear.showModel = armorSlot == EquipmentSlotType.HEAD;
        model.isChild = _default.isChild;
        model.isSitting = _default.isSitting;
        model.isSneak = _default.isSneak;
        model.rightArmPose = _default.rightArmPose;
        model.leftArmPose = _default.leftArmPose;

        return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return super.getArmorTexture(stack, entity, slot, type);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.Helmet.copyModelAngles(this.bipedHead);
        this.Chestplate.copyModelAngles(this.bipedBody);
        this.LeftSleeve.copyModelAngles(this.bipedLeftArm);
        this.RightSleeve.copyModelAngles(this.bipedRightArm);
        this.LeftLegging.copyModelAngles(this.bipedLeftLeg);
        this.RightLegging.copyModelAngles(this.bipedRightLeg);
        this.LeftBoot.copyModelAngles(this.bipedLeftLeg);
        this.RightBoot.copyModelAngles(this.bipedRightLeg);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}
