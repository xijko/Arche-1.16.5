package net.xijko.arche.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.screen.DisplayPedestalScreen;
import net.xijko.arche.tileentities.SoilTableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class SoilTableRestoreMessage {
    private static final Logger LOGGER = LogManager.getLogger();
    private static DisplayPedestalScreen screen;
    private static World world;
    private static BlockPos pos;

    public SoilTableRestoreMessage(DisplayPedestalScreen screen, World world, BlockPos pos) {
        super();
        this.screen = screen;
        this.world  = world;
        this.pos = pos;
    }

    public static void encode(SoilTableRestoreMessage message, PacketBuffer buffer) {
    }

    public static SoilTableRestoreMessage decode(PacketBuffer buffer) {
        return new SoilTableRestoreMessage(screen, world, pos);
    }

    public static void handle(SoilTableRestoreMessage message, Supplier<NetworkEvent.Context> ctx) {
        if(!ctx.get().getPacketHandled()) {
            LOGGER.warn("received packet from "+ctx.get().getSender());
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (!(player instanceof PlayerEntity)) return;
                //restoreItem(screen,player.world,pos,player);

                ctx.get().setPacketHandled(true);
            });
        }
    }

}
