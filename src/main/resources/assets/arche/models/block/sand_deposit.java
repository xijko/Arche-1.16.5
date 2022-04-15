Stream.of(
Block.makeCuboidShape(11, 12, 8, 12, 16, 12),
Block.makeCuboidShape(6, 12, 7, 12, 16, 8),
Block.makeCuboidShape(6, 12, 12, 12, 16, 13),
Block.makeCuboidShape(-2, 7, -1, 0, 11, 0),
Block.makeCuboidShape(11, 6, -2, 12, 12, 2),
Block.makeCuboidShape(6, 12, -2, 12, 13, 2),
Block.makeCuboidShape(12, 10, 7, 14, 11, 9),
Block.makeCuboidShape(11, 2, 4, 13, 3, 6),
Block.makeCuboidShape(16, 3, 9, 20, 4, 13),
Block.makeCuboidShape(-2, 13, 13, 2, 14, 17),
Block.makeCuboidShape(-4.454584813932538, 1.3400311718554114, 4, -3.454584813932538, 5.340031171855411, 8),
Block.makeCuboidShape(-4.4545848139325255, 1.3400311718554114, 8, 1.5454151860674745, 5.340031171855411, 9),
Block.makeCuboidShape(-4.4545848139325255, 1.3400311718554114, 3, 1.5454151860674745, 5.340031171855411, 4),
Block.makeCuboidShape(9, 2, 14, 14, 5, 17),
Block.makeCuboidShape(9, -1, 3, 14, 2, 6),
Block.makeCuboidShape(0, 0, 0, 16, 16, 16)
).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();