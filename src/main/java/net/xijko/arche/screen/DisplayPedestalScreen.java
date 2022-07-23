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
            "textures/gui/restore_table.png");
    private final ResourceLocation compOverlay = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/restore_table_overlay.png");

    private static final Logger LOGGER = LogManager.getLogger();

    private static Button restoreButton;

    private DisplayPedestalTile tile = (DisplayPedestalTile) this.container.tileEntity;


    public DisplayPedestalScreen(DisplayPedestalContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        //initButton(screenContainer);
    }

    protected void initButton(RestoreTableContainer screenContainer){
        ITextComponent title = ITextComponent.getTextComponentOrEmpty("Restore");
        restoreButton = new Button(this.guiLeft+61, this.guiTop+60, 55, 20, title, (button) -> {
            RestoreTableTile table = (RestoreTableTile) screenContainer.tileEntity;
            //ModNetwork.sendToServer(new RestoreTableRestoreMessage(this,table.getWorld(),table.getPos()));
        });
        this.addButton(restoreButton);
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
        tile.renderArtifact(this,itemStack,20,20, ibakedmodel);
        //renderItem(item,20,tick,Minecraft.getInstance().currentScreen.);
        //renderItem(item, new double[] { 0.5d, 1d, 0.5d },Vector3f.YP.rotationDegrees(180f - tick), matrixStackIn, bufferIn, partialTicks,combinedOverlayIn, lightLevel, 0.8f);
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

    protected void drawArtifact(){
        IItemHandler handler = tile.itemHandler;
        ItemStack itemStack = handler.getStackInSlot(0);
        //this.itemRenderer.renderItemIntoGUI(new ItemStack(item),50,50);
        IBakedModel ibakedmodel = this.getItemRenderer().getItemModelWithOverrides(itemStack, tile.getWorld(), null);
        tile.renderArtifact(this,itemStack,80,80, ibakedmodel);
    }

    public void drawComponentMaterials(){
        this.itemRenderer.zLevel = 1;
        int bumpDown = 18;
        int startX = 26 + this.guiLeft;
        int startY = 9 + this.guiTop;
        RestoreTableTile table = (RestoreTableTile) this.container.tileEntity;
        IItemHandler handler = table.itemHandler;
        if(!(handler.getStackInSlot(4).getItem() instanceof ArcheArtifactBroken)){this.buttons.get(0).active = false;
            return;}
        ItemStack artifactBrokenStack = handler.getStackInSlot(4);
        ArcheArtifactBroken artifactBroken = (ArcheArtifactBroken) artifactBrokenStack.getItem();
        Item comp1 = artifactBroken.comp1;
        Item comp2 = artifactBroken.comp2;
        Item comp3 = artifactBroken.comp3;
        Item comp4 = artifactBroken.comp4;
        int q1 = artifactBrokenStack.getTag().getInt("comp1quant");
        int q2 = artifactBrokenStack.getTag().getInt("comp2quant");
        int q3 = artifactBrokenStack.getTag().getInt("comp3quant");
        int q4 = artifactBrokenStack.getTag().getInt("comp4quant");
        if(comp1!=null && q1 > 0){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(comp1, q1),startX,startY);
        }
        if(comp2!=null && q2 > 0){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(comp2, q2),startX,startY+bumpDown);
        }
        if(comp3!=null && q3 > 0){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(comp3, q3),startX,startY+bumpDown*2);
        }
        if(comp4!=null && q4 > 0){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(comp4, q4),startX,startY+bumpDown*3);
        }
        this.buttons.get(0).active = true;
    }

    private void renderItem(ItemStack stack, double[] translation, int tix,
                            IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {

        Quaternion rotation = new Quaternion(Vector3f.YP.rotationDegrees(180f - tix));

        MatrixStack matrixStack = new MatrixStack();

        Minecraft mc = Minecraft.getInstance();
        matrixStack.push();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.rotate(rotation);
        matrixStack.scale(scale, scale, scale);

        BlockState blockState = new BlockState(Blocks.STONE, null, null);

        mc.getBlockRendererDispatcher().renderBlock(blockState,matrixStack,buffer,lightLevel,combinedOverlay);
        //mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer, lightLevel, combinedOverlay, model);
        matrixStack.pop();
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
        this.drawArtifact();
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
        //initButton(this.container);
    }



}