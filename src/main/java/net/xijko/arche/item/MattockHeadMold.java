package net.xijko.arche.item;

import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.extensions.IForgeItem;

import java.util.function.Consumer;

public class MattockHeadMold extends Item implements IForgeItem {


    public MattockHeadMold(Properties properties) {
        super(properties);
        properties.containerItem(this);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        Entity player = itemStack.getAttachedEntity();
        ItemStack item = itemStack.copy();
        item.setDamage(item.getDamage()+1);
        //world.playSound(x, y, z, SoundEvent.REGISTRY.getObject("block.anvil.break"), category, volume, pitch, distanceDelay);

        return item;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        int resultingDamage = stack.getDamage()+1;
        boolean willBreak = stack.getMaxDamage() <= resultingDamage;
        return !willBreak;
    }
}
