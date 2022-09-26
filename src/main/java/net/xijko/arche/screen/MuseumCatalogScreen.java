package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.container.MuseumCatalogContainer;
import net.xijko.arche.container.RestoreTableContainer;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.RestoreTableRestoreMessage;
import net.xijko.arche.tileentities.MuseumCatalogTile;
import net.xijko.arche.tileentities.RestoreTableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class MuseumCatalogScreen extends ContainerScreen<MuseumCatalogContainer> {
    private final ResourceLocation GUI = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/restore_table.png");
    private final ResourceLocation compOverlay = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/restore_table_overlay.png");

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation CLEANING_TABLE_TEXTURE = new ResourceLocation(Arche.MOD_ID,"textures/gui/restore_button.png");
    private static Button restoreButton;


    public MuseumCatalogScreen(MuseumCatalogContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        initButton(screenContainer);
    }

    protected void initButton(MuseumCatalogContainer screenContainer){
        ITextComponent title = ITextComponent.getTextComponentOrEmpty("Restore");

    }

    @Override
    public void tick() {
        super.tick();
        /*RestoreTableTile table = (RestoreTableTile) this.container.tileEntity;
        IItemHandler handler = table.itemHandler;
        if(!(handler.getStackInSlot(4).getItem() instanceof ArcheArtifactBroken)){this.buttons.get(0).active = false;
            return;}
        ArcheArtifactBroken artifactBroken = (ArcheArtifactBroken) handler.getStackInSlot(4).getItem();
        Item comp1 = artifactBroken.comp1;
        Item comp2 = artifactBroken.comp2;
        Item comp3 = artifactBroken.comp3;
        Item comp4 = artifactBroken.comp4;
        drawComponentMaterials(comp1,comp2,comp3,comp4);*/
        //this.buttons.get(0).active = true;
    }


    /*public void drawComponentOverlay(MatrixStack matrixStack){
        RenderSystem.color4f(1f, 1f, 1f, 0.8f);
        this.minecraft.getTextureManager().bindTexture(compOverlay);
        int i = 26 + this.guiLeft;
        int j = 9 + this.guiTop;
        int ui = 42 + this.width;
        int uj = 9 + this.height;
        this.blit(matrixStack, i, j, ui, uj, 17, 72);
    }*/

    public void drawArtifacts(){
        this.itemRenderer.zLevel = 1;
        int bumpDown = 18;
        int startX = 8 + this.guiLeft;
        int startY = 9 + this.guiTop;
        MuseumCatalogTile tile = (MuseumCatalogTile) this.container.tileEntity;
        //tile.serializePedestalsNBT();
        ArcheArtifactItem[] artifactItemsList = ModItems.artifactItemsList;
        for (int i = 0; i < artifactItemsList.length; i++) {
            boolean completed = tile.artifactCompletion[i];
            ArcheArtifactItem artifact = artifactItemsList[i];
            this.renderArtifactItemIntoGUI(completed,new ItemStack(artifact, 1),startX + bumpDown*(i%9),startY+bumpDown*Math.round(i/9));
        }
    }

    private void renderArtifactItemIntoGUI(boolean completed, ItemStack stack, int x, int y) {
        int completeFloat = 0;
        if(completed) completeFloat = 1;
        int lighting = completeFloat*15728880;

        IBakedModel bakedmodel = this.itemRenderer.getItemModelWithOverrides(stack, (World)null, (LivingEntity)null);
        RenderSystem.pushMatrix();
        this.itemRenderer.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        this.itemRenderer.textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmapDirect(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef((float)x, (float)y, 100.0F + this.itemRenderer.zLevel);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        MatrixStack matrixstack = new MatrixStack();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        boolean flag = !bakedmodel.isSideLit();
        if (flag) {
            RenderHelper.setupGuiFlatDiffuseLighting();
        }

        this.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GUI, false, matrixstack, irendertypebuffer$impl, lighting, OverlayTexture.NO_OVERLAY, bakedmodel);
        irendertypebuffer$impl.finish();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupGui3DDiffuseLighting();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        this.drawArtifacts();
        //this.drawComponentOverlay(matrixStack);
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


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        initButton(this.container);
    }



}