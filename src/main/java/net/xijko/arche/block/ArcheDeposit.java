package net.xijko.arche.block;

import net.minecraft.block.OreBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Random;

public class ArcheDeposit extends OreBlock {
    public int archeTier;
    protected static final Random random = new Random();

    public ArcheDeposit(Properties properties, int archeTier) {
        super(properties);
        this.archeTier = archeTier;
    }

    public List<ItemStack> rollDepositLoot(World worldIn, PlayerEntity playerIn){
        String resourceName = "arche:items/deposit" + archeTier;
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

    public void ejectLootAndXp(World worldIn, PlayerEntity playerIn){
        assert Minecraft.getInstance().player != null;
        List<ItemStack> lootOutcome = rollDepositLoot(worldIn, playerIn);
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


}
