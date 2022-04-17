package net.xijko.arche.storages.examplestorage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.inits.ContainerTypeInit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExampleStorageContainer extends Container {

    public static final TranslationTextComponent TITLE = new TranslationTextComponent("container.examplestorage");

    protected ExampleStorageContainer(int id, IItemHandlerModifiable playerInv, IItemHandlerModifiable storageInv, PlayerEntity player, BlockPos pos) {
        super(ContainerTypeInit.EXAMPLE_STORAGE_CONTAINER.get() ,id);
        worldPosCallable = IWorldPosCallable.of(player.world,pos);



        //allows you to see player inventory on screen
        for(int i=0;i<3;++i){
            for(int j=0;j<9;++j){
                this.addSlot(new SlotItemHandler(playerInv,j+i*9+9, 8+j*18,84+i*18));
            }
        }

        //allows you to see player hotbar
        for (int k=0;k<9;++k){
            this.addSlot(new SlotItemHandler(playerInv,k,8+k*18,142));
        }

        //container slot display
        for(int i=0;i<9;++i){
            this.addSlot(new BoneMealSlot(storageInv,i,9+(i/3)*18,18+i%3*18));
        }
        for(int k=0;k<3;++k){
            this.addSlot(new SlotItemHandler(storageInv,k+9,151,18+k*18));
        }
    }

    //a slot dedicated to bonemeal
    public static class BoneMealSlot extends SlotItemHandler{

        public BoneMealSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.getItem() == Items.BONE_MEAL; //allows you to place bonemeal in this bonemeal slot
        }
    }

    private final IWorldPosCallable worldPosCallable;

    public static ExampleStorageContainer getClientContainer(int id, PlayerInventory playerInv, PacketBuffer buffer){
        return new ExampleStorageContainer(id, new InvWrapper(playerInv), new ItemStackHandler(12), playerInv.player, BlockPos.ZERO);
    }

    public static IContainerProvider getServerContainerProvider(ExampleStorageTE te, BlockPos activationPos){
        return (id,playerInv, serverPlayer)-> new ExampleStorageContainer(id,new InvWrapper(playerInv), te.getInventory(), serverPlayer, activationPos);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true; //this is different from the tutorial - see 19:08
    }


    //this method allows you to shift click items into the container, fast
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

        return ItemStack.EMPTY;
        //this is different from the tutorial - see 27:38
        //if it can be moved
        /*if(index <35){
            putStackInSlot(index,this.getSlot(index).getStack());
            return this.getSlot(index).getStack();
        //otherwise put it back
        }else{
            //putStackInSlot(index,this.getSlot(index).getStack());
            putStackInSlot(this.getSlot(index).slotNumber,this.getSlot(index).getStack());
            this.getSlot(index).getStack();
        }*/
    }


}
