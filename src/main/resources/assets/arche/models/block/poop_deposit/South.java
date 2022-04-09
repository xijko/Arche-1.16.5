Stream.of(
Block.makeCuboidShape(7, 0, 11, 8, 1, 12),
Block.makeCuboidShape(3, 0, 4, 6, 3, 7),
Block.makeCuboidShape(9, 0, 8, 11, 2, 10),
Block.makeCuboidShape(5, 0, 8, 7, 2, 10),
Block.makeCuboidShape(7, 0, 2, 10, 2, 5),
Block.makeCuboidShape(9, 0, 9, 12, 1, 11),
Block.makeCuboidShape(8, 2, 3, 9, 3, 4)
).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();