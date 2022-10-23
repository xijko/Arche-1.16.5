package net.xijko.arche.entity.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.xijko.arche.tileentities.CandleLanternTile;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class CandleLanternRenderer extends GeoBlockRenderer<CandleLanternTile> {
    public CandleLanternRenderer(TileEntityRendererDispatcher rendererDispatcherIn, AnimatedGeoModel<CandleLanternTile> modelProvider) {
        super(rendererDispatcherIn, new CandleLanternModel());
    }

    @Override
    public RenderType getRenderType(CandleLanternTile animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(getTextureLocation(animatable));
    }



}
