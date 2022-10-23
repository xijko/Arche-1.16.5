package net.xijko.arche.tileentities.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.xijko.arche.entity.client.CandleLanternModel;
import net.xijko.arche.tileentities.CandleLanternTile;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CandleLanternTileRenderer extends GeoBlockRenderer<CandleLanternTile> {
    public CandleLanternTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new CandleLanternModel());
    }

    @Override
    public RenderType getRenderType(CandleLanternTile animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(getTextureLocation(animatable));
    }
}
