package net.xijko.arche.util.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.xijko.arche.block.DisplayPedestalBlock;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.tileentities.DisplayPedestalTile;
import net.xijko.arche.tileentities.MuseumCatalogTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;

public class DisplayPedestalTileRenderer  extends TileEntityRenderer<DisplayPedestalTile> {

        private Minecraft mc = Minecraft.getInstance();
    private static final Logger LOGGER = LogManager.getLogger();


    public DisplayPedestalTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
            super(rendererDispatcherIn);
        }

        private float getFacing(BlockState state){
            float angle = 180;

            switch (state.get(HORIZONTAL_FACING)) {
                case NORTH:
                    return angle;
                case EAST:
                    return angle-90;
                case SOUTH:
                    return angle-180;
                case WEST:
                    return angle+90;
                default:
                    return angle;
            }
        }

        @Override
        public void render(DisplayPedestalTile te, float partialTicks, MatrixStack matrixStackIn,
                           IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
            DisplayPedestalBlock block = (DisplayPedestalBlock) te.getBlockState().getBlock();


            ItemStack renderItemStack = te.getItem();

            if (renderItemStack.equals(ItemStack.EMPTY) || renderItemStack.getItem().equals(Items.AIR)){
                return;
            }

            ClientPlayerEntity player = mc.player;
            int lightLevel = getLightLevel(te.getWorld(), te.getPos().up());

            float angle = getFacing(te.getBlockState());
            Quaternion rotation = Vector3f.YP.rotationDegrees(angle);

            renderItem(renderItemStack, new double[] { 0.5d, 1.38d, 0.5d },
                    rotation, matrixStackIn, bufferIn, partialTicks,
                    combinedOverlayIn, lightLevel, 1f);

            ITextComponent label = renderItemStack.hasDisplayName() ? renderItemStack.getDisplayName()
                    : new TranslationTextComponent(renderItemStack.getTranslationKey());

            renderLabel(matrixStackIn, bufferIn, lightLevel, new double[] { .5d, 2d, .5d }, label, 0xffffff);
        }

        private void renderItem(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack,
                                IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
            matrixStack.push();
            matrixStack.translate(translation[0], translation[1], translation[2]);
            matrixStack.rotate(rotation);
            matrixStack.scale(scale, scale, scale);

            IBakedModel model = mc.getItemRenderer().getItemModelWithOverrides(stack,
                    mc.world, null);
            mc.getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, true, matrixStack, buffer,
                    lightLevel, combinedOverlay, model);
            matrixStack.pop();
        }

        private void renderLabel(MatrixStack stack, IRenderTypeBuffer buffer, int lightLevel, double[] corner,
                                 ITextComponent text, int color) {

            FontRenderer font = mc.fontRenderer;

            stack.push();
            float scale = 0.01f;
            int opacity = (int) (.4f * 255.0f) << 24;
            float offset = (float) (-font.getStringPropertyWidth(text) / 2);
            Matrix4f matrix = stack.getLast().getMatrix();

            stack.translate(corner[0], corner[1] + .4f, corner[2]);
            stack.scale(scale, scale, scale);
            stack.rotate(mc.getRenderManager().getCameraOrientation());
            stack.rotate(Vector3f.ZP.rotationDegrees(180f));

            font.func_243247_a(text, offset, 0, color, false, matrix, buffer, false, opacity, lightLevel);
            stack.pop();
        }

        private int getLightLevel(World world, BlockPos pos) {
            int bLight = world.getLightFor(LightType.BLOCK, pos);
            int sLight = world.getLightFor(LightType.SKY, pos);
            return LightTexture.packLight(bLight, sLight);
        }




    }
