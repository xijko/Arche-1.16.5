package net.xijko.arche.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.xijko.arche.Arche;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ModNetwork {

    private static final Logger LOGGER = LogManager.getLogger();
    private static int index = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Arche.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void packetRegister() {
        INSTANCE.registerMessage(
                index++,
                ToolBeltOpenMessage.class,
                ToolBeltOpenMessage::encode,
                ToolBeltOpenMessage::decode,
                ToolBeltOpenMessage::handle
        );
    }

    public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);

        LOGGER.warn("Sent packet:"+message);
    }
}
