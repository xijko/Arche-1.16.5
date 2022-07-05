package net.xijko.arche.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.fml.RegistryObject;
import org.apache.http.config.Registry;

import java.util.List;

public class StructureOrePlacementConfig implements IPlacementConfig {


    public static final Codec<StructureOrePlacementConfig> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
            /*com.mojang.serialization.Codec.STRING.fieldOf("blockTarget").forGetter((config) -> {
            return config.blockTarget;
        }), com.mojang.serialization.Codec.STRING.fieldOf("blockReplace").forGetter((config) -> {
            return config.blockReplace;
        }), */com.mojang.serialization.Codec.INT.fieldOf("bottom_offset").orElse(0).forGetter((config) -> {
            return config.bottomOffset;
        }), com.mojang.serialization.Codec.INT.fieldOf("top_offset").orElse(0).forGetter((config) -> {
            return config.topOffset;
        }), com.mojang.serialization.Codec.INT.fieldOf("maximum").orElse(0).forGetter((config) -> {
            return config.maximum;
        }), com.mojang.serialization.Codec.INT.fieldOf("blockTarget").forGetter((config) -> {
            return config.blockX;
        }), com.mojang.serialization.Codec.INT.fieldOf("blockTarget").forGetter((config) -> {
            return config.blockY;
        }), com.mojang.serialization.Codec.INT.fieldOf("blockTarget").forGetter((config) -> {
            return config.blockZ;
        }), com.mojang.serialization.Codec.INT.fieldOf("blockTarget").forGetter((config) -> {
            return config.radius;
        })).apply(builder, StructureOrePlacementConfig::new);
    });

    //public String blockTarget;
    //public String blockReplace;
    public int bottomOffset;
    public int topOffset;
    public int maximum;
    public int radius;
    public int blockX;
    public int blockY;
    public int blockZ;


    public StructureOrePlacementConfig(
            //String bTarget,
        //String bReplace,
        int bottomOffset, int topOffset, int maximum, int x, int y, int z, int radius) {
        //this.blockTarget = bTarget;
        //this.blockReplace = bReplace;
        this.bottomOffset = bottomOffset;
        this.topOffset = topOffset;
        this.maximum = maximum;
        /*this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
        this.radius = z;*/
    }


    /*
    private String getStructure(){
        return structure;
    }

    private String getBlockTarget(){
        return blockTarget;
    }

    private String getBlockReplace(){
        return blockReplace;
    }

public ConfiguredPlacement<StructureOrePlacementConfig> configure(StructureOrePlacementConfig config) {
        return super.configure(config);
    }

    public int getBottomOffset() {
        return bottomOffset;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getTopOffset() {
        return topOffset;
    }
 */
}
