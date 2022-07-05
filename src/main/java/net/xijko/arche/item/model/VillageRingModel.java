package net.xijko.arche.item.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Quaternion;

public class VillageRingModel  extends BipedModel<LivingEntity> {

    public VillageRingModel() {
        super(0.33F);



        this.textureWidth = 4;
        this.textureHeight = 4;

        this.bipedLeftArm = new ModelRenderer(this, 0, 0);
        this.bipedLeftArm.setRotationPoint(6F, 2.0F, 0.0F);
        this.bipedLeftArm.setTextureOffset(0, 1).addBox(1.9F, 9.25F, -1.9F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        MatrixStack scaledMatrix = matrixStack;
        scaledMatrix.scale(1F,1F,1F);
        scaledMatrix.translate(0,0,0);
        this.bipedLeftArm.render(scaledMatrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
