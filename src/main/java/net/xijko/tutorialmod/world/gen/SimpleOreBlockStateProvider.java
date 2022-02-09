package net.xijko.tutorialmod.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;

public class SimpleOreBlockStateProvider {
    public BlockState BlockState(OreBlock oreBlock){
        return oreBlock.getDefaultState();

    }
}
