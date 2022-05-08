package net.xijko.arche.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.xijko.arche.storages.toolbelt.ToolBeltCheckCurio;
import net.xijko.arche.storages.toolbelt.ToolBeltItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ArcheDebris extends Item {


    private static final Logger LOGGER = LogManager.getLogger();
    private int archeTier;

    public ArcheDebris(Properties properties, int archeTier) {
        super(new Properties().group(ModItemGroup.ARCHE_GROUP));
        this.archeTier = archeTier;
    }

    public int getArcheTier() {
        return this.archeTier;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        Item offHandTool = playerIn.getHeldItemOffhand().getItem();
        ItemStack equippedToolBeltStack = new ToolBeltCheckCurio().ToolBeltCheckCurio(playerIn);
        if (
                handIn == Hand.MAIN_HAND &&
                offHandTool.getClass() == ArcheSieves.class &&
                ((ArcheSieves) offHandTool).getArcheTier() >= this.getArcheTier()
        ){
            if(playerIn.getHeldItemOffhand().isDamageable()){
                playerIn.getHeldItemOffhand().damageItem(1,playerIn,playerEntity -> {
                    playerEntity.sendBreakAnimation(Hand.OFF_HAND);
                });
            }
            ejectLootAndXp(worldIn, playerIn);
                playerIn.getHeldItem(handIn).shrink(1);
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));

        }else if(!(equippedToolBeltStack == null)){
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
                        ejectLootAndXp(worldIn, playerIn);
                        playerIn.getHeldItem(handIn).shrink(1);
                        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
                    }
                }
            }
        }
        LOGGER.warn("Invalid offhand detected- tag:"+playerIn.getHeldItemOffhand().getItem().getClass()+", Debris tier: " + this.archeTier);
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    public void ejectLootAndXp(World worldIn, PlayerEntity playerIn){
        assert Minecraft.getInstance().player != null;
        List<ItemStack> lootOutcome = rollDebrisLoot(worldIn, playerIn);
        //Minecraft.getInstance().player.sendChatMessage("Loot: " + lootOutcome);
        for(ItemStack itemstack : lootOutcome)
        {
            ItemEntity lootItem = new ItemEntity(worldIn,playerIn.getPosX(),playerIn.getPosY()+1,playerIn.getPosZ(),itemstack);
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
