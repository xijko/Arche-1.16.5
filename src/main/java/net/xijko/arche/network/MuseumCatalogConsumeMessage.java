package net.xijko.arche.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.screen.MuseumCatalogScreen;
import net.xijko.arche.screen.RestoreTableScreen;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import net.xijko.arche.tileentities.MuseumCatalogTile;
import net.xijko.arche.tileentities.RestoreTableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class MuseumCatalogConsumeMessage {
    private static final Logger LOGGER = LogManager.getLogger();
    private static MuseumCatalogScreen screen;
    private static World world;
    private static BlockPos pos;

    public MuseumCatalogConsumeMessage(World world, BlockPos pos) {
        super();
        this.world  = world;
        this.pos = pos;
    }

    public static void encode(MuseumCatalogConsumeMessage message, PacketBuffer buffer) {
    }

    public static MuseumCatalogConsumeMessage decode(PacketBuffer buffer) {
        return new MuseumCatalogConsumeMessage(world, pos);
    }

    public static void handle(MuseumCatalogConsumeMessage message, Supplier<NetworkEvent.Context> ctx) {
        if(!ctx.get().getPacketHandled()) {
            LOGGER.warn("received packet from "+ctx.get().getSender());
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (!(player instanceof PlayerEntity)) return;
                consumeItem(player.world,pos,player);

                ctx.get().setPacketHandled(true);
            });
        }
    }

    public static void consumeItem(World world, BlockPos pos, ServerPlayerEntity player){
        LOGGER.warn(world + " " + pos);
        MuseumCatalogTile tile = (MuseumCatalogTile) world.getTileEntity(pos);
        LOGGER.warn("restoring in "+tile);
        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).resolve().get();
        //if(!world.isRemote()){return;}
        ItemStack slotStack= handler.getStackInSlot(0);
        LOGGER.warn("slotstack is "+slotStack);
        Item slotItem = slotStack.getItem();
        if(!(slotItem instanceof ArcheArtifactItem)){
            LOGGER.warn("not artifact");
            return;
        }else {
            ArcheArtifactItem artifact = (ArcheArtifactItem) slotItem;
            int slot = ((ArcheArtifactItem) slotItem).slot;
            tile.artifactCompletion[slot] = true;
            tile.write(tile.serializePedestalsNBT());
            LOGGER.warn(tile.serializePedestalsNBT());

            int xPos = tile.xArray[slot];
            int yPos = tile.yArray[slot];
            int zPos = tile.zArray[slot];

            BlockPos pedestalPos = new BlockPos(xPos,yPos,zPos);
            TileEntity pedestalTile = tile.getWorld().getTileEntity(pedestalPos);
            if(!(pedestalTile instanceof DisplayPedestalTile))return;
            ((DisplayPedestalTile) pedestalTile).setItem(handler.getStackInSlot(0),0);
            handler.getStackInSlot(0).shrink(1);
            tile.markDirty();
        }
    }

}
