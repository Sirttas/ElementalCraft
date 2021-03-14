package sirttas.elementalcraft.block.shrine.sweet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.Shapes;
import sirttas.elementalcraft.block.shrine.AbstractBlockShrine;

public class BlockSweetShrine extends AbstractBlockShrine {

	public static final String NAME = "sweetshrine";

	private static final VoxelShape BASE = Block.makeCuboidShape(5D, 12D, 5D, 11D, 14D, 11D);

	private static final VoxelShape IRON_NORTH_1 = Block.makeCuboidShape(4D, 9D, 1D, 6D, 11D, 3D);
	private static final VoxelShape IRON_NORTH_2 = Block.makeCuboidShape(10D, 9D, 1D, 12D, 11D, 3D);
	private static final VoxelShape IRON_SOUTH_1 = Block.makeCuboidShape(4D, 9D, 13D, 6D, 11D, 15D);
	private static final VoxelShape IRON_SOUTH_2 = Block.makeCuboidShape(10D, 9D, 13D, 12D, 11D, 15D);
	private static final VoxelShape IRON_WEST_1 = Block.makeCuboidShape(1D, 9D, 4D, 3D, 11D, 6D);
	private static final VoxelShape IRON_WEST_2 = Block.makeCuboidShape(1D, 9D, 10D, 3D, 11D, 12D);
	private static final VoxelShape IRON_EAST_1 = Block.makeCuboidShape(13D, 9D, 4D, 15D, 11D, 6D);
	private static final VoxelShape IRON_EAST_2 = Block.makeCuboidShape(13D, 9D, 10D, 15D, 11D, 12D);

	private static final VoxelShape SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, BASE, IRON_NORTH_1, IRON_NORTH_2, IRON_SOUTH_1,
			IRON_SOUTH_2, IRON_EAST_1, IRON_EAST_2, IRON_WEST_1, IRON_WEST_2);

	public BlockSweetShrine() {
		super(ElementType.WATER);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileSweetShrine();
	}


	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}