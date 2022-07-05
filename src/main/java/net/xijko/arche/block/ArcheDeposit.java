package net.xijko.arche.block;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.SilkTouchEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.tileentities.DepositTile;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArcheDeposit extends OreBlock implements IForgeBlock {
    public int archeTier;
    protected static final Random random = new Random();
    public static final  BooleanProperty GENERATED = BooleanProperty.create("generated");
    public static final  IntegerProperty STRUCTUREDROP = IntegerProperty.create("structure_drop",0,8);
    public List<Structure> structures = null;
    public List<Block> blocks = null;
    public boolean checkForStructures = false;

    public ArcheDeposit(Properties properties, int archeTier, @Nullable List<Structure> structures, @Nullable List<Block> blocks, @Nullable boolean generated) {
        super(properties);
        this.archeTier = archeTier;
        this.structures = structures;
        this.blocks = blocks;
        this.setDefaultState(this.stateContainer.getBaseState().with(GENERATED, Boolean.valueOf(false)));
    }

    public List<Structure> getStructureModifiers(){
        return this.structures;
    }

    public List<Block> getStructureBlocks(){
        return this.blocks;
    }

    private List<ItemStack> getStacksForAltLoot(String alt, Boolean isSilkTouch){
        String resourceType;
        if(isSilkTouch){
            resourceType = "arche:" + alt + "_deposit";
        }else{
            resourceType = "arche:" + alt + "_debris";
        }
        ResourceLocation resultItemResourceLocation = new ResourceLocation(resourceType);
        Item resultItem = ForgeRegistries.ITEMS.getValue(resultItemResourceLocation);
        ItemStack resultStack = new ItemStack(resultItem);
        List<ItemStack> resultList = new ArrayList<>(1);
        resultList.add(0,resultStack);
        return resultList;
    }

    

    public List<ItemStack> rollDepositLoot(World worldIn, PlayerEntity playerIn, BlockState state, String alt){
        String defaultResourceName = "arche:items/deposit" + archeTier;
        if(alt!=""){
            Boolean isSilkTouch = EnchantmentHelper.getEnchantments(playerIn.getHeldItemMainhand()).containsKey(Enchantments.SILK_TOUCH);
            LOGGER.warn(EnchantmentHelper.getEnchantments(playerIn.getHeldItemMainhand()));

            return getStacksForAltLoot(alt, isSilkTouch);
        }
        LootTable defaultTable = ServerLifecycleHooks.getCurrentServer().getLootTableManager().getLootTableFromLocation(new ResourceLocation(defaultResourceName)); // resolves to /assets/mymod/loot_tables/my_table.json
        LootContext ctx = new LootContext.Builder(ServerLifecycleHooks.getCurrentServer().func_241755_D_())
                //[<parameter minecraft:direct_killer_entity>,
                // <parameter minecraft:this_entity>,
                // <parameter minecraft:explosion_radius>,
                // <parameter minecraft:tool>,
                // <parameter minecraft:origin>,
                // <parameter minecraft:damage_source>,
                // <parameter minecraft:killer_entity>,
                // <parameter minecraft:last_damage_player>,
                // <parameter minecraft:block_state>,
                // <parameter minecraft:block_entity>]
                /*.withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY,null)
                .withNullableParameter(LootParameters.THIS_ENTITY,null)
                .withNullableParameter(LootParameters.EXPLOSION_RADIUS,null)
                .withNullableParameter(LootParameters.ORIGIN,null)
                .withNullableParameter(LootParameters.DAMAGE_SOURCE,null)
                .withNullableParameter(LootParameters.KILLER_ENTITY,null)
                .withNullableParameter(LootParameters.LAST_DAMAGE_PLAYER,null)
                .withNullableParameter(LootParameters.BLOCK_STATE,null)
                .withNullableParameter(LootParameters.BLOCK_ENTITY,null)*/
                .withLuck(playerIn.getLuck())// adjust luck, commonly EntityPlayer.getLuck()
                .build(LootParameterSets.EMPTY);
                //if(this.)
        return defaultTable.generate(ctx);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(GENERATED);
    }



    @Override
    public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        if(super.canHarvestBlock(state, world, pos, player) && world.getBlockState(pos).get(GENERATED)){
            LOGGER.warn("I saw this get harvested!");
            detectStructures(state, world.getTileEntity(pos).getWorld(), (DepositTile) world.getTileEntity(pos), player);
        }
        return super.canHarvestBlock(state, world, pos, player);
    }

    public void ejectLootAndXp(World worldIn, BlockPos pos, PlayerEntity playerIn, String alt, @Nullable BlockState state){
        assert Minecraft.getInstance().player != null;
        BlockState blockState = state;
        if(state == null){blockState=this.getDefaultState();}
        List<ItemStack> lootOutcome = rollDepositLoot(worldIn, playerIn, blockState, alt);
        //Minecraft.getInstance().player.sendChatMessage("Loot: " + lootOutcome);
        for(ItemStack itemstack : lootOutcome)
        {
            ItemEntity lootItem = new ItemEntity(worldIn,pos.getX(),pos.getY()+0.5D,pos.getZ(),itemstack);
            if(random.nextInt(100) * archeTier> 75 ){
                int xpGrant = random.nextInt(archeTier)+1;
                ExperienceOrbEntity xpOrb = new ExperienceOrbEntity(worldIn,playerIn.getPosX(),playerIn.getPosY()+2,playerIn.getPosZ(),xpGrant);
                worldIn.addEntity(xpOrb);
            }
            lootItem.setMotion(0,0.1D,0);
            worldIn.addEntity(lootItem);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(GENERATED);
        builder.add(STRUCTUREDROP);
    }

    public void detectStructures(BlockState state, World world, DepositTile tile, PlayerEntity player){
        List<Structure> structureChecks = this.getStructureModifiers();
        List<Block> structureBlocks = this.getStructureBlocks();
        if(structureChecks!=null){
            for (int i=0;i<structureChecks.size();i++) {
                Structure structure = structureChecks.get(i);
                ServerWorld serverWorld = (ServerWorld) world;
                BlockPos searchPos = new BlockPos(tile.getPos().getX(),0,tile.getPos().getZ());
                BlockPos closestStructure = serverWorld.getStructureLocation(structure, searchPos, 2, false);
                if (searchPos.distanceSq(closestStructure) <= 64*64) {
                    //thisTE.world.setBlockState(thisTE.getPos(), blocks.getDefaultState().with(STRUCTUREDROP,i));
                    assert world != null;
                    //world.setBlockState(tile.getPos(), this.getDefaultState().with(STRUCTUREDROP,i));
                    ejectLootAndXp(world, tile.getPos(), player, structure.getStructure().getStructureName(), world.getBlockState(tile.getPos()));
                    //this.spawnAdditionalDrops(world.getBlockState(tile.getPos()),world,tile.getPos(), new ItemStack(RegistryObject.of(this.getRegistryName()+"alt"+i)) Rthis.getRegistryName());
                }
            }
        }
    }

}
