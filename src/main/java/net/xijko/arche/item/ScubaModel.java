package net.xijko.arche.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nonnull;

public class ScubaModel<T extends LivingEntity> extends BipedModel<LivingEntity> {

    public ScubaModel() {
        super(1);
        textureWidth = 64;
        textureHeight = 64;

        this.bipedBody = new ModelRenderer(this);
        ModelRenderer back = this.bipedBody;

        this.bipedHead = new ModelRenderer(this);
        ModelRenderer head = this.bipedHead;

        back.setRotationPoint(0.0F, 11.0F, 0.0F);
        back.setTextureOffset(0, 6).addBox(-2.5F, -13.5F, 5.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        back.setTextureOffset(0, 0).addBox(-4.0F, -11.0F, 2.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        back.setTextureOffset(0, 26).addBox(-4.5F, -5.5F, 1.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
        back.setTextureOffset(27, 7).addBox(-4.5F, -9.5F, 1.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
        back.setTextureOffset(24, 0).addBox(-4.5F, -3.5F, -2.5F, 9.0F, 1.0F, 5.0F, 0.0F, false);
        back.setTextureOffset(52, 28).addBox(-2.5F, -11.5F, -2.5F, 1.0F, 8.0F, 5.0F, 0.0F, false);
        back.setTextureOffset(40, 28).addBox(1.5F, -11.5F, -2.5F, 1.0F, 8.0F, 5.0F, 0.0F, false);
        back.setTextureOffset(0, 0).addBox(-1.5F, -9.5F, -2.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        back.setTextureOffset(0, 16).addBox(-1.0F, -9.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        back.setTextureOffset(0, 3).addBox(-3.0F, -11.5F, 5.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        back.setTextureOffset(5, 17).addBox(-2.5F, -12.5F, 5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        back.setTextureOffset(12, 37).addBox(-0.5F, -13.5F, 0.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);


        head.setRotationPoint(0.0F, 54.0F, 0.0F);
        head.setTextureOffset(28, 46).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){

        matrixStack.scale(1F,1F,1F);
        matrixStack.translate(0,0,0);
        this.bipedHead.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha*0.2F);


        matrixStack.scale(1F,1F,1F);
        matrixStack.translate(0,0,0);
        this.bipedBody.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}