package net.xijko.arche.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.xijko.arche.storages.toolbelt.ToolBeltCapabilityProvider;
import net.xijko.arche.storages.toolbelt.ToolBeltContainer;
import net.xijko.arche.storages.toolbelt.ToolBeltItemStackHandler;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ToolBeltItem extends Item implements ICurio {

        public ToolBeltItem() {
            super(new Item.Properties().maxStackSize(1).group(ModItemGroup.ARCHE_GROUP) // the item will appear on the Miscellaneous tab in creative
            );
        }

        /**
         * When the player right clicks while holding the bag, open the inventory screen
         * @param world
         * @param player
         * @param hand
         * @return the new itemstack
         */
        @Nonnull
        @Override
        public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
            ItemStack stack = player.getHeldItem(hand);
            if (!world.isRemote) {  // server only!
                INamedContainerProvider containerProviderToolBelt = new ContainerProviderToolBelt(this, stack);
                final int NUMBER_OF_SLOTS = 12;
                Minecraft.getInstance().player.sendChatMessage("Item has display name of: "+containerProviderToolBelt.getDisplayName());
                NetworkHooks.openGui((ServerPlayerEntity) player,
                        containerProviderToolBelt,
                        (packetBuffer)->{packetBuffer.writeInt(12);});

                // We use the packetBuffer to send the bag size; not necessary since it's always 16, but just for illustration purposes
            }
            return ActionResult.resultSuccess(stack);
        }

        /**
         *  If we use the item on a block with an ITEM_HANDLER_CAPABILITY, automatically transfer the entire contents of the flower bag
         *     into that block
         *  onItemUseFirst is a forge extension that is called before the block is activated
         *  If you use onItemUse, this will never get called for a container because the container will capture the click first
         * @param ctx
         * @return
         */

        @Nonnull
        @Override
        public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext ctx) {
            World world = ctx.getWorld();
            if (world.isRemote()) return ActionResultType.PASS;

            BlockPos pos = ctx.getPos();
            Direction side = ctx.getFace();
            ItemStack itemStack = ctx.getItem();
            if (!(itemStack.getItem() instanceof ToolBeltItem)) throw new AssertionError("Unexpected ToolBeltItem type");
            ToolBeltItem itemToolBelt = (ToolBeltItem)itemStack.getItem();

            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity == null) return ActionResultType.PASS;
            if (world.isRemote()) return ActionResultType.SUCCESS; // always succeed on client side

            // check if this object has an inventory- either Forge capability, or vanilla IInventory
            IItemHandler tileInventory;
            LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            if (capability.isPresent()) {
                tileInventory = capability.orElseThrow(AssertionError::new);
            } else if (tileEntity instanceof IInventory) {
                tileInventory = new InvWrapper((IInventory)tileEntity);
            } else {
                return ActionResultType.FAIL;
            }

            // go through each flower ItemStack in our flower bag and try to insert as many as possible into the tile's inventory.
            ToolBeltItemStackHandler itemStackHandlerFlowerBag =  itemToolBelt.getItemStackHandlerToolBelt(itemStack);
            for (int i = 0; i < itemStackHandlerFlowerBag.getSlots(); i++) {
                ItemStack tool = itemStackHandlerFlowerBag.getStackInSlot(i);
                ItemStack rejectedTools = ItemHandlerHelper.insertItemStacked(tileInventory, tool, false);
                itemStackHandlerFlowerBag.setStackInSlot(i, rejectedTools);
            }
            tileEntity.markDirty();           // make sure that the tileEntity knows we have changed its contents

            // we need to mark the flowerbag ItemStack as dirty so that the server will send it to the player.
            // This normally happens in ServerPlayerEntity.tick(), which calls this.openContainer.detectAndSendChanges();
            // Unfortunately, this code only detects changes to item type, number, or nbt.  It doesn't check the capability instance.
            // We could copy the detectAndSendChanges code out and call it manually, but it's easier to mark the itemstack as
            //  dirty by modifying its nbt...
            //  Of course, if your ItemStack's capability doesn't affect the rendering of the ItemStack, i.e. the Capability is not needed
            //  on the client at all, then you don't need to bother to mark it dirty.

            CompoundNBT nbt = itemStack.getOrCreateTag();
            int dirtyCounter = nbt.getInt("dirtyCounter");
            nbt.putInt("dirtyCounter", dirtyCounter + 1);
            itemStack.setTag(nbt);

            return ActionResultType.SUCCESS;
        }

        // ------  Code used to generate a suitable Container for the contents of the FlowerBag

        /**
         * Uses an inner class as an INamedContainerProvider.  This does two things:
         *   1) Provides a name used when displaying the container, and
         *   2) Creates an instance of container on the server which is linked to the ItemFlowerBag
         * You could use SimpleNamedContainerProvider with a lambda instead, but I find this method easier to understand
         * I've used a static inner class instead of a non-static inner class for the same reason
         */
        private static class ContainerProviderToolBelt implements INamedContainerProvider {
            public ContainerProviderToolBelt(ToolBeltItem itemToolBelt, ItemStack itemStackToolBelt) {
                this.itemStackToolBelt = itemStackToolBelt;
                this.itemToolBelt = itemToolBelt;
            }

            @Override
            public ITextComponent getDisplayName() {
                return itemStackToolBelt.getDisplayName();
            }

            /**
             * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
             */
            @Override
            public ToolBeltContainer createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                ToolBeltContainer container = ToolBeltContainer.createContainerServerSide(windowID, playerInventory, itemToolBelt.getItemStackHandlerToolBelt(itemStackToolBelt), itemStackToolBelt);
                Minecraft.getInstance().player.sendChatMessage("Item stack is: "+container.getType());
                return container;
                //Minecraft.getInstance().player.sendChatMessage("Item stack is: "+ ForgeRegistries.CONTAINERS.getKey().getPath());


            }

            private ToolBeltItem itemToolBelt;
            private ItemStack itemStackToolBelt;
        }

        // ---------------- Code related to Capabilities
        //

        // The CapabilityProvider returned from this method is used to specify which capabilities the ItemFlowerBag possesses
        @Nonnull
        @Override
        public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {

            return new ToolBeltCapabilityProvider();
        }

        /*private ItemStackHandler createHandler() {
            return new ItemStackHandler(2) {
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
                    if (!isItemValid(slot, stack)) {
                        return stack;
                    }

                    return super.insertItem(slot, stack, simulate);
                }
            };
        }*/
        private static ToolBeltItemStackHandler getItemStackHandlerToolBelt(ItemStack itemStack) {
            IItemHandler toolBelt = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
            if (toolBelt == null || !(toolBelt instanceof ToolBeltItemStackHandler)) {
                //LOGGER.error("ItemFlowerBag did not have the expected ITEM_HANDLER_CAPABILITY");
                return new ToolBeltItemStackHandler(1);
            }
            return (ToolBeltItemStackHandler)toolBelt;
        }

        private final String BASE_NBT_TAG = "base";
        private final String CAPABILITY_NBT_TAG = "cap";

        /**
         * Ensure that our capability is sent to the client when transmitted over the network.
         * Not needed if you don't need the capability information on the client
         *
         * Note that this will sometimes be applied multiple times, the following MUST
         * be supported:
         *   Item item = stack.getItem();
         *   NBTTagCompound nbtShare1 = item.getShareTag(stack);
         *   stack.readShareTag(nbtShare1);
         *   NBTTagCompound nbtShare2 = item.getShareTag(stack);
         *   assert nbtShare1.equals(nbtShare2);
         *
         * @param stack The stack to send the NBT tag for
         * @return The NBT tag
         */
        @Nullable
        @Override
        public CompoundNBT getShareTag(ItemStack stack) {
            CompoundNBT baseTag = stack.getTag();
            ToolBeltItemStackHandler itemStackHandlerFlowerBag = getItemStackHandlerToolBelt(stack);
            CompoundNBT capabilityTag = itemStackHandlerFlowerBag.serializeNBT();
            CompoundNBT combinedTag = new CompoundNBT();
            if (baseTag != null) {
                combinedTag.put(BASE_NBT_TAG, baseTag);
            }
            if (capabilityTag != null) {
                combinedTag.put(CAPABILITY_NBT_TAG, capabilityTag);
            }
            return combinedTag;
        }

        /** Retrieve our capability information from the transmitted NBT information
         *
         * @param stack The stack that received NBT
         * @param nbt   Received NBT, can be null
         */
        @Override
        public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
            if (nbt == null) {
                stack.setTag(null);
                return;
            }
            CompoundNBT baseTag = nbt.getCompound(BASE_NBT_TAG);              // empty if not found
            CompoundNBT capabilityTag = nbt.getCompound(CAPABILITY_NBT_TAG); // empty if not found
            stack.setTag(baseTag);
            ToolBeltItemStackHandler itemStackHandlerFlowerBag = getItemStackHandlerToolBelt(stack);
            itemStackHandlerFlowerBag.deserializeNBT(capabilityTag);
        }
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack) {
        ICurio.super.onEquip(slotContext, prevStack);
        Minecraft.getInstance().player.sendChatMessage("Equipped!");
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack) {
        ICurio.super.onUnequip(slotContext, newStack);
        Minecraft.getInstance().player.sendChatMessage("Unequipped!");
    }

}

