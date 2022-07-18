// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class custom_model extends EntityModel<Entity> {
	private final ModelRenderer head;

	public custom_model() {
		texWidth = 64;
		texHeight = 64;

		head = new ModelRenderer(this);
		head.setPos(0.0F, 33.0F, 0.0F);
		head.texOffs(0, 16).addBox(-4.5F, -10.0F, -4.5F, 9.0F, 2.0F, 9.0F, 0.0F, false);
		head.texOffs(27, 2).addBox(-5.0F, -9.5F, 2.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		head.texOffs(27, 0).addBox(-5.0F, -9.5F, -0.5F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		head.texOffs(27, 4).addBox(-5.0F, -9.5F, -3.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		head.texOffs(26, 17).addBox(2.0F, -9.5F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		head.texOffs(0, 27).addBox(-0.5F, -9.5F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		head.texOffs(27, 6).addBox(-3.0F, -9.5F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		head.texOffs(0, 0).addBox(-4.5F, -17.0F, -4.5F, 9.0F, 7.0F, 9.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}