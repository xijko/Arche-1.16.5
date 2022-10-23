package net.xijko.arche.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.xijko.arche.block.CandleLanternBlock;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.item.client.CandleLanternItemRenderer;
import software.bernie.example.client.renderer.item.PistolRender;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.GeckoLibNetwork;

public class CandleLanternItem extends BlockItem implements IAnimatable {
    private final AnimationFactory manager = new AnimationFactory(this);

    public CandleLanternItem() {
        super(ModBlocks.CANDLE_LANTERN.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP).setISTER(() -> CandleLanternItemRenderer::new));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){return PlayState.CONTINUE;}

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"controller",0,this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.manager;
    }
}
