package net.xijko.arche.world.gen;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.Codec;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.SimplePlacement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.RegistryObject;

import java.util.Random;
import java.util.stream.Stream;

public class StructureOrePlacement<DC extends IPlacementConfig> extends SimplePlacement<StructureOrePlacementConfig> {


    public StructureOrePlacement(Codec<StructureOrePlacementConfig> codec) {
        super(codec);
    }

    public ConfiguredPlacement<StructureOrePlacementConfig> configure(StructureOrePlacementConfig config) {
        return super.configure(config);
    }



    protected Stream<BlockPos> getPositions(Random random, StructureOrePlacementConfig config, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getZ();
        int k = random.nextInt(config.maximum - config.topOffset) + config.bottomOffset;
        BlockPos blockPos = new BlockPos(i, k, j);
        Stream<BlockPos> blockPosStream = Stream.of(new BlockPos(i, k, j));
        return blockPosStream.filter(b-> b.distanceSq(config.blockX,config.blockY, config.blockZ, true)<config.radius);
    }
}
