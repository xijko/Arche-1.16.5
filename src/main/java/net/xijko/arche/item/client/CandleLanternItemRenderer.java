package net.xijko.arche.item.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.xijko.arche.entity.client.CandleLanternModel;
import net.xijko.arche.item.CandleLanternItem;
import net.xijko.arche.tileentities.CandleLanternTile;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;

public class CandleLanternItemRenderer extends GeoItemRenderer<CandleLanternItem> {
    public CandleLanternItemRenderer() {
        super(new CandleLanternItemModel());
    }
}
