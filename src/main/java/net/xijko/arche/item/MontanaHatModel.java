package net.xijko.arche.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class MontanaHatModel<T extends LivingEntity> extends BipedModel<LivingEntity> {

    public MontanaHatModel() {
        super(1);
        textureWidth = 64;
        textureHeight = 64;

        this.bipedHead = new ModelRenderer(this);
        ModelRenderer bb_main;
        ModelRenderer cube_r1;
        ModelRenderer cube_r2;

        bb_main = this.bipedHead;
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.setTextureOffset(0, 0).addBox(-6.5F, -8.0F, -6.0F, 11.0F, 1.0F, 12.0F, 0.0F, false);
        bb_main.setTextureOffset(24, 21).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.0F, false);
        bb_main.setTextureOffset(0, 29).addBox(4.5F, -10.0F, -6.0F, 1.0F, 3.0F, 12.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-4.0F, -12.0F, 0.0F);
        bb_main.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.2182F);
        cube_r1.setTextureOffset(14, 33).addBox(0.0F, -1.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(4.0F, -12.0F, 0.0F);
        bb_main.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2182F);
        cube_r2.setTextureOffset(30, 33).addBox(-4.0F, -1.0F, -4.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);

        /*head.setRotationPoint(0.0F, 11.0F, 0.0F);
        head.setTextureOffset(28, 46).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, 0.0F, false);
        */
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){

        matrixStack.scale(1.1F,1.1F,1.1F);
        matrixStack.translate(0,1F,0);
        this.bipedHead.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}