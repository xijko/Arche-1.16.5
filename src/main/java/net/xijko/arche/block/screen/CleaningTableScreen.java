package net.xijko.arche.block.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.xijko.arche.Arche;
import net.xijko.arche.container.CleaningTableContainer;
import net.xijko.arche.item.ArcheArtifactBroken;
import net.xijko.arche.network.CleaningTableRestoreMessage;
import net.xijko.arche.network.ModNetwork;
import net.xijko.arche.network.ToolBeltOpenMessage;
import net.xijko.arche.tileentities.CleaningTableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CleaningTableScreen extends ContainerScreen<CleaningTableContainer> {
    private final ResourceLocation GUI = new ResourceLocation(Arche.MOD_ID,
            "textures/gui/cleaning_table.png");

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation CLEANING_TABLE_TEXTURE = new ResourceLocation(Arche.MOD_ID,"textures/gui/restore_button.png");
    private static Button restoreButton;


    public CleaningTableScreen(CleaningTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        initButton(screenContainer);
    }

    protected void initButton(CleaningTableContainer screenContainer){
        ITextComponent title = ITextComponent.getTextComponentOrEmpty("Restore");
        restoreButton = new Button(this.guiLeft+61, this.guiTop+50, 55, 20, title, (button) -> {
            CleaningTableTile table = (CleaningTableTile) screenContainer.tileEntity;
            ModNetwork.sendToServer(new CleaningTableRestoreMessage(this,table.getWorld(),table.getPos()));
        });
        this.addButton(restoreButton);
    }

    @Override
    public void tick() {
        super.tick();
        CleaningTableTile table = (CleaningTableTile) this.container.tileEntity;
        IItemHandler handler = table.itemHandler;
        if(!(handler.getStackInSlot(4).getItem() instanceof ArcheArtifactBroken)){this.buttons.get(0).active = false;
            return;}
        ArcheArtifactBroken artifactBroken = (ArcheArtifactBroken) handler.getStackInSlot(4).getItem();
        Item comp1 = artifactBroken.comp1;
        Item comp2 = artifactBroken.comp2;
        Item comp3 = artifactBroken.comp3;
        Item comp4 = artifactBroken.comp4;
        drawComponentMaterials(comp1,comp2,comp3,comp4);
        this.buttons.get(0).active = true;
    }

    public void drawComponentMaterials(Item A, Item B, Item C, Item D){
        this.itemRenderer.zLevel = 300;
        int bumpDown = 18;
        int startX = 8;
        int startY = 9;
        if(A!=null){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(A, 1),startX,startY);
        }
        if(B!=null){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(B, 1),startX,startY+bumpDown);
        }
        if(C!=null){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(C, 1),startX,startY+bumpDown*2);
        }
        if(D!=null){
            this.itemRenderer.renderItemIntoGUI(new ItemStack(D, 1),startX,startY+bumpDown*3);
        }
    }



    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        if(container.isLightningStorm()) {
            this.blit(matrixStack, i + 82, j + 9, 176, 0, 13, 17);
        }
    }


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        initButton(this.container);
    }



}