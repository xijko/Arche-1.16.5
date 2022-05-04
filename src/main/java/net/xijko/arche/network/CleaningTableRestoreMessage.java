package net.xijko.arche.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.block.screen.CleaningTableScreen;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.tileentities.CleaningTableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class CleaningTableRestoreMessage {
    private static final Logger LOGGER = LogManager.getLogger();
    private static CleaningTableScreen screen;
    private static World world;
    private static BlockPos pos;

    public CleaningTableRestoreMessage(CleaningTableScreen screen, World world, BlockPos pos) {
        super();
        this.screen = screen;
        this.world  = world;
        this.pos = pos;
    }

    public static void encode(CleaningTableRestoreMessage message, PacketBuffer buffer) {
    }

    public static CleaningTableRestoreMessage decode(PacketBuffer buffer) {
        return new CleaningTableRestoreMessage(screen, world, pos);
    }

    public static void handle(CleaningTableRestoreMessage message, Supplier<NetworkEvent.Context> ctx) {
        if(!ctx.get().getPacketHandled()) {
            LOGGER.warn("received packet from "+ctx.get().getSender());
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (!(player instanceof PlayerEntity)) return;
                restoreItem(screen,player.world,pos,player);

                ctx.get().setPacketHandled(true);
            });
        }
    }

    public static void restoreItem(CleaningTableScreen screen, World world, BlockPos pos,ServerPlayerEntity player){
        LOGGER.warn(world + " " + pos);
        CleaningTableTile tile = (CleaningTableTile) world.getTileEntity(pos);
        LOGGER.warn("restoring in "+tile);
        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null).resolve().get();
        //if(!world.isRemote()){return;}
        ItemStack slotStack= handler.getStackInSlot(4);
        LOGGER.warn("slotstack is "+slotStack);
        Item slotItem = slotStack.getItem();
        if(!(slotItem instanceof ArcheArtifactBroken)){
            LOGGER.warn("not artifact");
            return;
        }else {
            ArcheArtifactBroken artifact = (ArcheArtifactBroken) slotItem;
            ItemStack artifactStack = slotStack;
            if (checkNBT(artifactStack)==null) {
                initNBT(artifactStack);
            }
            LOGGER.warn(artifactStack.getTag());
            int artifactDamage = slotStack.getDamage();
            int restoreProgress1 = 0;
            ItemStack providedComp1 = handler.getStackInSlot(0);
            Item requiredComp1 = artifact.comp1;
            //CompoundNBT artifactStackNBT = artifactStack.ge;
            int requiredQuant1 = slotStack.getTag().getInt("comp1quant");
            LOGGER.warn("slotstack requires "+requiredQuant1 +" "+requiredComp1);
            if(requiredQuant1 > 0 && artifact.comp1!=null){
                restoreProgress1 = checkMats(providedComp1,requiredComp1,requiredQuant1,artifact);
                handler.getStackInSlot(0).shrink(restoreProgress1);
                artifactStack.getTag().putInt("comp1quant",requiredQuant1- restoreProgress1);
                //artifactStack.deserializeNBT(artifactStackNBT);
                artifactStack.damageItem(-restoreProgress1,player,null);
            }

            ItemStack providedComp2 = handler.getStackInSlot(1).copy();
            Item requiredComp2 = artifact.comp2;
            int restoreProgress2 = 0;
            int requiredQuant2 = slotStack.getTag().getInt("comp2quant");
            LOGGER.warn("slotstack requires "+requiredQuant2 +" "+requiredComp2);
            if(requiredQuant2 > 0 && artifact.comp2!=null){
                restoreProgress2 = checkMats(providedComp2,requiredComp2,requiredQuant2,artifact);
                handler.getStackInSlot(1).shrink(restoreProgress2);
                artifactStack.getTag().putInt("comp2quant",requiredQuant2- restoreProgress2);
                //artifactStack.deserializeNBT(artifactStackNBT);
                artifactStack.damageItem(-restoreProgress2,player,null);
            }

            ItemStack providedComp3 = handler.getStackInSlot(2);
            Item requiredComp3 = artifact.comp3;
            int restoreProgress3 = 0;
            int requiredQuant3 = slotStack.getTag().getInt("comp3quant");
            LOGGER.warn("slotstack requires "+requiredQuant3 +" "+requiredComp3);
            if(requiredQuant3 > 0 && artifact.comp3!=null){
                restoreProgress3 = checkMats(providedComp3,requiredComp3,requiredQuant3,artifact);
                handler.getStackInSlot(2).shrink(restoreProgress3);
                artifactStack.getTag().putInt("comp3quant",requiredQuant3- restoreProgress3);
                //artifactStack.deserializeNBT(artifactStackNBT);
                artifactStack.damageItem(-restoreProgress3,player,null);
            }

            ItemStack providedComp4 = handler.getStackInSlot(3);
            Item requiredComp4 = artifact.comp4;
            int restoreProgress4 = 0;
            int requiredQuant4 = slotStack.getTag().getInt("comp4quant");
            LOGGER.warn("slotstack requires "+requiredQuant4 +" "+requiredComp4);
            if(requiredQuant4 > 0 && artifact.comp4!=null){
                restoreProgress4 = checkMats(providedComp4,requiredComp4,requiredQuant4,artifact);
                handler.getStackInSlot(3).shrink(restoreProgress4);
                artifactStack.getTag().putInt("comp4quant",requiredQuant4- restoreProgress4);
                //artifactStack.deserializeNBT(artifactStackNBT);
                artifactStack.damageItem(-restoreProgress4,player,null);
            }
            assert artifactStack.getTag() != null;
            if(insertResult(artifactStack.getTag())){
                ItemStack result = new ItemStack(artifact.artifactOut,1);
                tile.outputItemHandler.insertItem(0,result,false);
                artifactStack.setCount(0);
                LOGGER.warn("Restored!");
            }

            tile.markDirty();
        }
    }

    public static boolean insertResult(CompoundNBT tag){
        return (
                tag.getInt("comp1quant") +
                        tag.getInt("comp1quant") +
                        tag.getInt("comp1quant") +
                        tag.getInt("comp1quant")
        )
                == 0;
    }

    public static CompoundNBT checkNBT(ItemStack stack){
        CompoundNBT nbtTagCompound = stack.getTag();
        if (nbtTagCompound == null){
            return initNBT(stack);
        }else{
            return stack.getTag();
        }

    }

    private static CompoundNBT initNBT(ItemStack stack){
        CompoundNBT  nbt = new CompoundNBT();
        ArcheArtifactBroken artifactItem = (ArcheArtifactBroken) stack.getItem();
        nbt.putInt("comp1quant",artifactItem.getComp1Quant());
        nbt.putInt("comp2quant",artifactItem.getComp2Quant());
        nbt.putInt("comp3quant",artifactItem.getComp3Quant());
        nbt.putInt("comp4quant",artifactItem.getComp4Quant());
        stack.setDamage(15);
        stack.setTag(nbt);
        return nbt;
    }

    private static int checkMats(ItemStack providedComps, Item requiredComp, int requiredQuant, ArcheArtifactBroken artifactBroken) {
        if(providedComps.getItem() == requiredComp) {
            int providedQuant = providedComps.getCount();
            int reduceAmount = requiredQuant - providedQuant;
            LOGGER.warn("provided "+providedQuant+" "+providedComps +"toward "+requiredQuant+" "+requiredComp);
            if (reduceAmount <= 0) {
                reduceAmount = requiredQuant;
            } else {
                reduceAmount = providedQuant;
            }
            return reduceAmount;
        }else{
            return 0;
        }
    }

}
