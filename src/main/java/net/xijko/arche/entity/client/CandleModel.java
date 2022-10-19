package net.xijko.arche.entity.client;

import net.minecraft.util.ResourceLocation;
import net.xijko.arche.Arche;
import net.xijko.arche.entity.CandleEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CandleModel extends AnimatedGeoModel<CandleEntity> {
    @Override
    public ResourceLocation getModelLocation(CandleEntity object) {
        return new ResourceLocation(Arche.MOD_ID,"geo/candle_entity.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CandleEntity object) {
        return new ResourceLocation(Arche.MOD_ID,"textures/entity/candle_entity.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CandleEntity animatable) {
        return new ResourceLocation(Arche.MOD_ID,"animations/candle_entity.animation.json");
    }

    //GeckoLib specific

}
