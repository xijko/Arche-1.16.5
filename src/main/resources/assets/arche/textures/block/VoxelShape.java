Stream.of(
Block.makeCuboidShape(2, 14, 13, 6, 22, 13),
Block.makeCuboidShape(1, 0, 1, 5, 5, 5),
Block.makeCuboidShape(11, 0, 1, 15, 5, 5),
Block.makeCuboidShape(1, 0, 11, 5, 5, 15),
Block.makeCuboidShape(11, 0, 11, 15, 5, 15),
Block.makeCuboidShape(0, 5, 0, 16, 13, 16),
Block.makeCuboidShape(2, 2, 3, 3, 4, 13),
Block.makeCuboidShape(3, 2, 3, 4, 4, 13),
Block.makeCuboidShape(13, 2, 3, 14, 4, 13),
Block.makeCuboidShape(12, 2, 3, 13, 4, 13),
Block.makeCuboidShape(4, 2, 7, 12, 3, 9),
Block.makeCuboidShape(1, 12.5, 0.5, 15, 13.5, 15.5),
Block.makeCuboidShape(2, 13, 11, 3, 17, 15),
Block.makeCuboidShape(5, 13, 11, 6, 17, 15),
Block.makeCuboidShape(3, 13, 11, 5, 17, 12),
Block.makeCuboidShape(3, 13, 14, 5, 17, 15)
).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();