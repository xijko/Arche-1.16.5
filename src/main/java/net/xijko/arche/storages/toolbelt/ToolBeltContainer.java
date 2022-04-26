package net.xijko.arche.storages.toolbelt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.xijko.arche.Arche;
import net.xijko.arche.container.ModContainers;
import net.xijko.arche.item.ToolBeltItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.tools.Tool;
import java.util.List;


public class ToolBeltContainer extends Container{
    private static final Logger LOGGER = LogManager.getLogger();


    private static IItemHandler playerInventory;

    /**
     * Creates the container to be used on the server side
     * @param windowID
     * @param playerInventory
     * @param bagContents
     * @param toolBelt the ItemStack for the flower bag; this is used for checking whether the player is still holding the bag in their hand
     * @return
     */
    public static ToolBeltContainer createContainerServerSide(int windowID, PlayerInventory playerInventory, ToolBeltItemStackHandler bagContents,
                                                              ItemStack toolBelt) {
        return new ToolBeltContainer(windowID, playerInventory, bagContents, toolBelt);
    }

    /**
     * Creates the container to be used on the client side  (contains dummy data)
     * @param windowID
     * @param playerInventory
     * @param extraData extra data sent from the server
     * @return
     */

    public ToolBeltContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        int numberOfSlots = extraData.readInt();

