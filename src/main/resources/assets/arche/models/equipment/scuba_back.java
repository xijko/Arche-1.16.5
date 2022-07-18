// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class custom_model extends EntityModel<Entity> {
	private final ModelRenderer back;
	private final ModelRenderer head;

	public custom_model() {
		texWidth = 64;
		texHeight = 64;

		back = new ModelRenderer(this);
		back.setPos(0.0F, 24.0F, 0.0F);
		back.texOffs(0, 6).addBox(-2.5F, -11.5F, 5.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		back.texOffs(0, 0).addBox(-4.0F, -9.0F, 2.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		back.texOffs(0, 26).addBox(-4.5F, -3.5F, 1.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
		back.texOffs(27, 7).addBox(-4.5F, -7.5F, 1.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
		back.texOffs(24, 0).addBox(-4.5F, -1.5F, -2.5F, 9.0F, 1.0F, 5.0F, 0.0F, false);
		back.texOffs(34, 39).addBox(-2.5F, -9.5F, -2.5F, 1.0F, 8.0F, 5.0F, 0.0F, false);
		back.texOffs(22, 39).addBox(1.5F, -9.5F, -2.5F, 1.0F, 8.0F, 5.0F, 0.0F, false);
		back.texOffs(0, 0).addBox(-1.5F, -7.5F, -2.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		back.texOffs(0, 16).addBox(-1.0F, -7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		back.texOffs(0, 3).addBox(-3.0F, -9.5F, 5.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
		back.texOffs(5, 17).addBox(-2.5F, -10.5F, 5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.texOffs(12, 37).addBox(-0.5F, -11.5F, 0.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 24.0F, 0.0F);
		head.texOffs(28, 18).addBox(-4.5F, -10.0F, -4.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
		head.texOffs(0, 47).addBox(-4.5F, -18.0F, -4.5F, 9.0F, 8.0F, 9.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		back.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}