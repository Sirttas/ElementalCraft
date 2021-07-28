package sirttas.elementalcraft.block.shape;

import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;

public class ShapeHelper {

	private ShapeHelper() {}
	
	public static boolean vectorCollideWithShape(VoxelShape shape, BlockPos offset, Vec3 vector) {
		return vectorCollideWithBoundingBox(shape.bounds().move(offset), vector);
	}
	
	public static boolean vectorCollideWithShape(VoxelShape shape, Vec3 offset, Vec3 vector) {
		return vectorCollideWithBoundingBox(shape.bounds().move(offset), vector);
	}
	
	public static boolean vectorCollideWithBoundingBox(AABB box, Vec3 vector) {
		return vector.x >= box.minX && vector.x <= box.maxX && vector.y >= box.minY && vector.y <= box.maxY && vector.z >= box.minZ && vector.z <= box.maxZ;
	} 
}
