Stream.of(
Block.makeCuboidShape(8, 0, 4, 9, 1, 5),
Block.makeCuboidShape(10, 0, 9, 13, 3, 12),
Block.makeCuboidShape(5, 0, 6, 7, 2, 8),
Block.makeCuboidShape(9, 0, 6, 11, 2, 8),
Block.makeCuboidShape(6, 0, 11, 9, 2, 14),
Block.makeCuboidShape(4, 0, 5, 7, 1, 7),
Block.makeCuboidShape(7, 2, 12, 8, 3, 13)
).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();