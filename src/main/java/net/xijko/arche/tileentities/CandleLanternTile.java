package net.xijko.arche.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.xijko.arche.block.ArcheDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.block.tile.FertilizerTileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Random;

import static net.xijko.arche.block.ArcheDeposit.GENERATED;
import static net.xijko.arche.block.CandleLanternBlock.LIT;

public class CandleLanternTile extends TileEntity implements IAnimatable{
    private final AnimationFactory manager = new AnimationFactory(this);

    public CandleLanternTile() {
        super(ModTileEntities.CANDLE_LANTERN_TILE.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this,"controller",0,this::predicate));
    }

    private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String litState;
        String litAction;
        if(!this.getBlockState().get(LIT)){
            litAction = "open";
            litState = "opened";
        }else{
            litAction = "close";
            litState = "closed";
        }
        event.getController().setAnimation(new AnimationBuilder().playAndHold("animation.candle_lantern."+litAction).addAnimation("animation.candle_lantern."+litState));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.manager;
    }
}
