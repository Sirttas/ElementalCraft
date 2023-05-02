package sirttas.elementalcraft.block.shape;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ShapeHelper {

	private ShapeHelper() {}

	public static boolean hitCollideWithShape(VoxelShape shape, BlockPos offset, BlockHitResult hit) {
		return vectorCollideWithShape(shape, Vec3.atBottomCenterOf(offset), hit.getLocation());
	}

	public static boolean vectorCollideWithShape(VoxelShape shape, BlockPos offset, Vec3 vector) {
		return vectorCollideWithShape(shape, Vec3.atLowerCornerOf(offset), vector);
	}
	
	public static boolean vectorCollideWithShape(VoxelShape shape, Vec3 offset, Vec3 vector) {
		return shape.toAabbs().stream().anyMatch(a -> vectorCollideWithBoundingBox(a.move(offset), vector));
	}
	
	public static boolean vectorCollideWithBoundingBox(AABB box, Vec3 vector) {
		return vector.x >= box.minX && vector.x <= box.maxX && vector.y >= box.minY && vector.y <= box.maxY && vector.z >= box.minZ && vector.z <= box.maxZ;
	}

	public static Map<Direction, VoxelShape> directionShapes(VoxelShape shape) {
		return directionShapes(Direction.UP, shape);
	}

	public static Map<Direction, VoxelShape> directionShapes(Direction from, VoxelShape shape) {
		return Maps.immutableEnumMap(Map.of(
				Direction.UP, rotateShape(from, Direction.UP, shape),
				Direction.DOWN, rotateShape(from, Direction.DOWN, shape),
				Direction.NORTH, rotateShape(from, Direction.NORTH, shape),
				Direction.SOUTH, rotateShape(from, Direction.SOUTH, shape),
				Direction.WEST, rotateShape(from, Direction.WEST, shape),
				Direction.EAST, rotateShape(from, Direction.EAST, shape)
		));
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		if (from == to) {
			return shape;
		}

		var result = new AtomicReference<>(Shapes.empty());

		shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> result.set(Shapes.or(result.get(), rotateAABB(from, to, shape, minX, minY, minZ, maxX, maxY, maxZ))));
		return result.get();
	}

	private static VoxelShape rotateAABB(Direction from, Direction to, VoxelShape shape, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return switch (from) {
			case UP -> switch (to) {
				case DOWN -> Shapes.box(minX, 1 - maxY, minZ, maxX, 1 - minY, maxZ);
				case NORTH -> Shapes.box(minX, minZ, 1 - maxY, maxX, maxZ, 1 - minY);
				case SOUTH -> Shapes.box(minX, minZ, minY, maxX, maxZ, maxY);
				case WEST -> Shapes.box(1 - maxY, minX, minZ, 1 - minY, maxX, maxZ);
				case EAST -> Shapes.box(minY, minX, minZ, maxY, maxX, maxZ);
				default -> shape;
			};
			case DOWN -> switch (to) {
				case UP -> Shapes.box(minX, 1 - maxY, minZ, maxX, 1 - minY, maxZ);
				case NORTH -> Shapes.box(minX, minZ, minY, maxX, maxZ, maxY);
				case SOUTH -> Shapes.box(minX, minZ, 1 - maxY, maxX, maxZ, 1 - minY);
				case WEST -> Shapes.box(minY, minX, minZ, maxY, maxX, maxZ);
				case EAST -> Shapes.box(1 - maxY, minX, minZ, 1 - minY, maxX, maxZ);
				default -> shape;
			};
			case NORTH -> switch (to) {
				case UP -> Shapes.box(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY);
				case DOWN -> Shapes.box(minX, minZ, minY, maxX, maxZ, maxY);
				case SOUTH -> Shapes.box(minX, minY, 1 - maxZ, maxX, maxY, 1 - minZ);
				case WEST -> Shapes.box(minZ, minY, minX, maxZ, maxY, maxX);
				case EAST -> Shapes.box(1 - maxZ, minY,  minX, 1 - minZ, maxY, maxX);
				default -> shape;
			};
			case SOUTH -> switch (to) {
				case UP -> Shapes.box(minX, minZ, 1 - maxY, maxX, maxZ, 1 - minY);
				case DOWN -> Shapes.box(minX, minZ, minY, maxX, maxZ, maxY);
				case NORTH -> Shapes.box(minX, 1 - maxY, minZ, maxX, 1 - minY, maxZ);
				case WEST -> Shapes.box(minY, minX, minZ, maxY, maxX, maxZ);
				case EAST -> Shapes.box(1 - maxY, minX, minZ, 1 - minY, maxX, maxZ);
				default -> shape;
			};
			case WEST -> switch (to) {
				case UP -> Shapes.box(minZ, minX, minY, maxZ, maxX, maxY);
				case DOWN -> Shapes.box(minZ, minX, 1 - maxY, maxZ, maxX, 1 - minY);
				case NORTH -> Shapes.box(minY, minX, minZ, maxY, maxX, maxZ);
				case SOUTH -> Shapes.box(1 - maxY, minX, minZ, 1 - minY, maxX, maxZ);
				case EAST -> Shapes.box(minX, minZ, 1 - maxY, maxX, maxZ, 1 - minY);
				default -> shape;
			};
			case EAST -> switch (to) {
				case UP -> Shapes.box(minZ, minX, 1 - maxY, maxZ, maxX, 1 - minY);
				case DOWN -> Shapes.box(minZ, minX, minY, maxZ, maxX, maxY);
				case NORTH -> Shapes.box(1 - maxY, minX, minZ, 1 - minY, maxX, maxZ);
				case SOUTH -> Shapes.box(minY, minX, minZ, maxY, maxX, maxZ);
				case WEST -> Shapes.box(minX, minZ, minY, maxX, maxZ, maxY);
				default -> shape;
			};
		};
	}
}
