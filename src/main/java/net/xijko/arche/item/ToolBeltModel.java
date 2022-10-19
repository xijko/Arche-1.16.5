package net.xijko.arche.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nonnull;

public class ToolBeltModel<T extends LivingEntity> extends EntityModel<T> {
    private final ModelRenderer bb_main;

    public ToolBeltModel() {
        textureWidth = 64;
        textureHeight = 64;

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.setTextureOffset(1, 0).addBox(-5.0F, -15.0F, -3.0F, 10.0F, 3.0F, 6.0F, 0.0F, false);
        bb_main.setTextureOffset(10, 11).addBox(5.0F, -12.0F, -2.0F, 2.0F, 3.0F, 4.0F, 0.0F, false);
        bb_main.setTextureOffset(18, 9).addBox(-2.0F, -15.0F, -4.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 18).addBox(-1.0F, -14.0F, -4.5F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 15).addBox(4.5F, -13.0F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 0).addBox(5.25F, -12.0F, -0.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 9).addBox(4.25F, -14.0F, -2.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);
        bb_main.setTextureOffset(18, 18).addBox(3.5F, -14.5F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bb_main.setTextureOffset(10, 18).addBox(2.25F, -14.5F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 9).addBox(-4.5F, -14.5F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 2).addBox(-3.25F, -14.5F, -3.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        MatrixStack scaledMatrix = matrixStack;
        scaledMatrix.scale(0.9F,0.9F,0.9F);
        scaledMatrix.translate(0,0,0);
        bb_main.render(scaledMatrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(@Nonnull T entity, float limbSwing, float limbSwingAmount,
                                  float ageInTicks, float netHeadYaw, float netHeadPitch) {

    }
}