package net.xijko.arche.world.gen;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.feature.structure.StructureIndexesSavedData;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.DecoratedPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.world.ModFeatures;

import java.util.ArrayList;
import java.util.function.Supplier;

import static net.xijko.arche.block.ArcheDeposit.GENERATED;


/*
@Mod.EventBusSubscriber(modid = Arche.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModOreGen {

    public static OreFeatureConfig.FillerBlockType OVER_DIRT = OreFeatureConfig.FillerBlockType.create("OVER_DIRT", "over_dirt", new BlockMatcher(Blocks.END_STONE));

    @SubscribeEvent
    public static void generateOres(FMLLoadCompleteEvent event) { //when done loading
        for (Biome biome : ForgeRegistries.BIOMES) { //for each registered biome in the game
            if (biome.getCategory() == Biome.Category.NETHER) { //if it's the nether...

            } else if (biome.getCategory() == Biome.Category.THEEND) { //if it's the end...

            } else { //otherwise...
                genOre(biome, 10, 20, 1, 300, OreFeatureConfig.FillerBlockType.OVER_DIRT, RegistryHandler.DIRT_DEPOSIT.Get().getDefaultState(),20);
            }
        }
    }

    //
    private static void genOre(Biome biome, int count, int bottomOffset, int topOffset, int max, OreFeatureConfig.FillerBlockType filler, BlockState defaultBlockstate, int size)
    {
        CountRangeConfig range = new CountRangeConfig(count, bottomOffset, topOffset, max);
        OreFeatureConfig feature = new OreFeatureConfig(filler, defaultBlockstate, size);
        ConfiguredPlacement config = Placement.COUNT_RANGE.configure(range);
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(feature).withPlacement(config));
    }

}

*/

/**
 * Ore generation
 * @author TechOFreak
 * Add ModOreGen.registerOres(); to your main class's setup method
 * https://pastebin.com/naAYBnjr
 */

@Mod.EventBusSubscriber
public class ModOreGen {

    private static final ArrayList<ConfiguredFeature<?, ?>> overworldOres = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> netherOres = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> endOres = new ArrayList<ConfiguredFeature<?, ?>>();

