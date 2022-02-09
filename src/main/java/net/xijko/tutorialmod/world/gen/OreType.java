package net.xijko.tutorialmod.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraftforge.common.util.Lazy;
import net.xijko.tutorialmod.block.ModBlocks;

public enum OreType {

    POOP_DEBRIS(Lazy.of(ModBlocks.POOP_DEPOSIT),8, 50,248);//enum with , ended with ;

    private final Lazy<OreBlock> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;


    OreType(Lazy<OreBlock> block, int maxVeinSize, int minHeight, int maxHeight) {
        this.block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    //change to getBlock?
    public Lazy<OreBlock> getOreBlock() {
        return block;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public static OreType get(Block block){
        for (OreType ore : values()){
            if(block == ore.block){
                return ore;
            }
        }
        return null;
    }

}
