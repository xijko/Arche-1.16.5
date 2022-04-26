package net.xijko.arche.events;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.xijko.arche.Arche;
import net.xijko.arche.inits.ContainerTypeInit;
import net.xijko.arche.storages.examplestorage.ExampleStorageScreen;

@Mod.EventBusSubscriber(modid = Arche.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        ScreenManager.registerFactory(ContainerTypeInit.EXAMPLE_STORAGE_CONTAINER.get(), ExampleStorageScreen::new);
    }

}
