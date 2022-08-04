package net.xijko.arche.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;
import net.minecraftforge.client.gui.ScrollPanel;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.container.DisplayPedestalContainer;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;

public class DisplayPedestalScreen extends ContainerScreen<DisplayPedestalContainer> implements INestedGuiEventHandler{
    private int tick = 0;
    private final ResourceLocation GUI = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/display_pedestal.png");
    private static final Logger LOGGER = LogManager.getLogger();
    private DisplayPedestalTile tile = (DisplayPedestalTile) this.container.tileEntity;
    private DisplayPedestalLorePanel lorePanel;
    public List<ITextProperties> lore;
    public List<String> words;

    public DisplayPedestalScreen(DisplayPedestalContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        //initButton(screenContainer);
    }

    public ItemRenderer getItemRenderer(){ return this.itemRenderer;}

    @Override
    protected void init() {
        super.init();

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //this.renderBackground(renderBGMatrixStack(matrixStack));
        this.renderBackground(matrixStack);
        renderLore(matrixStack);
        int height = this.height;
        int width = this.width;
        int top = this.guiTop;
        int left = this.guiLeft;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.lorePanel = new DisplayPedestalLorePanel(this.minecraft, width / 2, height - 20, top, left,matrixStack,this.font,this.lore ,mouseX,mouseY);
        matrixStack.translate(0,0,500);
        this.lorePanel.render(matrixStack,mouseX,mouseY,partialTicks);
        setLorePanelListeners();
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        //this.lorePanel = new DisplayPedestalScreen.DisplayPedestalLorePanel(this.minecraft, this.width / 2, this.height, this.guiTop, this.guiLeft + this.width / 2,matrixStack,this.font,this.lore ,mouseX,mouseY);
        //this.drawArtifact(matrixStack);


        //this.drawComponentMaterials();
        //this.drawComponentOverlay(matrixStack);
    }

    public void setLorePanelListeners(){
        this.lorePanel.setListener(new IGuiEventListener() {
            @Override
            public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
                return IGuiEventListener.super.mouseScrolled(p_231043_1_, p_231043_3_, p_231043_5_);
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
        IItemHandler handler = tile.itemHandler;
        ItemStack itemStack = handler.getStackInSlot(0);
        //this.itemRenderer.renderItemIntoGUI(new ItemStack(item),50,50);
        IBakedModel ibakedmodel = this.getItemRenderer().getItemModelWithOverrides(itemStack, tile.getWorld(), null);
        //tile.renderArtifact(this,itemStack,20,20, ibakedmodel);

    }

    protected void drawArtifact(MatrixStack matrixStack){
        int rendererX = /*this.xSize/2 +*/ this.guiLeft;
        int rendererY = this.guiTop - 50 + this.ySize/2;

        IItemHandler handler = tile.itemHandler;
        ItemStack itemStack = handler.getStackInSlot(0);
        //this.itemRenderer.renderItemIntoGUI(new ItemStack(item),50,50);
        IBakedModel ibakedmodel = this.getItemRenderer().getItemModelWithOverrides(itemStack, tile.getWorld(), null);
        //matrixStack.translate(0,0,100);
        renderArtifact(this,matrixStack,itemStack,rendererX,rendererY, ibakedmodel);
    }

    @Override
    public void init(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.init(p_231158_1_, p_231158_2_, p_231158_3_);
    }

    public void renderPedestal(DisplayPedestalScreen screen, Quaternion rotation, int x, int y, float z){
        ItemStack stack = new ItemStack(ModBlocks.DISPLAY_PEDESTAL.get(),1);
        IBakedModel bakedmodel = screen.getItemRenderer().getItemModelWithOverrides(stack, screen.getContainer().tileEntity.getWorld(), null);
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

    public void renderArtifact(DisplayPedestalScreen screen, MatrixStack matrixStack,ItemStack stackIn, int x, int y, IBakedModel bakedmodel) {
        float zLevel = -200F;
        float angle = (System.currentTimeMillis() / 50) % 360;

        ItemStack stack = stackIn.copy();

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

    protected List<ITextProperties> blockLore(ITextComponent loreIn){
        IFormattableTextComponent formattedLore = (IFormattableTextComponent) loreIn;
        Style style = title.getStyle();
        return font.getCharacterManager().func_238365_g_(loreIn.getString(),this.width/2,style);
    }

    protected void setLore(ITextComponent loreIn){
        this.lore = blockLore(loreIn);

    }

    protected void renderLore(MatrixStack ms){
        ItemStack item = this.tile.getItem();
        int leftCorner =  guiLeft+width/2;
        int topCorner = guiTop;
        try{
            String loreRL = item.getTranslationKey()+".lore";
            ITextComponent lore = new TranslationTextComponent(item.getTranslationKey()+".lore");
            if(lore.getString().isEmpty() || item.isEmpty() || item.getItem()==Blocks.AIR.asItem() || loreRL.equals(lore.getString())) this.lore = Collections.emptyList();
            setLore(lore);
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

}