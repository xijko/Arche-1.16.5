package net.xijko.arche.item;

import net.minecraft.client.gui.IBidiTooltip;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ArcheArtifactBrokenToolTip implements IBidiTooltip {


    @Override
    public Optional<List<IReorderingProcessor>> func_241867_d() {
        return Optional.empty();
    }


}
