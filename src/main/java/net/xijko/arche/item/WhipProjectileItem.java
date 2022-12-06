package net.xijko.arche.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.xijko.arche.entity.ModEntityTypes;
import net.xijko.arche.entity.WhipProjectileEntity;

public class WhipProjectileItem extends ArrowItem {
    public WhipProjectileItem(Properties builder) {
        super(builder);
    }
}
