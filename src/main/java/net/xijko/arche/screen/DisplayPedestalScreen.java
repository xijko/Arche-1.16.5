package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.*;
import net.minecraftforge.client.gui.ScrollPanel;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.block.DisplayPedestalBlock;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.container.DisplayPedestalContainer;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.xijko.arche.block.DisplayPedestalBlock.MUSEUM_OWNED;
import static net.xijko.arche.block.DisplayPedestalBlock.MUSEUM_SLOT;

public class DisplayPedestalScreen extends ContainerScreen<DisplayPedestalContainer> implements INestedGuiEventHandler{
    private int tick = 0;
    private final ResourceLocation GUI = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/display_pedestal.png");
    private static final Logger LOGGER = LogManager.getLogger();
    private DisplayPedestalTile tile = (DisplayPedestalTile) this.container.tileEntity;
    private DisplayPedestalLorePanel lorePanel;
    public List<ITextProperties> lore;
    public List<String> words;
    double scrollVar;
    boolean scrolling = false;
    boolean hasLore = false;
    int xSize = 256; //336
    public ItemStack displayItem = ItemStack.EMPTY;

    public DisplayPedestalScreen(DisplayPedestalContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    public ItemRenderer getItemRenderer(){ return this.itemRenderer;}

    @Override
    protected void init() {
        super.init();

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //this.renderBackground(renderBGMatrixStack(matrixStack));
        matrixStack.translate(0,0,-200);
        this.renderBackground(matrixStack);
        matrixStack.translate(0,0,200);
        this.drawArtifact(matrixStack, hasLore);
        //matrixStack.translate(0,0,600);
        renderLore(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.lorePanel = new DisplayPedestalScreen.DisplayPedestalLorePanel(this.minecraft, 161, 104, this.guiTop+18, this.guiLeft+88,matrixStack,this.font,this.lore ,mouseX,mouseY, scrollVar, hasLore);
        if(this.getMaxScroll()<this.scrollVar){this.scrollVar=getMaxScroll();}
        this.lorePanel.render(matrixStack,mouseX,mouseY,partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        //setLorePanelListeners();


    }

    @Override
    public void tick() {
        super.tick();
        tick++;
        IItemHandler handler = tile.itemHandler;
        ItemStack itemStack = handler.getStackInSlot(0);
        IBakedModel ibakedmodel = this.getItemRenderer().getItemModelWithOverrides(itemStack, tile.getWorld(), null);

    }

    protected void drawArtifact(MatrixStack matrixStack, boolean hasLore){
        int rendererX = /*this.xSize/2 +*/ this.guiLeft;
        if(!hasLore) rendererX = this.width/2;
        int rendererY = this.guiTop - 50 + this.ySize/2;

        IItemHandler handler = tile.itemHandler;
        ItemStack itemStack = handler.getStackInSlot(0);
        IBakedModel ibakedmodel = this.getItemRenderer().getItemModelWithOverrides(itemStack, tile.getWorld(), null);
        renderArtifact(this,matrixStack,itemStack,rendererX,rendererY, ibakedmodel);
    }

    @Override
    public void init(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.init(p_231158_1_, p_231158_2_, p_231158_3_);
    }

    public void renderPedestal(DisplayPedestalScreen screen, Quaternion rotation, int x, int y, float z){
        ItemStack stack = new ItemStack(ModBlocks.DISPLAY_PEDESTAL.get(),1);
        IBakedModel bakedmodel = screen.getItemRenderer().getItemModelWithOverrides(stack, screen.getContainer().tileEntity.getWorld(), null);

        RenderSystem.pushMatrix();
        screen.getItemRenderer().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        screen.getItemRenderer().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)x, (float)y+100, screen.getItemRenderer().zLevel + z);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.rotate(rotation);
        matrixstack.translate(0,1.6,0);
        matrixstack.scale(6,6,6);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        boolean flag = !bakedmodel.isSideLit();
        if (flag) {
            RenderHelper.setupGuiFlatDiffuseLighting();
        }

        screen.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupGui3DDiffuseLighting();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

    public ItemStack getDisplayedStack(ItemStack stackIn){
        DisplayPedestalTile tile = this.tile;
        BlockState state = tile.getBlockState();
        BlockPos pos = tile.getPos();
        Boolean isMuseumOwned = tile.museum_owned;
        if(isMuseumOwned){
            return this.displayItem;
        }else{
            return stackIn;
        }
    }

    public void renderArtifact(DisplayPedestalScreen screen, MatrixStack matrixStack,ItemStack stackIn, int x, int y, IBakedModel bakedmodel) {
        float zLevel = -100F;
        float angle = (System.currentTimeMillis() / 50) % 360;

        ItemStack stack = getDisplayedStack(stackIn).copy();


        if(stack.isEmpty() || stack.getItem()== Items.AIR || stack == ItemStack.EMPTY) angle=45;
        if(stack.getItem() instanceof BlockItem){
            BlockItem blockItem = (BlockItem) stack.getItem();
            stack = new ItemStack(blockItem.getBlock().asItem());
        }
        //ADD CASE FOR IF EXTENDS BLOCKITEM TO RENDER BLOCK INSTEAD//
        Quaternion rotation = new Quaternion(20,angle,0,true);
        //Quaternion rot = new Quaternion(20,angle,0,true);

        RenderSystem.pushMatrix();
        screen.getItemRenderer().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        screen.getItemRenderer().textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)x, (float)y, screen.getItemRenderer().zLevel + zLevel);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.rotate(rotation);
        matrixstack.translate(0d, 1.14d, 0d);
        matrixstack.scale(6,6,6);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        boolean flag = !bakedmodel.isSideLit();
        if (flag) {
            RenderHelper.setupGuiFlatDiffuseLighting();
        }

        screen.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, false, matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupGui3DDiffuseLighting();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
        renderPedestal(screen,rotation,x,y,zLevel);

    }

