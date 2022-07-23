Stream.of(
Block.box(1, 13, 1, 15, 32, 15),
Block.box(0, 11, 0, 16, 13, 16),
Block.box(1, 0, 1, 15, 4, 15),
Block.box(2, 4, 2, 14, 11, 14),
Block.box(2, 13, 2, 14, 14, 14)
).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();