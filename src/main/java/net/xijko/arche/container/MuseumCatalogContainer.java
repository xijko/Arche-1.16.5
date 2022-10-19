package net.xijko.arche.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.tileentities.MuseumCatalogTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MuseumCatalogContainer extends Container {
    public final TileEntity tileEntity;
    private final PlayerEntity playerEntity;
    private final IItemHandler playerInventory;
    private static final Logger LOGGER = LogManager.getLogger();
    private static int artifactCount = 36;
    private int MAX_ARTIFACT_COUNT = artifactCount;
    public MuseumCatalogItemStackHandler museumCatalogItemStackHandler;

    public MuseumCatalogContainer(int windowId, World world, BlockPos pos,
                                  PlayerInventory playerInventory, PlayerEntity player) {
        super(ModContainers.MUSEUM_CATALOG_CONTAINER.get(), windowId);
        this.tileEntity = world.getTileEntity(pos);
        assert this.tileEntity != null;
        this.artifactCount = ((MuseumCatalogTile) this.tileEntity).getArtifactCount();
        playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(8, 104);

        if(tileEntity != null) {
            ((MuseumCatalogTile) tileEntity).findPedestals();
            ((MuseumCatalogTile) tileEntity).serializePedestalsNBT();
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                //THIS IS THE SLOTS FOR THE INPUTS< WILL LAY OUT
                //addSlot(new SlotItemHandler(h, this.artifactCount, 8, 104));
                addSlot(new SlotItemHandler(h, 0, 8, 81));
                //layoutCatalogInventorySlots(museumCatalogItemStackHandler);
            });
        }

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()),
                playerIn, ModBlocks.MUSEUM_CATALOG.get());
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public static int getArtifactCount(){
        return artifactCount;
    }

    private void layoutCatalogInventorySlots(IItemHandler h){
        int artifactSlotCount = getArtifactCount();
        int slotLimit = TE_INVENTORY_FIRST_SLOT_INDEX + artifactSlotCount;
        if (artifactSlotCount < 1 || artifactSlotCount > MAX_ARTIFACT_COUNT) {
            //LOGGER.("Unexpected invalid slot count in ItemStackHandlerFlowerBag(" + bagSlotCount + ")");
            artifactSlotCount = MathHelper.clamp(artifactSlotCount, 1, MAX_ARTIFACT_COUNT);
        }

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int CATALOG_INVENTORY_YPOS = 0;
        final int CATALOG_INVENTORY_XPOS = 0;
        final int EXTRA_ITEM_SLOTS = 6;
        final int BAG_SLOTS_PER_ROW = 8;
        final int BAG_INVENTORY_XPOS = 8;
        // Add the tile inventory container to the gui
        for (int bagSlot =TE_INVENTORY_FIRST_SLOT_INDEX; bagSlot < slotLimit; ++bagSlot) {
            //add slots for pouch
            int slotNumber = bagSlot;
            int toolRowSlotPos = BAG_INVENTORY_XPOS + SLOT_X_SPACING *bagSlot;
            int bagRow = bagSlot / BAG_SLOTS_PER_ROW;
            int bagCol = bagSlot % BAG_SLOTS_PER_ROW;
            int inventoryxpos = BAG_INVENTORY_XPOS;
            int inventoryypos = CATALOG_INVENTORY_YPOS; //starts in middle row
            int xpos=0;
            int ypos=inventoryypos;
            //add slots for tools
            xpos = inventoryxpos +1 + SLOT_X_SPACING * bagCol;
            LOGGER.warn("Added SLot "+slotNumber+" at "+"xpos:"+xpos+" ypos:"+ypos);
            addSlot(new SlotItemHandler(h, slotNumber, xpos, ypos));
                //ypos is unchanged
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private final int TE_INVENTORY_SLOT_COUNT = 1;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!mergeItemStack(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!mergeItemStack(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }
        sourceSlot.onTake(playerEntity, sourceStack);
        return copyOfSourceStack;
    }

}
