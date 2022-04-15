package net.xijko.arche.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;

import java.util.Set;

public class MattockItem extends ToolItem implements IVanishable {

    int attackDamageIn;
    float attackSpeedIn = this.attackSpeedIn;
    IItemTier tier = this.tier;
    private static final Set<Block> EFFECTIVE_ON = ImmutableSet.of();
    private static final ToolType shovelType = ToolType.SHOVEL;
    private static final ToolType pickType = ToolType.PICKAXE;

    public MattockItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON, builderIn.addToolType(ToolType.SHOVEL, tier.getHarvestLevel()));
    }


    public boolean canHarvestBlock(BlockState blockIn) {
        int i = this.getTier().getHarvestLevel();
         if( (blockIn.isToolEffective(pickType) || blockIn.isToolEffective(shovelType)) && i>=blockIn.getHarvestLevel()){
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

}
