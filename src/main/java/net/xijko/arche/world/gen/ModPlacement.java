package net.xijko.arche.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.*;

import java.util.Random;
import java.util.stream.Stream;

public class ModPlacement extends Placement{
    public static final Placement<StructureOrePlacementConfig> STRUCTURE_ORE = register("range", new StructureOrePlacement(StructureOrePlacementConfig.CODEC));

    public ModPlacement(Codec codec) {
        super(codec);
    }


    private static <T extends IPlacementConfig, G extends Placement<T>> G register(String key, G placement) {
        return Registry.register(Registry.DECORATOR, key, placement);
    }

    @Override
    public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, IPlacementConfig config, BlockPos pos) {
        return null;
    }

}
