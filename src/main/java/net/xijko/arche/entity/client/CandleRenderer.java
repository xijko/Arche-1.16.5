package net.xijko.arche.entity.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.xijko.arche.entity.CandleEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class CandleRenderer extends GeoEntityRenderer<CandleEntity> {
    public CandleRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CandleModel());
        this.shadowSize = 0.2F;
    }

    @Override
    public ResourceLocation getEntityTexture(CandleEntity entity) {
        return null;
    }



    @Override
    public RenderType getRenderType(CandleEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

    //GeckoLib specific

}
