package net.xijko.arche.item.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.xijko.arche.Arche;
import net.xijko.arche.item.CandleLanternItem;
import net.xijko.arche.item.MontanaWhipItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MontanaWhipItemModel extends AnimatedGeoModel<MontanaWhipItem> {

    @Override
    public ResourceLocation getModelLocation(MontanaWhipItem object) {
        return new ResourceLocation(Arche.MOD_ID, "geo/montana_whip_item.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(MontanaWhipItem object) {
        return new ResourceLocation(Arche.MOD_ID, "textures/item/montana_whip_item.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MontanaWhipItem animatable) {
        return new ResourceLocation(Arche.MOD_ID, "animations/montana_whip_item.animation.json");
    }
}
