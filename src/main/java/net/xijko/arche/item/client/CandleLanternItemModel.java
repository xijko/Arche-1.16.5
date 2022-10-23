package net.xijko.arche.item.client;

import net.minecraft.util.ResourceLocation;
import net.xijko.arche.Arche;
import net.xijko.arche.item.CandleLanternItem;
import net.xijko.arche.tileentities.CandleLanternTile;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CandleLanternItemModel extends AnimatedGeoModel<CandleLanternItem> {

    @Override
    public ResourceLocation getModelLocation(CandleLanternItem object) {
        return new ResourceLocation(Arche.MOD_ID, "geo/candle_lantern_item.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CandleLanternItem object) {
        return new ResourceLocation(Arche.MOD_ID, "textures/block/candle_lantern.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CandleLanternItem animatable) {
        return new ResourceLocation(Arche.MOD_ID, "animations/candle_lantern.animation.json");
    }
}
