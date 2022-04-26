package net.xijko.arche.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.KeyBindingList;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.ToolBeltOpenMessage;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber
public class ModKeybinds {

    public static final KeyBinding ToolBeltKey = new KeyBinding("key.test", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_J, "key.categories."+Arche.MOD_ID);

    public static void register()
    {
        ClientRegistry.registerKeyBinding(ToolBeltKey);
    };

    /*
    @SubscribeEvent
    public void onKeyPress(TickEvent.ClientTickEvent evt) {

        Minecraft mc = Minecraft.getInstance();
        if (evt.phase == TickEvent.Phase.END && ToolBeltKey.isPressed() && mc.isGameFocused()) {
            ModNetwork.sendToServer(new ToolBeltOpenMessage());
        }
    }*/


}

