package net.xijko.arche.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.loot.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.xijko.arche.block.CleaningTableBlock;
import net.xijko.arche.storages.toolbelt.ToolBeltCheckCurio;
import net.xijko.arche.storages.toolbelt.ToolBeltItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public class ArcheDebris extends Item {


    private static final Logger LOGGER = LogManager.getLogger();
    private int archeTier;
    public String alt = "";
    public boolean isAltDeposit = false;

    public ArcheDebris(Properties properties, int archeTier, String alt, @Nullable boolean isAltDeposit) {
        super(new Properties().group(ModItemGroup.ARCHE_GROUP));
        this.archeTier = archeTier;
        this.alt = alt;
        this.isAltDeposit = false;
        if(alt!=""){
            if(isAltDeposit){
                this.isAltDeposit = isAltDeposit;
            }
        }
    }

    public int getArcheTier() {
        return this.archeTier;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        if (world.getBlockState(blockpos).getBlock() instanceof CleaningTableBlock && this.isAltDeposit) {
            LOGGER.warn("This action went through!");
                return ActionResultType.FAIL;
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        Item offHandTool = playerIn.getHeldItemOffhand().getItem();
        ItemStack equippedToolBeltStack = new ToolBeltCheckCurio().ToolBeltCheckCurio(playerIn);
        if (
                handIn == Hand.MAIN_HAND &&
                offHandTool.getClass() == ArcheSieves.class &&
                ((ArcheSieves) offHandTool).getArcheTier() >= this.getArcheTier() &&
        !this.isAltDeposit
        ){
            if(playerIn.getHeldItemOffhand().isDamageable()){
                playerIn.getHeldItemOffhand().damageItem(1,playerIn,playerEntity -> {
                    playerEntity.sendBreakAnimation(Hand.OFF_HAND);
                });
            }
            ejectLootAndXp(worldIn, null, playerIn, null);
                playerIn.getHeldItem(handIn).shrink(1);
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));

        }else if(!(equippedToolBeltStack == null) &&
                !this.isAltDeposit){
            ToolBeltItem toolBeltItem = (ToolBeltItem) equippedToolBeltStack.getItem();
            ToolBeltItemStackHandler toolBeltItemStackHandler = ToolBeltItem.getStackHandler(equippedToolBeltStack);
            int toolBeltSlots = toolBeltItemStackHandler.getSlots();
            for(int i=0;i<toolBeltSlots;++i){
                ItemStack slotStack = toolBeltItemStackHandler.getStackInSlot(i);
                if(slotStack.getItem() instanceof ArcheSieves){
                    ArcheSieves slotSieve = (ArcheSieves) slotStack.getItem();
                    int sieveTier = slotSieve.getArcheTier();
                    if(sieveTier >= this.getArcheTier()){
                        slotStack.damageItem(1,playerIn,playerEntity -> {
                            playerEntity.sendBreakAnimation(EquipmentSlotType.CHEST);
                        });
                        ejectLootAndXp(worldIn, null, playerIn, null);
                        playerIn.getHeldItem(handIn).shrink(1);
                        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
                    }
                }
            }
        }
        LOGGER.warn("Invalid offhand detected- tag:"+playerIn.getHeldItemOffhand().getItem().getClass()+", Debris tier: " + this.archeTier);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public void ejectLootAndXp(World worldIn, @Nullable BlockPos pos, PlayerEntity playerIn, @Nullable BlockState state){
        assert Minecraft.getInstance().player != null;
        List<ItemStack> lootOutcome = rollDebrisLoot(worldIn, playerIn);
        //Minecraft.getInstance().player.sendChatMessage("Loot: " + lootOutcome);
        double x,y,z = 0;
        if(pos!=null){
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
        }else{
            x = playerIn.getPosX();
            y = playerIn.getPosY();
            z = playerIn.getPosZ();
        }
        for(ItemStack itemstack : lootOutcome)
        {
            ItemEntity lootItem = new ItemEntity(worldIn,x,y+1,z,itemstack);
            if(random.nextInt(100) * archeTier> 75 ){
                int xpGrant = random.nextInt(archeTier)+1;
                ExperienceOrbEntity xpOrb = new ExperienceOrbEntity(worldIn,playerIn.getPosX(),playerIn.getPosY()+2,playerIn.getPosZ(),xpGrant);
                worldIn.addEntity(xpOrb);
            }
            worldIn.addEntity(lootItem);
        }
    }

    public List<ItemStack> rollDebrisLoot(World worldIn, PlayerEntity playerIn){
        String resourceName = "arche:items/debris" + archeTier;
        if(this.alt!=""){
            if(this.isAltDeposit){
                resourceName = "arche:items/" + this.alt + "_deposit";
            }else{
                resourceName = "arche:items/" + this.alt + "_debris";
            }
        }
        LootTable table = ServerLifecycleHooks.getCurrentServer().getLootTableManager().getLootTableFromLocation(new ResourceLocation(resourceName)); // resolves to /assets/mymod/loot_tables/my_table.json
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
                .withNullableParameter(LootParameters.TOOL,null)
                .withNullableParameter(LootParameters.ORIGIN,null)
                .withNullableParameter(LootParameters.DAMAGE_SOURCE,null)
                .withNullableParameter(LootParameters.KILLER_ENTITY,null)
                .withNullableParameter(LootParameters.LAST_DAMAGE_PLAYER,null)
                .withNullableParameter(LootParameters.BLOCK_STATE,null)
                .withNullableParameter(LootParameters.BLOCK_ENTITY,null)*/
                .withLuck(playerIn.getLuck())// adjust luck, commonly EntityPlayer.getLuck()
                .build(LootParameterSets.EMPTY);
        return table.generate(ctx);
    }

}
