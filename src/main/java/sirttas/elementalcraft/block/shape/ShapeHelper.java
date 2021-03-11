package sirttas.elementalcraft.block.shape;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;

public class ShapeHelper {

	private ShapeHelper() {}
	
	public static boolean vectorCollideWithShape(VoxelShape shape, BlockPos offset, Vector3d vector) {
		return vectorCollideWithBoundingBox(shape.getBoundingBox().offset(offset), vector);
	}
	
	public static boolean vectorCollideWithShape(VoxelShape shape, Vector3d offset, Vector3d vector) {
		return vectorCollideWithBoundingBox(shape.getBoundingBox().offset(offset), vector);
	}
	
	public static boolean vectorCollideWithBoundingBox(AxisAlignedBB box, Vector3d vector) {
		return vector.x >= box.minX && vector.x <= box.maxX && vector.y >= box.minY && vector.y <= box.maxY && vector.z >= box.minZ && vector.z <= box.maxZ;
	} 
}
