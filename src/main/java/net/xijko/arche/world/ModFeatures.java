package net.xijko.arche.world;

import com.mojang.serialization.Codec;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.tileentities.CorpseFlowerTile;
import net.xijko.arche.tileentities.RestoreTableTile;

public class ModFeatures {

    public static DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, Arche.MOD_ID);

    /*public static RegistryObject<StructureOreFeature> STRUCTURE_ORE_FEATURE =
            FEATURES.register("structure_ore_feature", StructureOreFeature::new);*/

    //public static RegistryObject<Feature<StructureOreFeatureConfig>> STRUCTURE_ORE = FEATURES.register("structure_ore", new StructureOreFeature(StructureOreFeatureConfig.ORE.getCodec()));

    //public static final Feature<OreFeatureConfig> ORE = register("ore", new OreFeature(OreFeatureConfig.CODEC));

    /*private static <C extends IFeatureConfig, F extends Feature<C>> F register(String key, F value) {
          return FEATURES.register(key, value);
    }*/

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }

}
