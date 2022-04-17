package net.xijko.arche.inits;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.storages.examplestorage.ExampleStorageContainer;

public class ContainerTypeInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Arche.MOD_ID);
    public static final RegistryObject<ContainerType<ExampleStorageContainer>> EXAMPLE_STORAGE_CONTAINER = CONTAINER_TYPES.register("storage",
            ()-> IForgeContainerType.create(ExampleStorageContainer::getClientContainer));
}
