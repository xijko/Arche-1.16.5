package net.xijko.arche.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModStructureGen {
    private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> netherOres = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> endOres = new ArrayList<ConfiguredFeature<?, ?>>();

    private static final Logger LOGGER = LogManager.getLogger();

    //@SubscribeEvent(priority = EventPriority.HIGH)
    public static void generateStructures(BiomeLoadingEvent event) {
        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        LOGGER.warn("BIOME GEN INFO HERE BOI - " + ForgeRegistries.STRUCTURE_FEATURES.getValues());

        event.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).forEach(configuredFeatureSupplier -> {
            if (configuredFeatureSupplier.get().getFeature().getRegistryName() == Structure.DESERT_PYRAMID.getRegistryName()) {
                generateStructureOre(overworldOres,
                        Blocks.SAND,
                        Blocks.DIAMOND_BLOCK,
                        25,
                        26,
                        200,
                        event.getGeneration(),
                        20);
            }
        });
    }

    static void generateStructureOre(ArrayList<ConfiguredFeature<?, ?>> location, Block bTarget, Block bReplace, int bottomOffset, int topOffset, int maximum, BiomeGenerationSettingsBuilder settings, int amount) {

        StructureOrePlacement sop = new StructureOrePlacement<>(StructureOrePlacementConfig.CODEC);
        BlockState defaultState = bTarget.getDefaultState();
        int veinSize = 8;

        BlockMatchRuleTest blockMatchRuleTest = new BlockMatchRuleTest(bTarget);
        location.add(register(bReplace.getRegistryName().getPath(), Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(topOffset, maximum, amount))))
                .square().count(amount));

        settings.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(topOffset, maximum, amount)))
                        .square().count(amount)
        );
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Arche.MOD_ID + ":" + name, configuredFeature);
    }


}