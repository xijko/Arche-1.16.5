package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;
import net.xijko.arche.block.MuseumCatalogBlock;
import net.xijko.arche.container.MuseumCatalogContainer;
import net.xijko.arche.item.ArcheArtifactItem;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.MuseumCatalogResetMessage;
import net.xijko.arche.tileentities.MuseumCatalogTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MuseumCatalogScreen extends ContainerScreen<MuseumCatalogContainer> {
    private final ResourceLocation GUI = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/museum_catalog.png");

    private static final Logger LOGGER = LogManager.getLogger();
    private static Button resetButton;
    private static ITextComponent title = ITextComponent.getTextComponentOrEmpty("");


    public MuseumCatalogScreen(MuseumCatalogContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, title);
    }

    protected void initButton(MuseumCatalogContainer screenContainer){
        ITextComponent title = ITextComponent.getTextComponentOrEmpty("x");
        resetButton = new Button(this.guiLeft+7, this.guiTop+8, 18, 18, title, (button) -> {
            MuseumCatalogTile tile = (MuseumCatalogTile) screenContainer.tileEntity;
            tile.resetCatalog(tile.getWorld(),tile.getPos());
            ModNetwork.sendToServer(new MuseumCatalogResetMessage(tile.getWorld(),tile.getPos()));
        });
        this.addButton(resetButton);
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
        int tierStartX = 8 + this.guiLeft + bumpDown;
        int startX = 8 + this.guiLeft + bumpDown*2;
        int startY = 9 + this.guiTop;
        MuseumCatalogTile tile = (MuseumCatalogTile) this.container.tileEntity;
        //tile.serializePedestalsNBT();
        ArcheArtifactItem[] artifactItemsList = MuseumCatalogBlock.getArtifactItemList();

        ItemStack archeTier0 = new ItemStack(ModBlocks.DIRT_DEPOSIT.get(),1);
        ItemStack archeTier1 = new ItemStack(ModBlocks.STONE_DEPOSIT.get(),1);
        ItemStack archeTier2 = new ItemStack(ModBlocks.OBSIDIAN_DEPOSIT.get(),1);
        ItemStack archeTier3 = new ItemStack(ModBlocks.NETHERRACK_DEPOSIT.get(),1);
        ItemStack archeTier4 = new ItemStack(ModBlocks.ENDSTONE_DEPOSIT.get(),1);

        this.itemRenderer.renderItemIntoGUI(archeTier0,tierStartX,startY+bumpDown*0);
        this.itemRenderer.renderItemIntoGUI(archeTier1,tierStartX,startY+bumpDown*1);
        this.itemRenderer.renderItemIntoGUI(archeTier2,tierStartX,startY+bumpDown*2);
        this.itemRenderer.renderItemIntoGUI(archeTier3,tierStartX,startY+bumpDown*3);
        this.itemRenderer.renderItemIntoGUI(archeTier4,tierStartX,startY+bumpDown*4);

        int tiers[] = new int[5];

        for (int i = 0; i < artifactItemsList.length; i++) {
            boolean completed = tile.artifactCompletion[i];
            ArcheArtifactItem artifact = artifactItemsList[i];
            if (artifact != null) {
                int row = artifact.archeTier;
                int col = tiers[row];
                this.renderArtifactItemIntoGUI(completed, new ItemStack(artifact, 1), startX + bumpDown * col, startY + bumpDown * row);
                tiers[row]++;
            }
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
        int ySize = 184;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, ySize);
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

    protected void drawResetButton(){

    }

}