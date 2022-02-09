package net.xijko.tutorialmod.world.gen;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.xijko.tutorialmod.block.PoopDeposit;

import static net.minecraft.client.renderer.FaceDirection.NORTH;

public class ModOreGeneration {
    public static void generateOres(final BiomeLoadingEvent event){
        for (OreType ore : OreType.values()){
            OreFeatureConfig oreFeatureConfig = new OreFeatureConfig(
                    //Feature.RANDOM_PATCH.withConfiguration((new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(ore.getOreBlock().get().getDefaultState(), SimpleBlockPlacer.PLACER)).tries(64).preventProjection().build()));
                    OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
                    ore.getOreBlock().get().getDefaultState(),
            ore.getMaxVeinSize());

            /*
            ConfiguredPlacement<TopSolidRangeConfig> configuredPlacement = Placement.HEIGHTMAP_WORLD_SURFACE.configure(
                    new TopSolidRangeConfig(ore.getMinHeight(), ore.getMinHeight(), ore.getMinHeight())
            );*/

            ConfiguredPlacement<TopSolidRangeConfig> configuredPlacement = Placement.RANGE.configure(
                    new TopSolidRangeConfig(ore.getMinHeight(), ore.getMinHeight(), ore.getMinHeight())
            );
        }
    }
}
