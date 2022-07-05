package net.xijko.arche.world;


import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.xijko.arche.Arche;
import net.xijko.arche.world.gen.ModStructureGen;
import net.xijko.arche.world.gen.StructureOrePlacement;
import net.xijko.arche.world.gen.StructureOrePlacementConfig;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Arche.MOD_ID)
public class ModWorldEvents {

    private static final ArrayList<ConfiguredFeature<?, ?>> overworld = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> nether = new ArrayList<ConfiguredFeature<?, ?>>();
    private static final ArrayList<ConfiguredFeature<?, ?>> end = new ArrayList<ConfiguredFeature<?, ?>>();


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {

        ModStructureGen.generateStructures(event);

        /*ModOreGeneration.generateOres(event);
        ModFlowerGeneration.generateFlowers(event);
        ModTreeGeneration.generateTrees(event);*/
    }
/*
    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            try {
                Method GETCODEC_METHOD =
                        ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey(
                        (Codec<? extends ChunkGenerator>)GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));

                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) {
                    return;
                }
            } catch (Exception e) {
                LogManager.getLogger().error("Was unable to check if " + serverWorld.getDimensionKey().getLocation()
                        + " is using Terraforged's ChunkGenerator.");
            }

            // Prevent spawning our structure in Vanilla's superflat world
            if (serverWorld.getChunkProvider().generator instanceof FlatChunkGenerator &&
                    serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                return;
            }

            // Adding our Structure to the Map
            generateStructureOre(overworldOres,
                    Blocks.SAND,
                    Blocks.DIAMOND_BLOCK,
                    25,
                    26,
                    200,
                    event.getGeneration(),
                    20);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void generateStructureOre(ArrayList<ConfiguredFeature<?, ?>> location, Block bTarget, Block bReplace, int bottomOffset, int topOffset, int maximum, BiomeGenerationSettingsBuilder settings, int amount) {

        StructureOrePlacement sop = new StructureOrePlacement<>(StructureOrePlacementConfig.CODEC);

        BlockMatchRuleTest blockMatchRuleTest = new BlockMatchRuleTest(bTarget);
        location.add(register(bReplace.getRegistryName().getPath(), Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                .withPlacement(sop.configure(new StructureOrePlacementConfig(bottomOffset,topOffset,maximum,x,y,z,radius)))
                .square().count(amount)));

        settings.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.withConfiguration(new OreFeatureConfig(blockMatchRuleTest, defaultState, veinSize))
                        .withPlacement(sop.configure(new StructureOrePlacementConfig(bottomOffset,topOffset,maximum,x,y,z,radius)))
                        .square().count(amount)
        );
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Arche.MOD_ID + ":" + name, configuredFeature);
    }
    */

}
