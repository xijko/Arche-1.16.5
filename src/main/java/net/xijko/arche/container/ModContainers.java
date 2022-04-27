package net.xijko.arche.container;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.xijko.arche.Arche;
import net.xijko.arche.item.ModItemGroup;
import net.xijko.arche.item.ToolBeltItem;
import net.xijko.arche.storages.toolbelt.ToolBeltContainer;
import net.xijko.arche.storages.toolbelt.ToolBeltContainerScreen;
import net.xijko.arche.storages.toolbelt.ToolBeltItemStackHandler;


public class ModContainers {

    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, Arche.MOD_ID);

    public static final RegistryObject<ContainerType<ToolBeltContainer>> TOOL_BELT_CONTAINER
            = CONTAINERS.register("tool_belt_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                return new ToolBeltContainer(windowId, inv, new ToolBeltItemStackHandler(12),null);
            })));
    public static final RegistryObject<ContainerType<CleaningTableContainer>> CLEANING_TABLE_CONTAINER
            = CONTAINERS.register("cleaning_table_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getEntityWorld();
                return new CleaningTableContainer(windowId, world, pos, inv, inv.player);
            })));


    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

}
