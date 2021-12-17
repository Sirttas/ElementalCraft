package sirttas.elementalcraft.block.shape;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ECShapes {

	private static final VoxelShape RS_SOURCE_NORTH = Shapes.or(Block.box(4D, 4D, 0D, 12D, 12D, 2D), Block.box(6D, 6D, 2D, 10D, 10D, 5D));
	private static final VoxelShape RS_SOURCE_SOUTH = Shapes.or(Block.box(4D, 4D, 14D, 12D, 12D, 16D), Block.box(6D, 6D, 11D, 10D, 10D, 14D));
	private static final VoxelShape RS_SOURCE_WEST = Shapes.or(Block.box(0D, 4D, 4D, 2D, 12D, 12D), Block.box(2D, 6D, 6D, 5D, 10D, 10D));
	private static final VoxelShape RS_SOURCE_EAST = Shapes.or(Block.box(14D, 4D, 4D, 16D, 12D, 12D), Block.box(11D, 6D, 6D, 14D, 10D, 10D));
	private static final VoxelShape RS_SOURCE_DOWN = Shapes.or(Block.box(4D, 0D, 4D, 12D, 2D, 12D), Block.box(6D, 2D, 6D, 10D, 5D, 10D));
	private static final VoxelShape RS_SOURCE_UP = Shapes.or(Block.box(4D, 14D, 4D, 12D, 16D, 12D), Block.box(6D, 11D, 6D, 10D, 14D, 10D));
	
	private static final VoxelShape RS_TARGET_NORTH = Shapes.or(Block.box(7D, 7D, 0D, 9D, 9D, 3D), Block.box(6D, 6D, 3D, 10D, 10D, 5D));
	private static final VoxelShape RS_TARGET_SOUTH = Shapes.or(Block.box(7D, 7D, 13D, 9D, 9D, 16D), Block.box(6D, 6D, 11D, 10D, 10D, 13D));
	private static final VoxelShape RS_TARGET_WEST = Shapes.or(Block.box(0D, 7D, 7D, 3D, 9D, 9D), Block.box(3D, 6D, 6D, 5D, 10D, 10D));
	private static final VoxelShape RS_TARGET_EAST = Shapes.or(Block.box(13D, 7D, 7D, 16D, 9D, 9D), Block.box(11D, 6D, 6D, 13D, 10D, 10D));
	private static final VoxelShape RS_TARGET_DOWN = Shapes.or(Block.box(7D, 0D, 7D, 9D, 3D, 9D), Block.box(6D, 3D, 6D, 10D, 5D, 10D));
	private static final VoxelShape RS_TARGET_UP = Shapes.or(Block.box(7D, 13D, 7D, 9D, 16D, 9D), Block.box(6D, 11D, 6D, 10D, 13D, 10D));
	
	public static final List<VoxelShape> SOURCE_SHAPES = List.of(RS_SOURCE_NORTH, RS_SOURCE_SOUTH, RS_SOURCE_WEST, RS_SOURCE_EAST, RS_SOURCE_DOWN, RS_SOURCE_UP);
	public static final List<VoxelShape> TARGET_SHAPES = List.of(RS_TARGET_NORTH, RS_TARGET_SOUTH, RS_TARGET_WEST, RS_TARGET_EAST, RS_TARGET_DOWN, RS_TARGET_UP);
	
	private static final VoxelShape SHRINE_BASE_1 = Block.box(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape SHRINE_BASE_2 = Block.box(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape SHRINE_BASE_3 = Block.box(3D, 7D, 3D, 13D, 12D, 13D);

	private static final VoxelShape SHRINE_PIPE_UP = Block.box(7D, 12D, 7D, 9D, 16D, 9D);
	private static final VoxelShape SHRINE_PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape SHRINE_PIPE_SOUTH = Block.box(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape SHRINE_PIPE_EAST = Block.box(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape SHRINE_PIPE_WEST = Block.box(0D, 7D, 7D, 3D, 9D, 9D);
	
	public static final VoxelShape SHRINE_SHAPE = Shapes.or(SHRINE_BASE_1, SHRINE_BASE_2, SHRINE_BASE_3, SHRINE_PIPE_UP, SHRINE_PIPE_NORTH, SHRINE_PIPE_SOUTH, SHRINE_PIPE_EAST, SHRINE_PIPE_WEST);

	private ECShapes() {}
	
	public static VoxelShape sourceShape(Direction direction) {
		return switch (direction) {
			case EAST -> RS_SOURCE_EAST;
			case NORTH -> RS_SOURCE_NORTH;
			case SOUTH -> RS_SOURCE_SOUTH;
			case WEST -> RS_SOURCE_WEST;
			case DOWN -> RS_SOURCE_DOWN;
			default -> RS_SOURCE_UP;
		};
	}

	public static VoxelShape targetShape(Direction direction) {
		return switch (direction) {
			case EAST -> RS_TARGET_EAST;
			case NORTH -> RS_TARGET_NORTH;
			case SOUTH -> RS_TARGET_SOUTH;
			case WEST -> RS_TARGET_WEST;
			case DOWN -> RS_TARGET_DOWN;
			default -> RS_TARGET_UP;
		};
	}
}
