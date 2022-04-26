package net.xijko.arche.inits;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.storages.examplestorage.ExampleStorageTE;

public class TileEntityInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Arche.MOD_ID);
    public static final RegistryObject<TileEntityType<ExampleStorageTE>> STORAGE_TE = TILE_ENTITY_TYPES.register("storage",
            ()-> TileEntityType.Builder.create(ExampleStorageTE::new, ModBlocks.EXAMPLE_STORAGE.get()).build(null));
}
