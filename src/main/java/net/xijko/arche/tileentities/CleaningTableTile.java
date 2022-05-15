package net.xijko.arche.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.item.ArcheArtifactBroken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class CleaningTableTile extends TileEntity {
    public final ItemStackHandler itemHandler = createHandler();
    public final ItemStackHandler outputItemHandler = createOutputHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final Random random = new Random();

    public CleaningTableTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public CleaningTableTile() {
        this(ModTileEntities.RESTORE_TABLE_TILE.get());
    }

    /*@Override
    protected void itemHandler.onContentsChanged(int slot) {

        if (slot==4){
            super(this.)
        }

    }*/

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.write(compound);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(5) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
                if (slot==4){

                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        return !(stack.getItem() instanceof ArcheArtifactBroken);
                    case 4: return stack.getItem() instanceof ArcheArtifactBroken;
                    default:
                        return true;
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                if(slot == 4){
                    return 1;
                }else {
                    return 64;
                }
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                    return 1;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            //if(side == Direction.DOWN){return outputHandler.cast();}
                return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public void ejectLootAndXp(World worldIn, PlayerEntity playerIn, ItemStack artifactIn, ItemStack artifactOut){
        //assert Minecraft.getInstance().player != null;
        //Minecraft.getInstance().player.sendChatMessage("Loot: " + lootOutcome);
        ArcheArtifactBroken artifactItem = (ArcheArtifactBroken) artifactIn.getItem();
        int archeTier = artifactItem.archeTier;
        ItemEntity lootItem = new ItemEntity(worldIn,playerIn.getPosX(),playerIn.getPosY()+1,playerIn.getPosZ(),artifactOut);
        if(random.nextInt(100) * archeTier> 75 ){
            int xpGrant = random.nextInt(archeTier)+1;
            ExperienceOrbEntity xpOrb = new ExperienceOrbEntity(worldIn,playerIn.getPosX(),playerIn.getPosY()+2,playerIn.getPosZ(),xpGrant);
            worldIn.addEntity(xpOrb);
        }
        worldIn.addEntity(lootItem);

    }

}
