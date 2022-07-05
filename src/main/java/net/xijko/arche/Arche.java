package net.xijko.arche;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.block.screen.RestoreTableScreen;
import net.xijko.arche.container.ModContainers;
import net.xijko.arche.events.SpawnEvents;
import net.xijko.arche.inits.ContainerTypeInit;
import net.xijko.arche.inits.ModToolTips;
import net.xijko.arche.inits.TileEntityInit;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.item.ToolBeltItem;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.storages.toolbelt.ToolBeltContainer;
import net.xijko.arche.storages.toolbelt.ToolBeltContainerScreen;
import net.xijko.arche.tileentities.ModTileEntities;
import net.xijko.arche.util.ModKeybinds;
import net.xijko.arche.world.ModFeatures;
import net.xijko.arche.world.gen.ModOreGen;
import net.xijko.arche.world.gen.ModStructureGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Arche.MOD_ID)
public class Arche
{
    public static final String MOD_ID = "arche";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();


    public Arche() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModContainers.register(eventBus);
        ModTileEntities.register(eventBus);
        ModFeatures.register(eventBus);

        //Register Tile Entities
        TileEntityInit.TILE_ENTITY_TYPES.register(eventBus);

        //Register Container Types
        ContainerTypeInit.CONTAINER_TYPES.register(eventBus);

        //ModTileEntities.register(eventBus);

        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //Generate ores
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH,ModOreGen::generateOres);
        //MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, ModStructureGen::generateStructures);

    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        ModOreGen.registerOres();
        ModContainers.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModNetwork.packetRegister();
        MinecraftForge.EVENT_BUS.register(ModToolTips.class);
        MinecraftForge.EVENT_BUS.register(SpawnEvents.class);
        MinecraftForge.EVENT_BUS.addListener(SpawnEvents::spawnEntity);



        //ScreenManager.registerFactory(ModContainers.TOOL_BELT_CONTAINER.get(), ToolBeltContainerScreen:: new);
        //ScreenManager.registerFactory(Arche.containerTypeToolBelt, ToolBeltContainerScreen::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ScreenManager.registerFactory(ModContainers.TOOL_BELT_CONTAINER.get(),
                ToolBeltContainerScreen::new);
        ScreenManager.registerFactory(ModContainers.RESTORE_TABLE_CONTAINER.get(),
                RestoreTableScreen::new);
        ModKeybinds.register();
        RenderTypeLookup.setRenderLayer(ModBlocks.CORPSE_FLOWER.get(), RenderType.getCutout());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
    }

    private void processIMC(final InterModProcessEvent event)
    {

    }

    //ADDITIONS
    public static ToolBeltItem itemToolBelt;  // this holds the unique instance of your block
    public static ContainerType<ToolBeltContainer> containerTypeToolBelt;

    @SubscribeEvent
    public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
        itemToolBelt = new ToolBeltItem();
        itemToolBelt.setRegistryName("tool_belt");
        itemRegisterEvent.getRegistry().register(itemToolBelt);
    }
/*
    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        containerTypeToolBelt = IForgeContainerType.create(ToolBeltContainer::createContainerClientSide);
        containerTypeToolBelt.setRegistryName("tool_belt_container");
        event.getRegistry().register(containerTypeToolBelt);
    }*/

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        // we need to attach the fullness PropertyOverride to the Item, but there are two things to be careful of:
        // 1) We should do this on a client installation only, not on a DedicatedServer installation.  Hence we need to use
        //    FMLClientSetupEvent.
        // 2) FMLClientSetupEvent is multithreaded but ItemModelsProperties is not multithread-safe.  So we need to use the enqueueWork method,
        //    which lets us register a function for synchronous execution in the main thread after the parallel processing is completed

        // register the factory that is used on the client to generate a ContainerScreen corresponding to our Container
        ScreenManager.registerFactory(Arche.containerTypeToolBelt, ToolBeltContainerScreen::new);
    }

    //END ADDITIONS

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }



    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
