package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.gui.ScrollPanel;

public class DisplayPedestalLorePanel extends ScrollPanel {

    public ITextComponent lore;
    public FontRenderer font;

    public DisplayPedestalLorePanel(Minecraft client, int width, int height, int top, int left) {
        super(client, width, height, top, left);
    }

    @Override
    protected int getContentHeight() {
        return 0;
    }

    @Override
    protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {

    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.render(matrix, mouseX, mouseY, partialTicks);
    }
}
