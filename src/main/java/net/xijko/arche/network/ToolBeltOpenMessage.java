package net.xijko.arche.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.xijko.arche.item.ToolBeltItem;
import net.xijko.arche.storages.toolbelt.ToolBeltContainer;
import net.xijko.arche.util.ModKeybinds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.function.Supplier;

public class ToolBeltOpenMessage {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void encode(ToolBeltOpenMessage message, PacketBuffer buffer) {
        //return new ToolBeltOpenMessage();
    }

    /*public static ToolBeltOpenMessage decode(PacketBuffer buffer) {
        return new ToolBeltOpenMessage();
    }*/

    public static ToolBeltOpenMessage decode(PacketBuffer buffer) {
        return new ToolBeltOpenMessage();
    }

    public static void handle(ToolBeltOpenMessage message, Supplier<NetworkEvent.Context> ctx) {
        if(!ctx.get().getPacketHandled()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (!(player instanceof PlayerEntity)) return;
                List<SlotResult> beltCurios = CuriosApi.getCuriosHelper().findCurios(player, "belt");
                LOGGER.warn("Curios slots returned:"+beltCurios);
                for (SlotResult slot : beltCurios) {
                    ItemStack slotStack = slot.getStack();
                    Item slotItem = slotStack.getItem();
                    if (slotItem instanceof ToolBeltItem && !player.world.isRemote) {
                        LOGGER.warn("Found Slot:" + slot + "|" + slotStack + "|" + slotItem);
                        ((ToolBeltItem) slotItem).onRemoteOpen(player.world,player, slotStack);
                        return;

                    }
                }
                //RepairContainer(id, playerInventory, IWorldPosCallable.of(player.world, player.getPosition()));

                ctx.get().setPacketHandled(true);
            });
        }
    }
}
