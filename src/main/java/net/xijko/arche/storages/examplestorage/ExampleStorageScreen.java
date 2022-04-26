package net.xijko.arche.storages.examplestorage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.xijko.arche.Arche;

public class ExampleStorageScreen extends ContainerScreen<ExampleStorageContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Arche.MOD_ID,"textures/gui/example.png");
    public ExampleStorageScreen(ExampleStorageContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.width = 175;
        this.height = 183;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        if(this.minecraft == null){
            return;
        }
        RenderSystem.color4f(1f,1f,1f,1f);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int startX = (this.width - this.xSize)/2;
        int startY = (this.height - this.ySize)/2;
        this.blit(stack,startX,startY,0,0,this.xSize,this.ySize);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack,mouseX,mouseX); //different - see 34:15
    }
}
