package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.client.gui.ScrollPanel;

import java.util.List;

public class DisplayPedestalLorePanel extends ScrollPanel /*implements INestedGuiEventHandler*/ {

    MatrixStack ms;
    FontRenderer font;
    List<ITextProperties> lore;
    int width;
    int height;
    int top;
    int left;
    int x;
    int y;

    public DisplayPedestalLorePanel(Minecraft client, int width, int height, int top, int left, MatrixStack ms, FontRenderer font, List<ITextProperties> lore, int x, int y) {
        super(client, width, height, top, left);
        this.ms = ms;
        this.font = font;
        this.lore = lore;
        this.width = width;
        this.height = height;
        this.top = top;
        this.left = left;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getContentHeight()
    {
        int height = (this.lore.size() * 9);
        if (height < this.height - this.top - 8)
            height = this.height - this.top - 8;
        return height;
    }

    @Override
    protected int getScrollAmount()
    {
        return 9 * 3;
    }

    @Override
    protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
        for(int i=0;i<this.lore.size();i++){
            String line = this.lore.get(i).getString();
            this.font.drawString(mStack,line,this.left,relativeY,0xFFFFFF);
            relativeY += 9;
        }
    }
}
