package net.xijko.arche.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.container.DisplayPedestalContainer;
import net.xijko.arche.container.RestoreTableContainer;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.RestoreTableRestoreMessage;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import net.xijko.arche.tileentities.RestoreTableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DisplayPedestalScreen extends ContainerScreen<DisplayPedestalContainer> {
    private int tick = 0;

    private final ResourceLocation GUI = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/display_pedestal.png");

    private static final Logger LOGGER = LogManager.getLogger();


    private DisplayPedestalTile tile = (DisplayPedestalTile) this.container.tileEntity;


    public DisplayPedestalScreen(DisplayPedestalContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        //initButton(screenContainer);
    }

    public ItemRenderer getItemRenderer(){ return this.itemRenderer;}

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

    protected void drawArtifact(){
        int rendererX = this.xSize/2 + this.guiLeft;
        int rendererY = this.guiTop - 50 + this.ySize/2;

        IItemHandler handler = tile.itemHandler;
        ItemStack itemStack = handler.getStackInSlot(0);
        //this.itemRenderer.renderItemIntoGUI(new ItemStack(item),50,50);
        IBakedModel ibakedmodel = this.getItemRenderer().getItemModelWithOverrides(itemStack, tile.getWorld(), null);
        tile.renderArtifact(this,itemStack,rendererX,rendererY, ibakedmodel);
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.drawArtifact();
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        this.renderBackground(renderBGMatrixStack(matrixStack));
        //this.drawComponentMaterials();
        //this.drawComponentOverlay(matrixStack);
    }

    private MatrixStack renderBGMatrixStack(MatrixStack ms){
        MatrixStack newMS = new MatrixStack();
        newMS = ms;
        newMS.translate(0,0,-200F);
        newMS.rotate(new Quaternion(0,10,0,true));
        return newMS;
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
        //initButton(this.container);
    }



}