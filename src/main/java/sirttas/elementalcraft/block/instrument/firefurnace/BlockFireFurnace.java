package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockFireFurnace extends AbstractBlockFireFurnace {

	public static final String NAME = "firefurnace";

	private static final VoxelShape OVEN_SLAB = Block.makeCuboidShape(0D, 2D, 0D, 16D, 12D, 16D);
	private static final VoxelShape TOP_BOWL = Block.makeCuboidShape(3D, 11D, 3D, 13D, 12D, 13D);
	private static final VoxelShape MIDDLE_1 = Block.makeCuboidShape(0D, 4D, 3D, 16D, 10D, 13D);
	private static final VoxelShape MIDDLE_2 = Block.makeCuboidShape(3D, 4D, 0D, 13D, 10D, 16D);
	private static final VoxelShape EMPTY_SPACE = VoxelShapes.or(TOP_BOWL, MIDDLE_1, MIDDLE_2);
	private static final VoxelShape OVEN = VoxelShapes.combineAndSimplify(OVEN_SLAB, EMPTY_SPACE, IBooleanFunction.ONLY_FIRST);
	private static final VoxelShape CONNECTION = Block.makeCuboidShape(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 2D, 3D);
	private static final VoxelShape PILLAT_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 2D, 3D);
	private static final VoxelShape PILLAT_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 2D, 15D);
	private static final VoxelShape PILLAT_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 2D, 15D);
	private static final VoxelShape SHAPE = VoxelShapes.or(OVEN, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFireFurnace();
	}


	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
