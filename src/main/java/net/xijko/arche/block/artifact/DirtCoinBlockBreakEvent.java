package net.xijko.arche.block.artifact;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DirtCoinBlockBreakEvent extends BlockEvent.BreakEvent {
    public DirtCoinBlockBreakEvent(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super(world, pos, state, player);

        if (state != null && ForgeHooks.canHarvestBlock(state, player, world, pos)) // Handle empty block or player unable to break block scenario
        {
            int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
            int silklevel = 1;
        }
    }
/*
    @SubscribeEvent
    public static void blockBreakEvent(BlockEvent.BreakEvent event) {
        BlockEvent.BreakEvent newEvent = new BlockEvent.BreakEvent(()->{
            super(event.getWorld(), event.getPos(), event.getState());
            this.player = player;

            if (state == null || !ForgeHooks.canHarvestBlock(state, player, world, pos)) // Handle empty block or player unable to break block scenario
            {
                this.exp = 0;
            }
            else
            {
                int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
                int silklevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand());
                this.exp = state.getExpDrop(world, pos, bonusLevel, silklevel);
            }
        });

    }
*/
}
