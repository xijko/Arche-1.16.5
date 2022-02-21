package net.xijko.tutorialmod.world.gen;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xijko.tutorialmod.TutorialMod;
import net.xijko.tutorialmod.block.ModBlocks;

import java.util.ArrayList;

/**
 * Ore generation
 * @author TechOFreak
 *
 */

@Mod.EventBusSubscriber
public class OreGenerationOLD {

    private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> netherOres = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> endOres = new ArrayList<ConfiguredFeature<?, ?>>();

    private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest fillertype, BlockState state, int veinSize, int minHeight, int maxHeight, int amount) {
        settings.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION)
    }
    public static void registerOres(){
        //BASE_STONE_OVERWORLD is for generating in stone, granite, diorite, and andesite
        //NETHERRACK is for generating in netherrack
        //BASE_STONE_NETHER is for generating in netherrack, basalt and blackstone

        //Overworld Ore Register
       overworldOres.add(register("neutralis_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, RegistryHandlerBlocks.NEUTRALIS_ORE.get().getDefaultState(), 4)) //Vein Size
                .range(64).square() //Spawn height start
                .func_242731_b(64))); //Chunk spawn frequency*/


        overworldOres.add(register("poop_deposit", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        new BlockMatchRuleTest(Blocks.TALL_GRASS), ModBlocks.POOP_DEPOSIT.get().getDefaultState(), 4)) //Vein Size Max
                .range(128).square() //Spawn height start
                .chance(64) )); //Chunk spawn frequency

        overworldOres.add(register("poop_deposit", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        new BlockMatchRuleTest(Blocks.TALL_GRASS), ModBlocks.POOP_DEPOSIT.get().getDefaultState(), 4)) //Vein Size Max
                .range(200).square() //Spawn height start
                .chance(128) )); //Chunk spawn frequency

        //Nether Ore Register
        netherOres.add(register("flame_crystal_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.NETHERRACK, RegistryHandlerBlocks.FLAME_CRYSTAL_ORE.get().getDefaultState(), 4))
                .range(48).square()
                .func_242731_b(64)));

        //The End Ore Register
       endOres.add(register("air_block", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        new BlockMatchRuleTest(Blocks.END_STONE), RegistryHandlerBlocks.AIR_CRYSTAL_BLOCK.get().getDefaultState(), 4)) //Vein Size
                .range(128).square() //Spawn height start
                .func_242731_b(64))); //Chunk spawn frequency
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if(event.getCategory().equals(Biome.Category.NETHER)){
            for(ConfiguredFeature<?, ?> ore : netherOres){
                if (ore != null) generation.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ore);
            }
        }
        if(event.getCategory().equals(Biome.Category.THEEND)){
            for(ConfiguredFeature<?, ?> ore : endOres){
                if (ore != null) generation.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ore);
            }
        }
        for(ConfiguredFeature<?, ?> ore : overworldOres){
            if (ore != null) generation.withFeature(biomeIn.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(Feature.FOREST_ROCK, new BlockBlobConfig(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0), Placement.FOREST_ROCK, new FrequencyConfig(3)));
);
        }
    }
/*
    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, TutorialMod.MOD_ID + ":" + name, configuredFeature);
    }

*/}