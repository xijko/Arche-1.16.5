package net.xijko.arche.world.structures.village;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.IJigsawDeserializer;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.LegacySingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.JigsawStructure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.*;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class MuseumStructure extends JigsawPiece {


    protected MuseumStructure(JigsawPattern.PlacementBehaviour p_i51398_1_) {
        super(p_i51398_1_);
    }

    @Override
    public int getGroundLevelDelta() {
        return -5;
    }

}
