package net.xijko.arche.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static net.xijko.arche.block.CandleLanternBlock.LIT;

public class CandleLanternTile extends TileEntity implements IAnimatable{
    private final AnimationFactory manager = new AnimationFactory(this);
    public String displayName = "";
    public String uuid = "";

    public CandleLanternTile() {
        super(ModTileEntities.CANDLE_LANTERN_TILE.get());
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

    private void deserializeNameNBT(CompoundNBT nbt){
        if(!nbt.getString("name").equals("")){
            displayName = nbt.getString("name");
        }
    }
    private void deserializeUUIDNBT(CompoundNBT nbt){
        if(!nbt.getString("uuid").equals("")){
            uuid = nbt.getString("uuid");
        }
    }

    public void setDisplayName(String text){
        this.displayName = text;
        this.markDirty();
    }

    public void setUUID(String uuid){
        this.uuid = uuid;
        this.markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        deserializeNameNBT(nbt);
        deserializeUUIDNBT(nbt);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("name", String.valueOf(displayName));
        compound.putString("uuid", String.valueOf(uuid));
        return super.write(compound);
    }
}
