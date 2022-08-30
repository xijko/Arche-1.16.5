package net.xijko.arche.container;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.storages.toolbelt.ToolBeltContainer;
import net.xijko.arche.storages.toolbelt.ToolBeltItemStackHandler;


public class ModContainers {

    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, Arche.MOD_ID);

    public static final RegistryObject<ContainerType<ToolBeltContainer>> TOOL_BELT_CONTAINER
            = CONTAINERS.register("tool_belt_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                return new ToolBeltContainer(windowId, inv, new ToolBeltItemStackHandler(12),null);
            })));
    public static final RegistryObject<ContainerType<RestoreTableContainer>> RESTORE_TABLE_CONTAINER
            = CONTAINERS.register("restore_table_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getEntityWorld();
                return new RestoreTableContainer(windowId, world, pos, inv, inv.player);
            })));
    public static final RegistryObject<ContainerType<DisplayPedestalContainer>> DISPLAY_PEDESTAL_CONTAINER
            = CONTAINERS.register("display_pedestal_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getEntityWorld();
                return new DisplayPedestalContainer(windowId, world, pos, inv, inv.player);
            })));

    public static final RegistryObject<ContainerType<MuseumCatalogContainer>> MUSEUM_CATALOG_CONTAINER
            = CONTAINERS.register("museum_catalog_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getEntityWorld();
                return new MuseumCatalogContainer(windowId, world, pos, inv, inv.player);
            })));


    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

}
