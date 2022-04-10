package net.xijko.arche.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraftforge.common.ForgeHooks.isToolEffective;

public class MattockIronItem extends ToolItem implements IVanishable {

    int attackDamageIn;
    float attackSpeedIn = this.attackSpeedIn;
    IItemTier tier = this.tier;
    private static final Set<Block> EFFECTIVE_ON = ImmutableSet.of();
    private static final ToolType shovelType = ToolType.SHOVEL;
    private static final ToolType pickType = ToolType.PICKAXE;

    public MattockIronItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Properties builderIn) {
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
