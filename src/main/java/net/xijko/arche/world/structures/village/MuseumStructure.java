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

public class MuseumStructure extends JigsawPiece {


    protected MuseumStructure(JigsawPattern.PlacementBehaviour p_i51398_1_) {
        super(p_i51398_1_);
    }

    @Override
    public List<Template.BlockInfo> getJigsawBlocks(TemplateManager templateManagerIn, BlockPos pos, Rotation rotationIn, Random rand) {
        return null;
    }

    @Override
    public MutableBoundingBox getBoundingBox(TemplateManager templateManagerIn, BlockPos pos, Rotation rotationIn) {
        return null;
    }

    @Override
    public boolean func_230378_a_(TemplateManager p_230378_1_, ISeedReader p_230378_2_, StructureManager p_230378_3_, ChunkGenerator p_230378_4_, BlockPos p_230378_5_, BlockPos p_230378_6_, Rotation p_230378_7_, MutableBoundingBox p_230378_8_, Random p_230378_9_, boolean p_230378_10_) {
        return false;
    }

    @Override
    public IJigsawDeserializer<?> getType() {
        return null;
    }

    @Override
    public int getGroundLevelDelta() {
        return -5;
    }

}