    public static void registerOres(){
        //field_241882_a is for generating in stone, granite, diorite, and andesite
        //field_241883_b is for generating in netherrack
        //field_241884_c is for generating in netherrack, basalt and blackstone

        //Overworld Ore Register
        /*overworldOres.add(register("dirt_deposit", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        new BlockMatchRuleTest(Blocks.DIRT), ModBlocks.DIRT_DEPOSIT.get().getDefaultState(), 20)) //Vein Size
                .range(300).square() //Spawn height start
                .chance(10))); //Chunk spawn frequency*/

        //Nether Ore Register
        /*netherOres.add(register("example_nether_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER, ModBlocks.DIRT_DEPOSIT.get().getDefaultState(), 4)) //Vein Size
                .func_242733_d(64).func_242728_a() //Spawn height start
                .func_242731_b(64))); //Chunk spawn frequency
        */
        //The End Ore Register
        /*endOres.add(register("example_end_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(
                        new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.DIRT_DEPOSIT.get().getDefaultState(), 4)) //Vein Size
                .func_242733_d(128).func_242728_a() //Spawn height start
                .func_242731_b(64))); //Chunk spawn frequency
         */
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void generateOres(final BiomeLoadingEvent event) {
        if (!(event.getCategory().equals(Biome.Category.NETHER) && event.getCategory().equals(Biome.Category.THEEND))) {
        generateOre(overworldOres,
                "dirt_deposit",
                event.getGeneration(),
                    new BlockMatchRuleTest(Blocks.DIRT),
                    ModBlocks.DIRT_DEPOSIT.get().getDefaultState().with(GENERATED,false),
                    25,
                    5,
                    200,
                    20);

        generateOre(overworldOres,
                "sand_deposit",
                event.getGeneration(),
                new BlockMatchRuleTest(Blocks.SAND),
                ModBlocks.SAND_DEPOSIT.get().getDefaultState().with(GENERATED,true),
                25,
                5,
                200,
                20);

            /*generateStructureOre(overworldOres,
                    event.getGeneration(),
                    Blocks.SAND,
                    Blocks.DIAMOND_BLOCK,
                    25,
                    5,
                    200,
                    20);*/

        generateOre(overworldOres,
                "stone_deposit",
                event.getGeneration(),
                new BlockMatchRuleTest(Blocks.STONE),
                ModBlocks.STONE_DEPOSIT.get().getDefaultState().with(GENERATED,false),
                25,
                20,
                60,
                10);

        generateOre(overworldOres,
                "obsidian_deposit",
                event.getGeneration(),
                new BlockMatchRuleTest(Blocks.LAVA),
                ModBlocks.OBSIDIAN_DEPOSIT.get().getDefaultState().with(GENERATED,false),
                15,
                0,
                20,
                5);

        }else if(event.getCategory().equals(Biome.Category.NETHER)){
            generateOre(netherOres,
                    "netherrack_deposit",
                    event.getGeneration(),
                    new BlockMatchRuleTest(Blocks.NETHERRACK),
                    ModBlocks.NETHERRACK_DEPOSIT.get().getDefaultState().with(GENERATED,false),
                    25,
                    0,
                    64,
                    30);

        }else if(event.getCategory().equals(Biome.Category.THEEND)) {
            generateOre(endOres,
                    "endstone_deposit",
                    event.getGeneration(),
                    new BlockMatchRuleTest(Blocks.END_STONE),
                    ModBlocks.ENDSTONE_DEPOSIT.get().getDefaultState().with(GENERATED,false),
                    25,
                    26,
                    51,
                    30);

        }

            /*generateOre(overworldOres,
                    "sand_deposit",
                    event.getGeneration(),
                    new BlockMatchRuleTest(Blocks.SAND),
                    ModBlocks.SAND_DEPOSIT.get().getDefaultState().with(GENERATED,false),
                    25,
                    26,
                    200,
                    30);

             */
    }

    /*@SubscribeEvent(priority = EventPriority.LOWEST)
    public static void generateStructureOres(final BiomeLoadingEvent event) {
                generateStructureOre(overworldOres,
                        "diamond_block",
                        Structure.DESERT_PYRAMID,
                        Blocks.SAND,
                        Blocks.DIAMOND_BLOCK,
                        64,
                        event.getGeneration(),
                        new BlockMatchRuleTest(Blocks.SAND),
                        Blocks.DIAMOND_BLOCK.getDefaultState(),
                        25,
                        26,
                        200,
                        300);
    }*/

    static void generateOre(ArrayList<ConfiguredFeature<?, ?>> location, String block, BiomeGenerationSettingsBuilder settings, BlockMatchRuleTest blockMatchRuleTest, BlockState defaultState, int veinSize, int minHeight, int maxHeight, int amount) {

        location.add(register(block,Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .square().count(amount)));
        settings.withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
                        Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .square().count(amount)
        );
    }
    /*private static void generateStructureOre(ArrayList<ConfiguredFeature<?, ?>> location, Structure struct, Block blockTarget, Block blockReplace, int radius, BiomeGenerationSettingsBuilder settings, int veinSize, int minHeight, int maxHeight, int amount) {
        location.add(register(blockTarget.getRegistryName().getPath(), ModFeatures.STRUCTURE_ORE_FEATURE.get().withConfiguration(new StructureOreFeatureConfig((Codec<OreFeatureConfig>) StructureOreFeatureConfig.CODEC,struct,blockTarget,blockReplace))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                .square().count(amount)));

        settings.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                ModFeatures.STRUCTURE_ORE_FEATURE.get().withConfiguration(new StructureOreFeatureConfig((Codec<OreFeatureConfig>) StructureOreFeatureConfig.CODEC,struct,blockTarget,blockReplace))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .square().count(amount)
        );
    }*/
    /*public static void gen(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if(event.getCategory().equals(Biome.Category.NETHER)){
            for(ConfiguredFeature<?, ?> ore : netherOres){
                if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
            }
        }
        if(event.getCategory().equals(Biome.Category.THEEND)){
            for(ConfiguredFeature<?, ?> ore : endOres){
                if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
            }
        }
        for(ConfiguredFeature<?, ?> ore : overworldOres){
            if (ore != null) generation.withFeature(GenerationStage.Decoration.RAW_GENERATION, ore);
        }
    }*/

    static void generateStructureOre(ArrayList<ConfiguredFeature<?, ?>> location, BiomeGenerationSettingsBuilder settings, Block bTarget, Block bReplace, int veinSize, int minHeight, int maxHeight, int amount) {

        StructureOrePlacement sop = new StructureOrePlacement<>(StructureOrePlacementConfig.CODEC);
        BlockState defaultState = bTarget.getDefaultState();

        BlockMatchRuleTest blockMatchRuleTest = new BlockMatchRuleTest(bTarget);
        location.add(register(bReplace.getRegistryName().getPath(), Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight))))
                .square().count(amount));

        settings.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
                        .square().count(amount)
        );
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Arche.MOD_ID + ":" + name, configuredFeature);
    }



}