package net.xijko.arche;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.structure.JigsawStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.entity.CandleEntity;
import net.xijko.arche.entity.ModEntityTypes;
import net.xijko.arche.entity.client.CandleLanternRenderer;
import net.xijko.arche.entity.client.CandleRenderer;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.item.client.MontanaWhipItemRenderer;
import net.xijko.arche.screen.DisplayPedestalScreen;
import net.xijko.arche.screen.MuseumCatalogScreen;
import net.xijko.arche.screen.RestoreTableScreen;
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
import net.xijko.arche.structures.ModStructures;
import net.xijko.arche.tileentities.CandleLanternTile;
import net.xijko.arche.tileentities.ModTileEntities;
import net.xijko.arche.tileentities.client.CandleLanternTileRenderer;
import net.xijko.arche.util.ModKeybinds;
import net.xijko.arche.util.render.DisplayPedestalTileRenderer;
import net.xijko.arche.villagers.ModVillagers;
import net.xijko.arche.world.gen.ModOreGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Arche.MOD_ID)
public class Arche
{
    public static final String MOD_ID = "arche";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static ArcheArtifactItem[] ARTIFACT_ITEM_LISTS = new ArcheArtifactItem[32];


    public Arche() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModContainers.register(eventBus);
        ModTileEntities.register(eventBus);
        ModEntityTypes.register(eventBus);

        //Register Tile Entities
        TileEntityInit.TILE_ENTITY_TYPES.register(eventBus);

        //Register Container Types
        ContainerTypeInit.CONTAINER_TYPES.register(eventBus);

        ModVillagers.register(eventBus);


        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        GeckoLib.initialize();

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
        //MinecraftForge.EVENT_BUS.addListener(ModStructures::addNewVillageBuilding);

        event.enqueueWork(ModVillagers::registerPOIs);
        event.enqueueWork(ModStructures::addNewVillageBuilding);

//K9#8016


        //ScreenManager.registerFactory(ModContainers.TOOL_BELT_CONTAINER.get(), ToolBeltContainerScreen:: new);
        //ScreenManager.registerFactory(Arche.containerTypeToolBelt, ToolBeltContainerScreen::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ScreenManager.registerFactory(ModContainers.TOOL_BELT_CONTAINER.get(),
                ToolBeltContainerScreen::new);
        ScreenManager.registerFactory(ModContainers.RESTORE_TABLE_CONTAINER.get(),
                RestoreTableScreen::new);
        ScreenManager.registerFactory(ModContainers.DISPLAY_PEDESTAL_CONTAINER.get(),
                DisplayPedestalScreen::new);
        ScreenManager.registerFactory(ModContainers.MUSEUM_CATALOG_CONTAINER.get(),
                MuseumCatalogScreen::new);
        ModKeybinds.register();
        RenderTypeLookup.setRenderLayer(ModBlocks.CORPSE_FLOWER.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.DISPLAY_PEDESTAL.get(), RenderType.getTranslucent());
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.DISPLAY_PEDESTAL_TILE.get(), DisplayPedestalTileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CANDLE.get(), CandleRenderer::new);
       ClientRegistry.bindTileEntityRenderer(ModTileEntities.CANDLE_LANTERN_TILE.get(), CandleLanternTileRenderer::new);


    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BODY.getMessageBuilder().build());
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
