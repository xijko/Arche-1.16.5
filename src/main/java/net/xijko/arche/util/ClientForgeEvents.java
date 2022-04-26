package net.xijko.arche.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xijko.arche.Arche;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.ToolBeltOpenMessage;

@Mod.EventBusSubscriber(modid = Arche.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEvents {
    private ClientForgeEvents() {
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void clientTick(InputEvent.KeyInputEvent event) {
        if (ModKeybinds.ToolBeltKey.isPressed()) {
            //if (!Minecraft.getInstance().player.world.isRemote) {
                ModNetwork.sendToServer(new ToolBeltOpenMessage());

        }
    }
}