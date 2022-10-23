package net.xijko.arche.entity.client;

import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.xijko.arche.Arche;
import net.xijko.arche.block.CandleLanternBlock;
import net.xijko.arche.entity.CandleEntity;
import net.xijko.arche.tileentities.CandleLanternTile;
import org.apache.commons.lang3.arch.Processor;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;
import static net.xijko.arche.block.CandleLanternBlock.LIT;

public class CandleLanternModel extends AnimatedGeoModel<CandleLanternTile> {

    @Override
    public ResourceLocation getModelLocation(CandleLanternTile object) {
        //return new ResourceLocation(Arche.MOD_ID, "geo/candle_lantern"+getDirectionForAnimations(object)+".geo.json");
        if(object.getBlockState().get(LIT)){
            return new ResourceLocation(Arche.MOD_ID, "geo/candle_lantern.geo.json");
        }else{
            return new ResourceLocation(Arche.MOD_ID, "geo/candle_lantern_empty.geo.json");
        }

    }

    @Override
    public ResourceLocation getTextureLocation(CandleLanternTile object) {
        return new ResourceLocation(Arche.MOD_ID, "textures/block/candle_lantern.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CandleLanternTile animatable) {
        return new ResourceLocation(Arche.MOD_ID, "animations/candle_lantern"+ getDirectionForAnimations(animatable) +".animation.json");
    }

    private String getDirectionForAnimations(CandleLanternTile tile){
        Direction dir = tile.getBlockState().get(HORIZONTAL_FACING);
        String dirString;
        String hangString;

        switch (dir) {
            case NORTH:
                dirString = "_n";
            /*case SOUTH:
                dirString = "_s";*/
            case WEST:
                dirString = "_w";
            /*(case EAST:
                dirString = "_e";*/
            default:
                dirString = "_n";
        }
        return dirString;
    }

}
