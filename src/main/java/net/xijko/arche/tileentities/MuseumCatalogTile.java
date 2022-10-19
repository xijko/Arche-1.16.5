package net.xijko.arche.tileentities;

import com.google.common.collect.Collections2;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.block.DisplayPedestalBlock;
import net.xijko.arche.container.MuseumCatalogContainer;
import net.xijko.arche.container.MuseumCatalogItemStackHandler;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.MuseumCatalogConsumeMessage;
import net.xijko.arche.network.RestoreTableRestoreMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static net.xijko.arche.block.MuseumCatalogBlock.MUSEUM_OWNED;

public class MuseumCatalogTile extends TileEntity {
    public final ItemStackHandler itemHandler = createHandler();
    public final ItemStackHandler outputItemHandler = createOutputHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> outputItemHandler);
    private static final Logger LOGGER = LogManager.getLogger();
    protected static final Random random = new Random();
    public ItemStack cachedItem;
    public int artifactCount = ModItems.artifactCount;
    public boolean[] artifactCompletion = new boolean[artifactCount];
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
        deserializePedestalNBT(nbt.getCompound("artifacts"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        compound.put("artifacts", serializePedestalsNBT());
        return super.write(compound);
    }

    public CompoundNBT serializePedestalsNBT()
    {
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
        ListNBT nbtArtifactList = new ListNBT();
        for(int i=0; i<artifactCount;i++){
            CompoundNBT catalogTag = new CompoundNBT();
            catalogTag.putInt("Slot",i);
            catalogTag.putBoolean("Complete",artifactCompletion[i]);
            nbtArtifactList.add(catalogTag);
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Artifacts",nbtArtifactList);
        nbt.put("Pedestals", nbtTagList);
        nbt.putInt("Size", pedestalCount);
        LOGGER.warn(nbt.toString());
        return nbt;
    }

    public void deserializePedestalNBT(CompoundNBT nbt)
    {
        pedestalArray = new int[nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : pedestalArray.length];
        ListNBT tagList = nbt.getList("Pedestals", Constants.NBT.TAG_COMPOUND);
        ListNBT artifactList = nbt.getList("Artifacts",Constants.NBT.TAG_COMPOUND);
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
        for (int i = 0; i < artifactList.size(); i++)
        {
            CompoundNBT itemTags = artifactList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            boolean completed = itemTags.getBoolean("Complete");

            if (slot >= 0 && slot < pedestalArray.length)
            {
                artifactCompletion[i] = completed;
            }
        }
        //onLoad();
    }

    public ItemStack getItem(){
        //TileEntity.readTileEntity(this.getBlockState(),);
        this.validate();
        LazyOptional<IItemHandler> handler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).cast();
        if(!handler.isPresent()){
            createHandler();
        }
        return this.itemHandler.getStackInSlot(0);
    }

    public boolean getArtifactValidCheck(ItemStack itemStackIn){
        serializePedestalsNBT();
        Item item = itemStackIn.getItem();
        if(item instanceof ArcheArtifactItem){
            int slot = ((ArcheArtifactItem) item).slot;
            return !artifactCompletion[slot];
        }else{
            return false;
        }
    }

    public ItemStack consumeArtifact(ItemStack itemStackIn){
        if(getArtifactValidCheck(itemStackIn)){
            ArcheArtifactItem item = (ArcheArtifactItem) itemStackIn.getItem();
            int slot = item.slot;
            artifactCompletion[slot]=true;
            serializePedestalsNBT();
        }
        ModNetwork.sendToServer(new MuseumCatalogConsumeMessage(this.getWorld(),this.getPos()));
        itemStackIn.shrink(1);
        LOGGER.warn("Consumed!");
        return ItemStack.EMPTY;
    }

    public int getArtifactCount(){
        return artifactCount;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
                super.setStackInSlot(slot, stack);
                if(slot == 0){
                    consumeArtifact(stack);
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return getArtifactValidCheck(stack);
                    default:
                        return false;
                }
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
                return consumeArtifact(stack);
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
    public void findPedestals(){
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
                    BlockPos pos = te.getPos();
                    BlockPos thisPos = this.pos;
                    int i = ((DisplayPedestalTile) te).museumSlot;
                    boolean proceed = ((DisplayPedestalTile) te).museum_owned;
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

                        if (Math.abs(xDist) >30 || Math.abs(yDist)>30 ||Math.abs(zDist)>30){
                            proceed=false;
                            LOGGER.warn("Will not proceed! Pedestal too far: " + pos);
                        }else {
                            ((DisplayPedestalTile) te).museum_owned = true;
                            ((DisplayPedestalTile) te).museum_paired = true;
                            ((DisplayPedestalTile) te).museumX = thisPos.getX();
                            ((DisplayPedestalTile) te).museumY = thisPos.getY();
                            ((DisplayPedestalTile) te).museumZ = thisPos.getZ();
                            te.markDirty();
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

    public void resetCatalog(World world, BlockPos pos) {
        for (boolean c : this.artifactCompletion
        ) {
            c = false;

        }
        for (int i = 0; i < this.artifactCompletion.length; i++) {
            this.artifactCompletion[i] = false;

            int xPos = this.xArray[i];
            int yPos = this.yArray[i];
            int zPos = this.zArray[i];

            //tile.write(tile.serializePedestalsNBT());
            //LOGGER.warn(tile.serializePedestalsNBT());
            this.markDirty();

            BlockPos pedestalPos = new BlockPos(xPos, yPos, zPos);
            TileEntity pedestalTile = this.getWorld().getTileEntity(pedestalPos);
            if (!(pedestalTile instanceof DisplayPedestalTile)) return;
            ((DisplayPedestalTile) pedestalTile).setItem(ItemStack.EMPTY, 0);
            pedestalTile.markDirty();
        }

        grantReward();
    }
    public static void grantReward(){

    }

}
