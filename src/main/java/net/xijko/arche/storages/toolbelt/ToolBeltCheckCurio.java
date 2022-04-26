package net.xijko.arche.storages.toolbelt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.xijko.arche.item.ToolBeltItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class ToolBeltCheckCurio {


    private static final Logger LOGGER = LogManager.getLogger();

    public ItemStack ToolBeltCheckCurio(PlayerEntity player){
        if (player == null) return null;
        List<SlotResult> beltCurios = CuriosApi.getCuriosHelper().findCurios(player, "belt");
        LOGGER.warn("Curios slots returned:"+beltCurios);
        for (SlotResult slot : beltCurios) {
            ItemStack slotStack = slot.getStack();
            Item slotItem = slotStack.getItem();
            if (slotItem instanceof ToolBeltItem && !player.world.isRemote) {
                return slotStack;
            }
        }
        return null;
    }
}
