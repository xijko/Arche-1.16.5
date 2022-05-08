package net.xijko.arche.tileentities;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;

public class ModTileEntities {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Arche.MOD_ID);

    public static RegistryObject<TileEntityType<RestoreTableTile>> RESTORE_TABLE_TILE =
            TILE_ENTITIES.register("restore_table_tile", () -> TileEntityType.Builder.create(
                    RestoreTableTile::new, ModBlocks.RESTORE_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
