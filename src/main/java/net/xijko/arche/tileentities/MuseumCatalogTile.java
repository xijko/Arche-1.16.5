package net.xijko.arche.tileentities;

import com.google.common.collect.Collections2;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.block.DisplayPedestalBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static net.xijko.arche.block.DisplayPedestalBlock.MUSEUM_SLOT;
import static net.xijko.arche.block.MuseumCatalogBlock.MUSEUM_OWNED;

public class MuseumCatalogTile extends TileEntity {
    public final ItemStackHandler itemHandler = createHandler();
    public final ItemStackHandler outputItemHandler = createOutputHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final Random random = new Random();
    public ItemStack cachedItem;
    public int artifactCount = 32;
    public int[] xArray = new int[artifactCount];
    public int[] yArray = new int[artifactCount];
    public int[] zArray = new int[artifactCount];
    public int[] pedestalArray = new int[artifactCount];

    public MuseumCatalogTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public MuseumCatalogTile() {
        this(ModTileEntities.MUSEUM_CATALOG_TILE.get());
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
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.write(compound);
    }

    public CompoundNBT pedestalWrite(CompoundNBT compound) {
        compound.put("inv", serializePedestalsNBT());
        return super.write(compound);
    }

    public void pedestalRead(BlockState state, CompoundNBT nbt) {
        deserializePedestalNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }


    public CompoundNBT serializePedestalsNBT()
    {
        findPedestals();
        ListNBT nbtTagList = new ListNBT();
        int pedestalCount = pedestalArray.length;
        for (int i = 0; i < pedestalCount; i++)
        {
            if (pedestalArray[i] >= 0)
            {
                CompoundNBT pedestalTag = new CompoundNBT();
                pedestalTag.putInt("Slot", i);
                pedestalTag.putInt("xPos", xArray[i]);
                pedestalTag.putInt("yPos", yArray[i]);
                pedestalTag.putInt("zPos", zArray[i]);
                nbtTagList.add(pedestalTag);
            }
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Pedestals", nbtTagList);
        nbt.putInt("Size", pedestalCount);
        LOGGER.warn(nbt.toString());
        return nbt;
    }

    public void deserializePedestalNBT(CompoundNBT nbt)
    {
        pedestalArray = new int[nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : pedestalArray.length];
        ListNBT tagList = nbt.getList("Pedestals", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            int xPos = itemTags.getInt("xPos");
            int yPos = itemTags.getInt("yPos");
            int zPos = itemTags.getInt("zPos");

            if (slot >= 0 && slot < pedestalArray.length)
            {
                pedestalArray[i] = slot;
                xArray[i] = xPos;
                yArray[i] = yPos;
                zArray[i] = zPos;
            }
        }
        onLoad();
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
        this.validate();
        LazyOptional<IItemHandler> handler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).cast();
        if(!handler.isPresent()){
            createHandler();
        }
        return this.itemHandler.getStackInSlot(0);
    }

    public int getArtifactCount(){
        return artifactCount;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(9 * Math.round(getArtifactCount()/9)) {

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

    @Override
    public void onLoad() {
        super.onLoad();
        serializePedestalsNBT();
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

    @SuppressWarnings("unchecked")
    private void findPedestals(){
        /*BlockPos center = this.pos;
        BlockPos boxCornerA =   new BlockPos(
                        center.getX()+radius,
                        center.getY()+radius,
                        center.getZ()+radius
                                );
        BlockPos boxCornerB =   new BlockPos(
                        center.getX()-radius,
                        center.getY()-radius,
                        center.getZ()-radius
                                );
                                *
         */
        assert world != null;
        assert !world.isRemote();
        //List<TileEntity> allTEs = world.loadedTileEntityList;
        world.loadedTileEntityList.stream()
                .filter(te -> te instanceof DisplayPedestalTile)
                /*.filter(te ->
                        ((DisplayPedestalBlock) te.getBlockState().getBlock()).isMuseumOwned(te.getBlockState())
                            &&
                        !((DisplayPedestalBlock) te.getBlockState().getBlock()).isMuseumPaired(te.getBlockState())
                )*/
                .forEach(te -> {
                    BlockState tebs = te.getBlockState();
                    BlockPos pos = te.getPos();
                    DisplayPedestalBlock b = (DisplayPedestalBlock) tebs.getBlock();
                    BlockPos thisPos = this.pos;
                    int i = tebs.get(MUSEUM_SLOT);;
                    boolean proceed = tebs.get(MUSEUM_OWNED);
                    LOGGER.warn(proceed);
                    if(xArray[i]!=0 && xArray[i]!=0 && xArray[i]!=0 && proceed){
                        BlockPos existingPos = new BlockPos(xArray[i],yArray[i],zArray[i]);
                        if(thisPos.distanceSq(existingPos) <= thisPos.distanceSq(pos)) proceed = false;
                        LOGGER.warn("Will not proceed! Closer pedestal found at " + existingPos);
                    }
                    if(proceed) {
                        xArray[i] = pos.getX();
                        yArray[i] = pos.getY();
                        zArray[i] = pos.getZ();
                        pedestalArray[i] = i;

                        int xDist = thisPos.getX() - pos.getX();
                        int yDist = thisPos.getY() - pos.getY();
                        int zDist = thisPos.getZ() - pos.getZ();

                        if (xDist>16 || yDist>16 ||zDist>16){
                            proceed=false;
                            LOGGER.warn("Will not proceed! Pedestal too far: " + pos);
                        }else {
                            ((DisplayPedestalTile) te).museum_paired = true;
                            ((DisplayPedestalTile) te).museumX = xDist;
                            ((DisplayPedestalTile) te).museumY = yDist;
                            ((DisplayPedestalTile) te).museumZ = zDist;
                            LOGGER.warn("Set position in array: " + i);
                        }
                    }

                });
        LOGGER.warn(world.loadedTileEntityList.stream()
                .filter(te -> te instanceof DisplayPedestalTile).toString());
        LOGGER.warn("xArray: "+ Arrays.toString(xArray));
        LOGGER.warn("yArray: "+ Arrays.toString(yArray));
        LOGGER.warn("zArray: "+ Arrays.toString(zArray));
    }

}
