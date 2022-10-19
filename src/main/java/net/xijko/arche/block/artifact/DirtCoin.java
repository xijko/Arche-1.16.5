package net.xijko.arche.block.artifact;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.item.ModItemGroup;
import net.xijko.arche.world.gen.ModStructureGen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Map;

import static net.minecraft.entity.ai.attributes.AttributeModifier.Operation.*;

public class DirtCoin  extends ArcheArtifactItem implements ICurioItem {

    private static final Logger LOGGER = LogManager.getLogger();
    private AttributeModifier attributemodifier0 = new AttributeModifier("dirtcoin_reset",0,MULTIPLY_TOTAL);
    private AttributeModifier attributemodifier1 = new AttributeModifier("dirtcoin_add",1,ADDITION);

    public DirtCoin(Properties properties) {

        super(properties.group(ModItemGroup.ARCHE_GROUP),4,0);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        LivingEntity livingEntity = slotContext.getWearer();
        livingEntity.getAttribute(Attributes.LUCK).applyNonPersistentModifier(attributemodifier0);
        livingEntity.getAttribute(Attributes.LUCK).applyNonPersistentModifier(attributemodifier1);
        LOGGER.warn("Luck is at "+livingEntity.getAttribute(Attributes.LUCK).getValue());
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
        LivingEntity livingEntity = slotContext.getWearer();
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.LUCK);
        attribute.removeModifier(attributemodifier0);
        attribute.removeModifier(attributemodifier1);
    }
}
