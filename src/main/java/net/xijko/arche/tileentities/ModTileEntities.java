package net.xijko.arche.tileentities;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ArcheDeposit;
import net.xijko.arche.block.ModBlocks;

public class ModTileEntities {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Arche.MOD_ID);


    public static RegistryObject<TileEntityType<DirtDepositTile>> DIRT_DEPOSIT_TILE =
            TILE_ENTITIES.register("dirt_deposit_tile", () -> TileEntityType.Builder.create(
                    DirtDepositTile::new, ModBlocks.DIRT_DEPOSIT.get()).build(null));

    public static RegistryObject<TileEntityType<SandDepositTile>> SAND_DEPOSIT_TILE =
            TILE_ENTITIES.register("sand_deposit_tile", () -> TileEntityType.Builder.create(
                    SandDepositTile::new, ModBlocks.SAND_DEPOSIT.get()).build(null));

    /*public static RegistryObject<TileEntityType<StoneDepositTile>> STONE_DEPOSIT_TILE =
            TILE_ENTITIES.register("stone_deposit_tile", () -> TileEntityType.Builder.create(
                    StoneDepositTile::new, ModBlocks.STONE_DEPOSIT.get()).build(null));
*/
    public static RegistryObject<TileEntityType<RestoreTableTile>> RESTORE_TABLE_TILE =
            TILE_ENTITIES.register("restore_table_tile", () -> TileEntityType.Builder.create(
                    RestoreTableTile::new, ModBlocks.RESTORE_TABLE.get()).build(null));

    public static RegistryObject<TileEntityType<CorpseFlowerTile>> CORPSE_FLOWER_TILE =
            TILE_ENTITIES.register("corpse_flower_tile", () -> TileEntityType.Builder.create(
                    CorpseFlowerTile::new, ModBlocks.CORPSE_FLOWER.get()).build(null));

    public static RegistryObject<TileEntityType<DisplayPedestalTile>> DISPLAY_PEDESTAL_TILE =
            TILE_ENTITIES.register("display_pedestal_tile", () -> TileEntityType.Builder.create(
                    DisplayPedestalTile::new, ModBlocks.DISPLAY_PEDESTAL.get()).build(null));

    public static RegistryObject<TileEntityType<MuseumCatalogTile>> MUSEUM_CATALOG_TILE =
            TILE_ENTITIES.register("museum_catalog_tile", () -> TileEntityType.Builder.create(
                    MuseumCatalogTile::new, ModBlocks.DISPLAY_PEDESTAL.get()).build(null));


    public static RegistryObject<TileEntityType<CandleLanternTile>> CANDLE_LANTERN_TILE =
            TILE_ENTITIES.register("candle_lantern_tile", () -> TileEntityType.Builder.create(
                    CandleLanternTile::new, ModBlocks.CANDLE_LANTERN.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
