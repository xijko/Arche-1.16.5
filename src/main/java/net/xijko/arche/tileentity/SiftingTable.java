package net.xijko.arche.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class SiftingTable extends TileEntity {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public SiftingTable(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public SiftingTable(){
        this(ModTileEntities.SIFTING_TABLE_TILE.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv",itemHandler.serializeNBT());
        return super.write(compound);
    }

    private ItemStackHandler createHandler(){
        return new ItemStackHandler(2){
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot){
                    case 0: return stack.getItem() == ModItems.DIRT_DEBRIS.get();
                    default:
                        return false;
                }
            }
            @Override
            public int getSlotLimit(int slot){
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
                if(!isItemValid(slot, stack)){
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    public void debrisIsProcessed(){
        boolean hasValidDebris = this.itemHandler.getStackInSlot(0).getCount() > 0;

        if (hasValidDebris){
            this.itemHandler.getStackInSlot(0).shrink(1);
            this.itemHandler.insertItem(1,new ItemStack(Items.PUMPKIN),false);
        }

    }

}
