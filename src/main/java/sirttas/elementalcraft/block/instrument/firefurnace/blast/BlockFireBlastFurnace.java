package sirttas.elementalcraft.block.instrument.firefurnace.blast;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.block.instrument.firefurnace.AbstractBlockFireFurnace;

public class BlockFireBlastFurnace extends AbstractBlockFireFurnace {

	public static final String NAME = "fireblastfurnace";

	private static final VoxelShape OVEN_SLAB = Block.makeCuboidShape(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.makeCuboidShape(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape OVEN_GLASS = Block.makeCuboidShape(2D, 4D, 2D, 14D, 10D, 14D);
	private static final VoxelShape TOP_BOWL = Block.makeCuboidShape(3D, 11D, 3D, 13D, 12D, 13D);
	private static final VoxelShape OVEN = VoxelShapes.combineAndSimplify(VoxelShapes.or(OVEN_SLAB, OVEN_SLAB_2, OVEN_GLASS), TOP_BOWL, IBooleanFunction.ONLY_FIRST);
	private static final VoxelShape CONNECTION = Block.makeCuboidShape(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape PILLAT_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape PILLAT_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape PILLAT_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape SHAPE = VoxelShapes.or(OVEN, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFireBlastFurnace();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
