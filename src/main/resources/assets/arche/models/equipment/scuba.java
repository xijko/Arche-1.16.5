// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


public class custom_model extends EntityModel<Entity> {
	private final ModelRenderer back;
	private final ModelRenderer head;

	public custom_model() {
		textureWidth = 64;
		textureHeight = 64;

		back = new ModelRenderer(this);
		back.setRotationPoint(0.0F, 11.0F, 0.0F);
		back.setTextureOffset(0, 6).addBox(-2.5F, -13.5F, 5.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		back.setTextureOffset(0, 0).addBox(-4.0F, -11.0F, 2.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		back.setTextureOffset(0, 26).addBox(-4.5F, -5.5F, 1.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
		back.setTextureOffset(27, 7).addBox(-4.5F, -9.5F, 1.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
		back.setTextureOffset(24, 0).addBox(-4.5F, -3.5F, -2.5F, 9.0F, 1.0F, 5.0F, 0.0F, false);
		back.setTextureOffset(52, 28).addBox(-2.5F, -11.5F, -2.5F, 1.0F, 8.0F, 5.0F, 0.0F, false);
		back.setTextureOffset(40, 28).addBox(1.5F, -11.5F, -2.5F, 1.0F, 8.0F, 5.0F, 0.0F, false);
		back.setTextureOffset(0, 0).addBox(-1.5F, -9.5F, -2.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		back.setTextureOffset(0, 16).addBox(-1.0F, -9.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		back.setTextureOffset(0, 3).addBox(-3.0F, -11.5F, 5.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
		back.setTextureOffset(5, 17).addBox(-2.5F, -12.5F, 5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.setTextureOffset(12, 37).addBox(-0.5F, -13.5F, 0.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 54.0F, 0.0F);
		head.setTextureOffset(28, 46).addBox(-4.5F, -19.5F, -4.5F, 9.0F, 9.0F, 9.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		back.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}