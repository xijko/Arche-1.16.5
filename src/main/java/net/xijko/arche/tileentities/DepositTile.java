package net.xijko.arche.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.xijko.arche.block.ArcheDeposit;
import net.xijko.arche.block.ModBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

import static net.xijko.arche.block.ArcheDeposit.GENERATED;
import static net.xijko.arche.block.ArcheDeposit.STRUCTUREDROP;

public class DepositTile extends TileEntity{

    private static final Logger LOGGER = LogManager.getLogger();
    protected static final Random random = new Random();
    public Block block;

    public DepositTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void remove() {
        this.removed = true;
        this.invalidateCaps();
    }

    @Override
    public void onLoad() {
        if(!this.world.isRemote()){
            //this.detectStructures();
        }
    }

    public void detectStructures(){
        DepositTile thisTE = this;
        if(thisTE.getWorld().isRemote()){
            this.remove();
            return;}
        if(!thisTE.getBlockState().get(GENERATED)){
            this.remove();
            return;}
        this.world.markChunkDirty(this.pos, this);
        ArcheDeposit depositBlock = (ArcheDeposit) thisTE.getWorld().getBlockState(thisTE.pos).getBlock();
        BlockState thisBlock = thisTE.getWorld().getBlockState(thisTE.pos);
        List<Structure> structureChecks = depositBlock.getStructureModifiers();
        List<Block> structureBlocks = depositBlock.getStructureBlocks();
        if(structureChecks!=null){
                for (Structure structure : structureChecks) {
                    ServerWorld serverWorld = (ServerWorld) thisTE.getWorld();
                    BlockPos closestStructure = serverWorld.getStructureLocation(structure, thisTE.getPos(), 3, false);
                    if (thisTE.getPos().distanceSq(closestStructure) <= 64*64) {
                        //thisTE.world.setBlockState(thisTE.getPos(), blocks.getDefaultState().with(STRUCTUREDROP,i));
                        assert thisTE.world != null;
                        this.world.setBlockState(thisTE.getPos(), Blocks.DIAMOND_ORE.getDefaultState());
                        LOGGER.warn("ORE TRANSFORMED! AT "+this.pos);
                        return;
                    }
                }
        }
        this.remove();
    }

    /*@Override
    protected void itemHandler.onContentsChanged(int slot) {

        if (slot==4){
            super(this.)
        }

    }*/
}
