Stream.of(
Block.makeCuboidShape(11, 0, 8, 12, 1, 9),
Block.makeCuboidShape(4, 0, 10, 7, 3, 13),
Block.makeCuboidShape(8, 0, 5, 10, 2, 7),
Block.makeCuboidShape(8, 0, 9, 10, 2, 11),
Block.makeCuboidShape(2, 0, 6, 5, 2, 9),
Block.makeCuboidShape(9, 0, 4, 11, 1, 7),
Block.makeCuboidShape(3, 2, 7, 4, 3, 8)
).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();