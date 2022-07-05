package net.xijko.arche.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.xijko.arche.block.ArcheDeposit;

import java.util.Map;
import java.util.Set;

public class MattockItem extends ToolItem implements IVanishable {

    int attackDamageIn;
    float attackSpeedIn = this.attackSpeedIn;
    IItemTier tier = this.tier;
    private static final Set<Block> EFFECTIVE_ON = ImmutableSet.of();
    private static final ToolType shovelType = ToolType.SHOVEL;
    private static final ToolType pickType = ToolType.PICKAXE;
    public int archeTier;

    public MattockItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, int archeTier, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON, builderIn.addToolType(ToolType.SHOVEL, tier.getHarvestLevel()));
        this.archeTier = archeTier;
    }


    public boolean canHarvestBlock(BlockState blockIn) {
        int i = this.getTier().getHarvestLevel();
         if( (blockIn.isToolEffective(pickType) || blockIn.isToolEffective(shovelType)) && i>=blockIn.getHarvestLevel()){
             if(blockIn.getBlock() instanceof ArcheDeposit){
                 return this.archeTier >= ((ArcheDeposit) blockIn.getBlock()).archeTier;
             }
             return true;
         } else{
             return false;
         }

    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        /*float shovelSpeed = new ShovelItem(this.tier, this.attackDamageIn, this.attackSpeedIn, (new Item.Properties())).getDestroySpeed(stack, state);
        float pickSpeed = new PickaxeItem(this.tier, this.attackDamageIn, this.attackSpeedIn, (new Item.Properties())).getDestroySpeed(stack, state);
        float average = (shovelSpeed+pickSpeed)/2;*/
        if (state.isToolEffective(shovelType) || state.isToolEffective(pickType)) return this.efficiency*0.75f;
        return 1.0F;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            ItemStack item = player.getHeldItem(Hand.MAIN_HAND);

            // Get list of enchantements on item.
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);

            if(enchantments.containsKey(Enchantments.FORTUNE)){
                if (state.getBlock() == Blocks.DIAMOND_ORE) {
                    int fortuneMultiplier = enchantments.get(Enchantments.FORTUNE);
                    worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.DIAMOND_SPLINT.get(), fortuneMultiplier)));
                }
                if (state.getBlock() == Blocks.EMERALD_ORE) {
                    int fortuneMultiplier = enchantments.get(Enchantments.FORTUNE);
                    worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.EMERALD_SPLINT.get(), fortuneMultiplier)));
                }
            }


        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
}
