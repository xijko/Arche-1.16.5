package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.container.MuseumCatalogContainer;
import net.xijko.arche.container.RestoreTableContainer;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.RestoreTableRestoreMessage;
import net.xijko.arche.tileentities.MuseumCatalogTile;
import net.xijko.arche.tileentities.RestoreTableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        restoreButton = new Button(this.guiLeft+61, this.guiTop+60, 55, 20, title, (button) -> {
            MuseumCatalogTile table = (MuseumCatalogTile) screenContainer.tileEntity;
        });
        this.addButton(restoreButton);
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


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        //this.drawComponentMaterials();
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