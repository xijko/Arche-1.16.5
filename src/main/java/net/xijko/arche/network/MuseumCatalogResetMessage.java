package net.xijko.arche.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.screen.MuseumCatalogScreen;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import net.xijko.arche.tileentities.MuseumCatalogTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class MuseumCatalogResetMessage {
    private static final Logger LOGGER = LogManager.getLogger();
    private static MuseumCatalogScreen screen;
    private static World world;
    private static BlockPos pos;

    public MuseumCatalogResetMessage(World world, BlockPos pos) {
        super();
        this.world  = world;
        this.pos = pos;
    }

    public static void encode(MuseumCatalogResetMessage message, PacketBuffer buffer) {
    }

    public static MuseumCatalogResetMessage decode(PacketBuffer buffer) {
        return new MuseumCatalogResetMessage(world, pos);
    }

    public static void handle(MuseumCatalogResetMessage message, Supplier<NetworkEvent.Context> ctx) {
        if(!ctx.get().getPacketHandled()) {
            LOGGER.warn("received packet from "+ctx.get().getSender());
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (!(player instanceof PlayerEntity)) return;
                MuseumCatalogTile tile = (MuseumCatalogTile) world.getTileEntity(pos);
                assert tile != null;
                tile.resetCatalog(player.world,pos);

                ctx.get().setPacketHandled(true);
            });
        }
    }
}
