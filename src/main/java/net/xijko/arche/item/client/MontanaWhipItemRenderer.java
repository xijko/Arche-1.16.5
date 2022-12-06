package net.xijko.arche.item.client;

import net.xijko.arche.item.CandleLanternItem;
import net.xijko.arche.item.MontanaWhipItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MontanaWhipItemRenderer extends GeoItemRenderer<MontanaWhipItem> {
    public MontanaWhipItemRenderer() {
        super(new MontanaWhipItemModel());
    }
}