        try {
            ToolBeltItemStackHandler itemStackHandlerFlowerBag = new ToolBeltItemStackHandler(numberOfSlots);
            layoutPlayerInventorySlots(8, 86);
            // on the client side there is no parent ItemStack to communicate with - we use a dummy inventory
            return new ToolBeltContainer(windowID, playerInventory, itemStackHandlerFlowerBag, ItemStack.EMPTY);
        } catch (IllegalArgumentException iae) {
            //LOGGER.(iae);
        }
        return null;
    }


    private final ToolBeltItemStackHandler itemStackHandlerToolBelt;
    private final ItemStack itemStackBeingHeld;

    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 51 = TileInventory slots, which map to our bag slot numbers 0 - 15)

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int BAG_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int MAX_EXPECTED_BAG_SLOT_COUNT = 12;

    public static final int BAG_INVENTORY_YPOS = 36;  // the ContainerScreenFlowerBag needs to know these so it can tell where to draw the Titles
    public static final int PLAYER_INVENTORY_YPOS = 84;

    /**
     * Creates a container suitable for server side or client side
     * @param windowId ID of the container
     * @param playerInv the inventory of the player
     * @param itemStackToolBelt the inventory stored in the bag
     */
    public ToolBeltContainer(int windowId, PlayerInventory playerInv,
                             ToolBeltItemStackHandler itemStackToolBelt,
                             ItemStack itemStackBeingHeld) {
        super(ModContainers.TOOL_BELT_CONTAINER.get(), windowId);
        this.itemStackHandlerToolBelt = itemStackToolBelt;
        this.itemStackBeingHeld = itemStackBeingHeld;
        this.playerInventory = new InvWrapper(playerInv);
        LOGGER.warn(this.playerInventory);
        /*
        if(this.itemStackBeingHeld != null) {
            this.itemStackBeingHeld.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                //addSlot(new SlotItemHandler(h, 0, 80, 31));
                //addSlot(new SlotItemHandler(h, 1, 80, 53));
            });
        }
        */

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int HOTBAR_XPOS = 8;
        final int HOTBAR_YPOS = 142;
        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            int slotNumber = x;
            addSlot(new Slot(playerInv, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        final int PLAYER_INVENTORY_XPOS = 8;
        // Add the rest of the player's inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new Slot(playerInv, slotNumber, xpos, ypos));
            }
        }

        int bagSlotCount = itemStackHandlerToolBelt.getSlots();
        if (bagSlotCount < 1 || bagSlotCount > MAX_EXPECTED_BAG_SLOT_COUNT) {
            //LOGGER.("Unexpected invalid slot count in ItemStackHandlerFlowerBag(" + bagSlotCount + ")");
            bagSlotCount = MathHelper.clamp(bagSlotCount, 1, MAX_EXPECTED_BAG_SLOT_COUNT);
        }

        final int MAIN_TOOL_SLOTS = 6;
        final int EXTRA_ITEM_SLOTS = 6;
        final int BAG_SLOTS_PER_ROW = 8;
        final int BAG_INVENTORY_XPOS = 8;
        // Add the tile inventory container to the gui
        for (int bagSlot = 0; bagSlot < bagSlotCount; ++bagSlot) {
            //add slots for pouch
            int slotNumber = bagSlot;
            int toolRowSlotPos = BAG_INVENTORY_XPOS + SLOT_X_SPACING *bagSlot;
            int bagRow = bagSlot / BAG_SLOTS_PER_ROW;
            int bagCol = bagSlot % BAG_SLOTS_PER_ROW;
            int toolxpos = BAG_INVENTORY_XPOS;
            int toolypos = BAG_INVENTORY_YPOS; //starts in middle row
            int pouchxpos = BAG_INVENTORY_XPOS + MAIN_TOOL_SLOTS*SLOT_X_SPACING;
            int pouchypos = BAG_INVENTORY_YPOS;
            int xpos=0;
            int ypos=toolypos;
            //add slots for tools
            if(bagSlot<MAIN_TOOL_SLOTS){
                xpos = toolxpos +1 + SLOT_X_SPACING * bagCol;
                addSlot(new SlotItemHandler(itemStackHandlerToolBelt, slotNumber, xpos, ypos));
                //ypos is unchanged
            }else{
                pouchxpos += bagSlot%2 * SLOT_X_SPACING + SLOT_X_SPACING;
                pouchypos += bagSlot%3  * SLOT_Y_SPACING - SLOT_Y_SPACING;
                addSlot(new SlotItemHandler(itemStackHandlerToolBelt, slotNumber, pouchxpos, pouchypos));
            }
        }

    }

    // Check if the player is still able to access the container
    // In this case - if the player stops holding the bag, return false
    // Called on the server side only.
    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {

        ItemStack main = player.getHeldItemMainhand();
        ItemStack off = player.getHeldItemOffhand();
        if(!main.isEmpty() && main == itemStackBeingHeld){
            return true;
        }
        if(!off.isEmpty() && off == itemStackBeingHeld){
            return true;
        }
        List<SlotResult> beltCurios = CuriosApi.getCuriosHelper().findCurios(player, "belt");
        for (SlotResult slot : beltCurios) {
            ItemStack slotStack = slot.getStack();
            Item slotItem = slotStack.getItem();
            if (slotItem instanceof ToolBeltItem && !player.world.isRemote) {
                return true;
            }
        };
        return false;
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

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = inventorySlots.get(index);
        if (sourceSlot == null || !sourceSlot.getHasStack() || sourceSlot.getStack().getItem() instanceof ToolBeltItem) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!mergeItemStack(sourceStack, BAG_INVENTORY_FIRST_SLOT_INDEX, BAG_INVENTORY_FIRST_SLOT_INDEX
                    + MAX_EXPECTED_BAG_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < BAG_INVENTORY_FIRST_SLOT_INDEX + MAX_EXPECTED_BAG_SLOT_COUNT) {
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
        sourceSlot.onTake(playerIn.inventory.player, sourceStack);
        return copyOfSourceStack;
    }

    /**
     * Because capability nbt is not actually stored in the ItemStack nbt (it is created fresh each time we need to transmit or save an nbt), detectAndSendChanges
     *   does not work for our ItemFlowerBag ItemStack.  i.e. when the contents of ItemStackHandlerFlowerBag are changed, the nbt of ItemFlowerBag ItemStack don't change,
     *   so it is not sent to the client.
     * For this reason, we need to manually detect when it has changed and mark it dirty.
     * The easiest way is just to set a counter in the nbt tag and let the vanilla code notice that the itemstack has changed.
     * The side effect is that the player's hand moves down and up (because the client thinks it is a new ItemStack) but that's not objectionable.
     * Alternatively you could copy the code from vanilla detectAndSendChanges and tweak it to find the slot for itemStackBeingHeld and send it manually.
     *
     * Of course, if your ItemStack's capability doesn't affect the rendering of the ItemStack, i.e. the Capability is not needed on the client at all, then
     *   you don't need to bother with marking it dirty.
     */
   /* @Override
    public void detectAndSendChanges() {
        if (itemStackHandlerToolBelt.isDirty()) {
            CompoundNBT nbt = itemStackBeingHeld.getOrCreateTag();
            int dirtyCounter = nbt.getInt("dirtyCounter");
            nbt.putInt("dirtyCounter", dirtyCounter + 1);
            itemStackBeingHeld.setTag(nbt);
        }
        super.detectAndSendChanges();
    }*/

}
