package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.client.gui.ScrollPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public class DisplayPedestalLorePanel extends ScrollPanel implements IGuiEventListener {
    private static final Logger LOGGER = LogManager.getLogger();

    MatrixStack ms;
    FontRenderer font;
    List<ITextProperties> lore;
    int width;
    int height;
    int top;
    int left;
    int x;
    int y;
    float scrollDistance;
    int scrollVar;

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
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
/*
        Tessellator tess = Tessellator.getInstance();

        int baseY = this.top + border + (int)this.scrollVar;
        LOGGER.warn(baseY);*/
        super.render(matrix, mouseX, mouseY, partialTicks);
        /*if(scrollVar>this.getMaxScroll()){
            scrollVar=0;
        }*/
        //this.drawPanel(matrix, right, baseY, tess, mouseX, mouseY);
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
        return 9;
    }

    @Override
    protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
        //relativeY-=scrollDistance;
        for(int i=0;i<this.lore.size();i++){
            String line = this.lore.get(i).getString();
            this.font.drawString(mStack,line,this.left,relativeY-scrollDistance,0xFFFFFF);
            relativeY += 9;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
    {
        if (scroll != 0 && needsScroll())
        {
            scrollDistance += -scroll * getScrollAmount();
            LOGGER.warn("scrollDistance is "+scrollDistance);
            applyScrollLimits();
            LOGGER.warn("actual scroll distance is "+scrollDistance);
            LOGGER.warn("needsscroll is "+needsScroll());
            LOGGER.warn("scrollvar is "+scrollVar);
            this.scrollVar += (int) scrollDistance;
            LOGGER.warn("scrollvar is "+scrollVar);
            return true;
        }
        return false;
    }

    private boolean needsScroll(){
        if (this.lore.size()*9 >= this.height){
            return true;
        }else{
            return false;
        }
    }

    public int getMaxScroll(){
        if(needsScroll()){
            return this.lore.size();
        }else{
            return 0;
        }
    }

    public void applyScrollLimits(){
        int max = getMaxScroll();
        LOGGER.warn("max is "+max);

        if (max < 0)
        {
            max /= 2;
        }

        if (scrollDistance < 0.0F)
        {
            scrollDistance = 0.0F;
        }

        if (scrollDistance > max)
        {
            scrollDistance = max;
        }
    }

    @Nullable
    @Override
    public IGuiEventListener getListener() {
        return super.getListener();
    }

    public void getMouseOver(double x, double y){
            if(this.isMouseOver(x,y)) LOGGER.warn("Impeding!");
    }
}
