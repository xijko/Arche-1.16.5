package net.xijko.arche.storages.toolbelt;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.xijko.arche.item.ToolBeltItem;

import javax.annotation.Nonnull;

public class ToolBeltItemStackHandler extends ItemStackHandler {

        public static final int MIN_SLOTS = 1;
        public static final int MAX_SLOTS = 12;

        public ToolBeltItemStackHandler(int numberOfSlots) {
            super(MathHelper.clamp(numberOfSlots, MIN_SLOTS, MAX_SLOTS));
            if (numberOfSlots < MIN_SLOTS || numberOfSlots > MAX_SLOTS) {
                throw new IllegalArgumentException("Invalid number of flower slots:"+numberOfSlots);
            }
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (slot < 0 || slot >= MAX_SLOTS) {
                throw new IllegalArgumentException("Invalid slot number:"+slot);
            }
            if (stack.isEmpty() || stack.getItem() instanceof ToolBeltItem) return false;
            Item item = stack.getItem();
            switch (slot) {
                case 5:
                case 4:
                case 3:
                case 2:
                case 1:
                case 0:
                    return stack.getItem() instanceof ToolItem;
                default:
                            return true;
            }

        }

        /**Count how many empty slots are in the bag
         * @return the number of empty slots
         */
        public int getNumberOfEmptySlots() {
            final int NUMBER_OF_SLOTS = getSlots();

            int emptySlotCount = 0;
            for (int i = 0; i < NUMBER_OF_SLOTS; ++i) {
                if (getStackInSlot(i) == ItemStack.EMPTY) {
                    ++emptySlotCount;
                }
            }
            return emptySlotCount;
        }

        /** returns true if the contents have changed since the last call.
         * Resets to false after each call.
         * @return true if changed since the last call
         */
        public boolean isDirty() {
            boolean currentState = isDirty;
            isDirty = false;
            return currentState;
        }

        /** Called whenever the contents of the bag have changed.
         *   We need to do this manually in order to make sure that the server sends a synchronisation packet to the client for the parent ItemStack
         *   The reason is because capability information is not stored in the ItemStack nbt tag, so vanilla does not notice when
         *   the flowerbag's capability has changed.
         * @param slot
         */
        protected void onContentsChanged(int slot) {
            // A problem - the ItemStack and the ItemStackHandler don't know which player is holding the flower bag.  Or in fact whether
            //   the bag is being held by any player at all.
            // We have a few choices -
            // * we can search all the players on the server to see which one is holding the bag; or
            // * we can try to store the owner of the ItemStack in the ItemStackHandler, ItemStack, or ContainerFlowerBag,
            //   (which becomes problematic if the owner drops the ItemStack, or if there is no container); or
            // * we can mark the bag as dirty and let the containerFlowerBag detect that.
            // I've used the third method because it is easier to code, produces less coupling between classes, and probably more efficient
            // Fortunately, we only need to manually force an update when the player has the container open.  If changes could occur while the
            //   item was discarded (inside an ItemEntity) it would be much trickier.
            isDirty = true;
        }

        private boolean isDirty = true;

    }


