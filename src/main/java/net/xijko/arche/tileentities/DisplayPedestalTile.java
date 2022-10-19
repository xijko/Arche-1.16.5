package net.xijko.arche.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.block.DisplayPedestalBlock;
import net.xijko.arche.block.MuseumCatalogBlock;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.item.ArcheArtifactList;
import net.xijko.arche.item.ModItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class DisplayPedestalTile extends TileEntity {
    public final ItemStackHandler itemHandler = createHandler(this.museum_owned);
    public final ItemStackHandler outputItemHandler = createOutputHandler(this.museum_owned);
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final Random random = new Random();
    public ItemStack cachedItem;

    public int museumSlot = 0;
    public int museumX = 0;
    public int museumY = 0;
    public int museumZ = 0;

    public boolean museum_owned;
    public boolean museum_completed = false;
    public boolean museum_paired;

    public DisplayPedestalTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DisplayPedestalTile() {
        this(ModTileEntities.DISPLAY_PEDESTAL_TILE.get());
    }

    /*@Override
    protected void itemHandler.onContentsChanged(int slot) {

        if (slot==4){
            super(this.)
        }

    }*/

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    /*
      Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
      many blocks change at once.
     */
     //This compound comes back to you clientside in {@link handleUpdateTag}
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        deserializeMuseumSlot(nbt);
        deserializeMuseumOwned(nbt);
        deserializeMuseumPaired(nbt);
        deserializeMuseumPos(nbt);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        setMuseumPaired();
        compound.put("inv", itemHandler.serializeNBT());
        compound.putInt("museumslot",this.museumSlot);
        compound.putBoolean("museumowned",this.museum_owned);
        compound.putBoolean("museumpaired",this.museum_paired);
        compound.putInt("museumx",this.museumX);
        compound.putInt("museumy",this.museumY);
        compound.putInt("museumz",this.museumZ);
        return super.write(compound);
    }

    /*
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public ItemStack getCachedEntity() {
        if (this.cachedItem == null) {
            this.cachedItem = EntityType.loadEntityAndExecute(this.spawnData.getNbt(), this.getWorld(), Function.identity());
            if (this.itemHandler.getStackInSlot(0).getItem() != Blocks.AIR.asItem() && this.itemHandler.serializeNBT().contains("inv", 8) && this.cachedEntity instanceof MobEntity) {
            }
        }

        return this.cachedItem;
    }
*/

    public ItemStack getItem(){
        //TileEntity.readTileEntity(this.getBlockState(),);
        ItemStack renderItemStack = this.itemHandler.getStackInSlot(0);
        this.validate();
        LazyOptional<IItemHandler> handler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).cast();
        if(!handler.isPresent()){
            createHandler(this.museum_owned);
        }
        if(this.museum_paired){
            BlockPos museumCatalogPairedPos = this.getMuseumCatalogPos();
            if(this.getWorld().getTileEntity(museumCatalogPairedPos) instanceof MuseumCatalogTile){
                MuseumCatalogTile museumCatalogTile = (MuseumCatalogTile) this.getWorld().getTileEntity(museumCatalogPairedPos);
                boolean renderCatalogItem = museumCatalogTile.artifactCompletion[this.museumSlot];
                if(renderCatalogItem){
                    renderItemStack = new ItemStack(Arche.ARTIFACT_ITEM_LISTS[this.museumSlot],1);
                }
            }
        }
        return renderItemStack;
    }


    public void setItem(ItemStack item, int slot){
        this.itemHandler.setStackInSlot(slot, item);
    }

    private ItemStackHandler createHandler(boolean isOwned) {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return super.getStackInSlot(slot);
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

    private ItemStackHandler createOutputHandler(boolean isOwned) {
        return new ItemStackHandler(1) {

            //protected boolean canInsert = true;

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

    @Override
    public void onLoad() {
        super.onLoad();
        LOGGER.warn("Result of Pair: "+ this.museum_paired);
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

    public BlockPos getMuseumCatalogPos(){
        int X = this.museumX;
        int Y = this.museumY;
        int Z = this.museumZ;
        BlockPos catalogTestPos = new BlockPos(X,Y,Z);
        return catalogTestPos;
    }

    public boolean checkForMuseumCatalog(){
        assert world != null;
        if(!this.museum_owned) return false;
        BlockState catalogBlockState = world.getBlockState(getMuseumCatalogPos());
        boolean foundCatalog = catalogBlockState.getBlock() instanceof MuseumCatalogBlock;
        LOGGER.warn("Catalog Located: "+foundCatalog);
        return foundCatalog;
    }

    public void setMuseumPaired(){
        if (this.museum_owned && !this.museum_paired)
        this.museum_paired = checkForMuseumCatalog();
    }

    public void setMuseumOwned(){
        this.museum_owned = true;
    }

    public void deserializeMuseumSlot(CompoundNBT compound){
        this.museumSlot = compound.getInt("museumslot");
    }

    public void deserializeMuseumOwned(CompoundNBT compound){
        this.museum_owned = compound.getBoolean("museumowned");
    }

    public void deserializeMuseumPaired(CompoundNBT compound){
        this.museum_paired = compound.getBoolean("museumpaired");
    }

    public void deserializeMuseumPos(CompoundNBT compound){
        this.museumX = compound.getInt("museumx");
        this.museumY = compound.getInt("museumy");
        this.museumZ = compound.getInt("museumz");
    }

    public CompoundNBT serializeMuseumSlot(){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("museumslot",this.museumSlot);
        return nbt;
    }

}
