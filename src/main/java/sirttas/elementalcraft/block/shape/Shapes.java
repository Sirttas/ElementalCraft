package sirttas.elementalcraft.block.shape;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class Shapes {

	private static final VoxelShape RS_SOURCE_NORTH = VoxelShapes.or(Block.makeCuboidShape(4D, 4D, 0D, 12D, 12D, 2D), Block.makeCuboidShape(6D, 6D, 2D, 10D, 10D, 5D));
	private static final VoxelShape RS_SOURCE_SOUTH = VoxelShapes.or(Block.makeCuboidShape(4D, 4D, 14D, 12D, 12D, 16D), Block.makeCuboidShape(6D, 6D, 11D, 10D, 10D, 14D));
	private static final VoxelShape RS_SOURCE_WEST = VoxelShapes.or(Block.makeCuboidShape(0D, 4D, 4D, 2D, 12D, 12D), Block.makeCuboidShape(2D, 6D, 6D, 5D, 10D, 10D));
	private static final VoxelShape RS_SOURCE_EAST = VoxelShapes.or(Block.makeCuboidShape(14D, 4D, 4D, 16D, 12D, 12D), Block.makeCuboidShape(11D, 6D, 6D, 14D, 10D, 10D));
	private static final VoxelShape RS_SOURCE_DOWN = VoxelShapes.or(Block.makeCuboidShape(4D, 0D, 4D, 12D, 2D, 12D), Block.makeCuboidShape(6D, 2D, 6D, 10D, 5D, 10D));
	private static final VoxelShape RS_SOURCE_UP = VoxelShapes.or(Block.makeCuboidShape(4D, 14D, 4D, 12D, 16D, 12D), Block.makeCuboidShape(6D, 11D, 6D, 10D, 14D, 10D));
	
	private static final VoxelShape RS_TARGET_NORTH = VoxelShapes.or(Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D), Block.makeCuboidShape(6D, 6D, 3D, 10D, 10D, 5D));
	private static final VoxelShape RS_TARGET_SOUTH = VoxelShapes.or(Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D), Block.makeCuboidShape(6D, 6D, 11D, 10D, 10D, 13D));
	private static final VoxelShape RS_TARGET_WEST = VoxelShapes.or(Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D), Block.makeCuboidShape(3D, 6D, 6D, 5D, 10D, 10D));
	private static final VoxelShape RS_TARGET_EAST = VoxelShapes.or(Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D), Block.makeCuboidShape(11D, 6D, 6D, 13D, 10D, 10D));
	private static final VoxelShape RS_TARGET_DOWN = VoxelShapes.or(Block.makeCuboidShape(7D, 0D, 7D, 9D, 3D, 9D), Block.makeCuboidShape(6D, 3D, 6D, 10D, 5D, 10D));
	private static final VoxelShape RS_TARGET_UP = VoxelShapes.or(Block.makeCuboidShape(7D, 13D, 7D, 9D, 16D, 9D), Block.makeCuboidShape(6D, 11D, 6D, 10D, 13D, 10D));
	
	public static final List<VoxelShape> SOURCE_SHAPES = ImmutableList.of(RS_SOURCE_NORTH, RS_SOURCE_SOUTH, RS_SOURCE_WEST, RS_SOURCE_EAST, RS_SOURCE_DOWN, RS_SOURCE_UP);
	public static final List<VoxelShape> TARGET_SHAPES = ImmutableList.of(RS_TARGET_NORTH, RS_TARGET_SOUTH, RS_TARGET_WEST, RS_TARGET_EAST, RS_TARGET_DOWN, RS_TARGET_UP);
	
	private static final VoxelShape SHRINE_BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape SHRINE_BASE_2 = Block.makeCuboidShape(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape SHRINE_BASE_3 = Block.makeCuboidShape(3D, 7D, 3D, 13D, 12D, 13D);

	private static final VoxelShape SHRINE_PIPE_UP = Block.makeCuboidShape(7D, 12D, 7D, 9D, 16D, 9D);
	private static final VoxelShape SHRINE_PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape SHRINE_PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape SHRINE_PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape SHRINE_PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);
	
	public static final VoxelShape SHRINE_SHAPE = VoxelShapes.or(SHRINE_BASE_1, SHRINE_BASE_2, SHRINE_BASE_3, SHRINE_PIPE_UP, SHRINE_PIPE_NORTH, SHRINE_PIPE_SOUTH, SHRINE_PIPE_EAST, SHRINE_PIPE_WEST);

	private Shapes() {}
	
	public static VoxelShape sourceShape(Direction direction) {
		switch (direction) {
		case EAST:
			return RS_SOURCE_EAST;
		case NORTH:
			return RS_SOURCE_NORTH;
		case SOUTH:
			return RS_SOURCE_SOUTH;
		case WEST:
			return RS_SOURCE_WEST;
		case DOWN:
			return RS_SOURCE_DOWN;
		default:
			return RS_SOURCE_UP;
		}
	}

	public static VoxelShape targetShape(Direction direction) {
		switch (direction) {
		case EAST:
			return RS_TARGET_EAST;
		case NORTH:
			return RS_TARGET_NORTH;
		case SOUTH:
			return RS_TARGET_SOUTH;
		case WEST:
			return RS_TARGET_WEST;
		case DOWN:
			return RS_TARGET_DOWN;
		default:
			return RS_TARGET_UP;
		}
	}
}
