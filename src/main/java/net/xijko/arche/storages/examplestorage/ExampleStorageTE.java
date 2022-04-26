package net.xijko.arche.storages.examplestorage;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.inits.TileEntityInit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExampleStorageTE extends TileEntity {

    private final LazyOptional<IItemHandlerModifiable> inventory = LazyOptional.of(this::createInventory);

    public ExampleStorageTE() {
        super(TileEntityInit.STORAGE_TE.get());
    }


    public IItemHandlerModifiable getInventory(){ //when getInventory method for ExampleStorageTE is called, return the inventory or complain
        return inventory.orElseThrow(()-> new IllegalStateException("Inventory not initialized correctly"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (Direction.Plane.HORIZONTAL.test(side) && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return inventory.cast();
        }else{
            return super.getCapability(cap,side);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compoundNBT) {
        super.write(compoundNBT);
        compoundNBT.put("inventory",((ItemStackHandler)getInventory()).serializeNBT());
        return compoundNBT;
    }

    @Override
    public void read(BlockState state, CompoundNBT compoundNBT) {
        super.read(state, compoundNBT);
        ((ItemStackHandler) getInventory()).deserializeNBT((CompoundNBT) compoundNBT.get("inventory"));
        //this.setChanged() doesn't exist anymore - what to use instead? Is it necessary?
    }

    @Nonnull
    public IItemHandlerModifiable createInventory(){
        return new ItemStackHandler(12){
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot < 6){
                    return stack.getItem() == Items.BONE_MEAL; //restrict slots to specific items
                }else{
                    return false;
                }
            }
        };
    }
}
