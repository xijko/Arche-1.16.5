Stream.of(
Block.makeCuboidShape(4, 0, 7, 5, 1, 8),
Block.makeCuboidShape(9, 0, 3, 12, 3, 6),
Block.makeCuboidShape(6, 0, 9, 8, 2, 11),
Block.makeCuboidShape(6, 0, 5, 8, 2, 7),
Block.makeCuboidShape(11, 0, 7, 14, 2, 10),
Block.makeCuboidShape(5, 0, 9, 7, 1, 12),
Block.makeCuboidShape(12, 2, 8, 13, 3, 9)
).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();