    private MatrixStack renderBGMatrixStack(MatrixStack ms){
        ms.translate(0,0,-400F);
        return ms;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
/*
        if(container.isLightningStorm()) {
            this.blit(matrixStack, i + 82, j + 9, 176, 0, 13, 17);
        }
        */

    }

    protected void blockLore(ITextComponent loreIn, boolean isEmpty){
        String loreString;
        Style style = title.getStyle();
        if(isEmpty){
            loreString="";
            this.hasLore = false;
        }else{
            loreString = loreIn.getString();
            this.hasLore = true;
        }
        this.lore = font.getCharacterManager().func_238365_g_(loreString,154,style);
    }

    protected void renderLore(MatrixStack ms){
        boolean isEmpty = false;
        ItemStack item = this.tile.getItem();
        try{
            String loreRL = item.getTranslationKey()+".lore";
            ITextComponent lore = new TranslationTextComponent(loreRL);
            if(lore.getString().isEmpty() || item.isEmpty() || item.getItem()==Blocks.AIR.asItem() || loreRL.equals(lore.getString())) {
                this.lore = Collections.emptyList();
                isEmpty = true;
            }
            blockLore(lore,isEmpty);
            /*for(int i=0;i<this.lore.size();i++){
                String line = this.lore.get(i).getString();
                this.font.drawString(ms,line,leftCorner,topCorner+(i*9),1);
            }*/

        }catch(RuntimeException e){
            LOGGER.warn("RTE Error!");
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {

    }

    @Override
    protected <T extends IGuiEventListener> T addListener(T p_230481_1_) {
        this.children.add(this.lorePanel.getListener());
        return super.addListener(p_230481_1_);
    }

    @Override
    protected void handleMouseClick(Slot p_184098_1_, int p_184098_2_, int p_184098_3_, ClickType p_184098_4_) {
        if(this.lorePanel.isMouseOver(p_184098_2_,p_184098_3_)){}
        else {
            super.handleMouseClick(p_184098_1_, p_184098_2_, p_184098_3_, p_184098_4_);
        }
    }

    public class DisplayPedestalLorePanel extends ScrollPanel implements IGuiEventListener {

        private final ResourceLocation GUI = new ResourceLocation(Arche.MOD_ID,
                "textures/gui/display_pedestal_lore.png");

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
        double scrollvar;
        boolean hasLore;
        boolean scrolling;
        final Minecraft client;
        final int barWidth = 6;
        final int barLeft;
        final int bottom;
        final int border = 0;

        final int header = 0;

        public DisplayPedestalLorePanel(Minecraft client, int width, int height, int top, int left, MatrixStack ms, FontRenderer font, List<ITextProperties> lore, int x, int y, double scrollVar, boolean hasLore) {
            super(client, width, height, top, left);
            this.ms = ms;
            this.font = font;
            this.lore = lore;
            this.width = width;
            this.height = height-header;
            this.top = top+header;
            this.left = left;
            this.x = x;
            this.y = y;
            this.scrollvar = scrollVar;
            this.client = client;
            this.barLeft = this.left + this.width - barWidth;
            this.bottom = height + this.top;
            this.hasLore = hasLore;
        }

        protected void drawLoreBox(MatrixStack matrixStack) {
            RenderSystem.color4f(1f, 1f, 1f, 1f);
            this.client.getTextureManager().bindTexture(GUI);
            int i = this.left;
            int j = this.right;
            this.blit(matrixStack, i, j, 0, 0, this.width, this.height);
/*
        if(container.isLightningStorm()) {
            this.blit(matrixStack, i + 82, j + 9, 176, 0, 13, 17);
        }
        */

        }

        @Override
        public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {


            if (this.hasLore) {
                //this.drawBackground();
                drawLoreBox(matrix);


                Tessellator tess = Tessellator.getInstance();
                BufferBuilder worldr = tess.getBuffer();

                double scale = client.getMainWindow().getGuiScaleFactor();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glScissor((int) (left * scale), (int) (client.getMainWindow().getFramebufferHeight() - (bottom * scale)),
                        (int) (width * scale), (int) (height * scale));

                    /*if (this.client.world != null)
                    {
                        //this.drawGradientRect(matrix, this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
                    }
                    else // Draw dark dirt background
                    {*/

                final ResourceLocation CUSTOM_BACKGROUND = new ResourceLocation("minecraft:textures/gui/advancements/backgrounds/stone.png");
                RenderSystem.disableLighting();
                RenderSystem.disableFog();
                this.client.getTextureManager().bindTexture(CUSTOM_BACKGROUND);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                final float darkness = 0x88;
                final float texScale = 32.0F;
                worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldr.pos(this.left, this.bottom, 0.0D).tex(this.left / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(darkness, darkness, darkness, 0xFF).endVertex();
                worldr.pos(this.right, this.bottom, 0.0D).tex(this.right / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(darkness, darkness, darkness, 0xFF).endVertex();
                worldr.pos(this.right, this.top, 0.0D).tex(this.right / texScale, (this.top + (int) this.scrollDistance) / texScale).color(darkness, darkness, darkness, 0xFF).endVertex();
                worldr.pos(this.left, this.top, 0.0D).tex(this.left / texScale, (this.top + (int) this.scrollDistance) / texScale).color(darkness, darkness, darkness, 0xFF).endVertex();
                tess.draw();
                //}

                int baseY = this.top + border - (int) this.scrollDistance;
                this.drawPanel(matrix, right, baseY, tess, mouseX, mouseY);

                RenderSystem.disableDepthTest();

                int extraHeight = (this.getContentHeight() + border) - height;
                if (extraHeight > 0) {
                    int barHeight = getBarHeight();

                    int maxBarTopPos = this.top + (this.height - barHeight);
                    int minBarTopPos = this.top;

                    //int barTop = ((this.scrollvar * (height - barHeight))/ extraHeight + this.top);
                    float barRatio = (float) ((this.height - barHeight) * (this.scrollvar) / this.getContentHeight());
                    float barTop = barRatio + this.top;

                        /*if (barTop < this.top)
                        {
                            barTop = this.top;
                        }*/

                    RenderSystem.disableTexture();
                    worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(barLeft, this.bottom, 0.0D).tex(0.0F, 1.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(barLeft + barWidth, this.bottom, 0.0D).tex(1.0F, 1.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(barLeft + barWidth, this.top, 0.0D).tex(1.0F, 0.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    worldr.pos(barLeft, this.top, 0.0D).tex(0.0F, 0.0F).color(0x00, 0x00, 0x00, 0xFF).endVertex();
                    tess.draw();
                    worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(barLeft, barTop + barHeight, 0.0D).tex(0.0F, 1.0F).color(0x55, 0x55, 0x55, 0xFF).endVertex();
                    worldr.pos(barLeft + barWidth, barTop + barHeight, 0.0D).tex(1.0F, 1.0F).color(0x55, 0x55, 0x55, 0xFF).endVertex();
                    worldr.pos(barLeft + barWidth, barTop, 0.0D).tex(1.0F, 0.0F).color(0x55, 0x55, 0x55, 0xFF).endVertex();
                    worldr.pos(barLeft, barTop, 0.0D).tex(0.0F, 0.0F).color(0x55, 0x55, 0x55, 0xFF).endVertex();
                    tess.draw();
                    worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                    worldr.pos(barLeft, barTop + barHeight - 1, 0.0D).tex(0.0F, 1.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
                    worldr.pos(barLeft + barWidth - 1, barTop + barHeight - 1, 0.0D).tex(1.0F, 1.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
                    worldr.pos(barLeft + barWidth - 1, barTop, 0.0D).tex(1.0F, 0.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
                    worldr.pos(barLeft, barTop, 0.0D).tex(0.0F, 0.0F).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
                    tess.draw();
                }

                RenderSystem.enableTexture();
                RenderSystem.shadeModel(GL11.GL_FLAT);
                RenderSystem.enableAlphaTest();
                RenderSystem.disableBlend();
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
        }

        @Override
        public int getContentHeight()
        {
            int height = (this.lore.size() * 9)-this.height;
            return height;
        }

        @Override
        protected int getScrollAmount()
        {
            return 9;
        }

        @Override
        protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
            for(int i=0;i<this.lore.size();i++){
                String line = this.lore.get(i).getString();
                this.font.drawString(mStack,line, (float) this.left, (float) (relativeY-scrollVar),0xFFFFFF);
                relativeY += 9;
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.scrolling = button == 0 && mouseX >= barLeft && mouseX < barLeft + barWidth;
            if (this.scrolling)
            {
                return true;
            }
            int mouseListY = ((int)mouseY) - this.top - this.getContentHeight() + (int)this.scrollDistance - border;
            if (mouseX >= left && mouseX <= right && mouseListY < 0)
            {
                return this.clickPanel(mouseX - left, mouseY - this.top + (int)this.scrollDistance - border, button);
            }
            return false;
        }

        @Override
        public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
            boolean ret = this.scrolling;
            this.scrolling = false;
            return ret;
        }

        private int getBarHeight()
        {
            int barHeight = (height * height) / this.getContentHeight();

            if (barHeight > height)
                barHeight = height;

            return barHeight;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
        {

            this.setDragging(true);
            int maxScroll = height - getBarHeight();
            double moved = deltaY / maxScroll;
            scrollVar += getMaxScroll() * moved;
            applyScrollLimits();
            return true;
        }
        public int getMaxScroll(){
            if(needsScroll()){
                return this.getContentHeight();
            }else{
                return 0;
            }
        }

    }

    @Override
    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        if(this.lorePanel.isDragging()) this.lorePanel.setDragging(false);
        this.scrolling = false;
        if(this.lorePanel.isMouseOver(p_231048_1_,p_231048_3_)){
            return lorePanel.mouseReleased(p_231048_1_,p_231048_3_,p_231048_5_);
        }else {
            return super.mouseReleased(p_231048_1_,p_231048_3_,p_231048_5_);
        }
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if(lorePanel.isMouseOver(p_231044_1_,p_231044_3_)){
            this.scrolling = true;
            this.lorePanel.setDragging(true);
            return lorePanel.mouseClicked(p_231044_1_,p_231044_3_,p_231044_5_);
        }else {
            return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
        }
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        if(this.scrolling) {
            return this.lorePanel.mouseDragged(p_231045_1_,p_231045_3_,p_231045_5_,p_231045_6_,p_231045_8_);
        }else{
            return super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
    {if(lorePanel.isMouseOver(mouseX,mouseY)){
        //LOGGER.warn("Scrollin over! Delta: "+scroll);
        //mouseScrolled(p_231043_1_,p_231043_3_,p_231043_5_);
        if (scroll != 0 && needsScroll())
        {
            lorePanel.scrollDistance += -scroll * getScrollAmount();
            //LOGGER.warn("needsscroll is "+needsScroll());
            //LOGGER.warn("scrollDistance is "+lorePanel.scrollDistance);
            applyScrollLimits();
            //LOGGER.warn("actual scroll distance is "+lorePanel.scrollDistance);
            //LOGGER.warn("scrollvar is "+scrollVar);
            scrollVar += (int) lorePanel.scrollDistance;
            //scrollDistance = 0;
            return true;
        }
        return false;
    }else{
        return false;
    }
    }

    protected int getScrollAmount()
    {
        return 9;
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
            return this.lorePanel.getContentHeight();
        }else{
            return 0;
        }
    }

    private void applyScrollLimits()
    {
        int max = getMaxScroll();

        /*if (max < 0)
        {
            max /= 2;
        }*/

        if (scrollVar+this.lorePanel.scrollDistance<= 0)
        {
            this.lorePanel.scrollDistance = 0.0F;
            this.scrollVar=0;
        }

        if (scrollVar+this.lorePanel.scrollDistance>= max)
        {
            this.lorePanel.scrollDistance = 0;
            scrollVar = max;
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