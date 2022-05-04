package net.xijko.arche.inits;

import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.xijko.arche.item.ArcheArtifactBroken;

public class ModToolTips {

    @SubscribeEvent
    public static void onRenderTooltip(final RenderTooltipEvent.Pre event) {
        if(event.getStack().getItem() instanceof ArcheArtifactBroken) {
            //DO STUFF
            int xpos = event.getX();
            int ypos = event.getY();

        }
    }

}
