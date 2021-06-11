package sirttas.elementalcraft.block.sorter;

import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shape.Shapes;

public interface ISorterBlock {

	public static final DirectionProperty SOURCE = DirectionProperty.create("source", Direction.values());
	public static final DirectionProperty TARGET = DirectionProperty.create("target", Direction.values());

	default VoxelShape getSourceShape(BlockState state) {
		return Shapes.sourceShape(state.getValue(SOURCE));
	}

	default VoxelShape getTargetShape(BlockState state) {
		return Shapes.targetShape(state.getValue(TARGET));
	}
	
	VoxelShape getCoreShape(BlockState state);
	
	default VoxelShape getCurentShape(BlockState state) {
		return VoxelShapes.or(getSourceShape(state), getTargetShape(state), getCoreShape(state)).optimize();
	}
	
	default VoxelShape getShape(BlockState state, BlockPos pos, RayTraceResult result) {
		if (result != null && result.getType() == RayTraceResult.Type.BLOCK && ((BlockRayTraceResult) result).getBlockPos().equals(pos)) {
			final Vector3d hit = result.getLocation();
			VoxelShape source = getSourceShape(state);
			VoxelShape target = getTargetShape(state);
			VoxelShape core = getCoreShape(state);

			if (ShapeHelper.vectorCollideWithShape(source, pos, hit)) {
				return source;
			} else if (ShapeHelper.vectorCollideWithShape(target, pos, hit)) {
				return target;
			} else if (ShapeHelper.vectorCollideWithShape(core, pos, hit)) {
				return core;
			}
		}
		return getCurentShape(state);
	}
	
	default ActionResultType moveIO(BlockState state, World world, BlockPos pos, BlockRayTraceResult hit) {
		return this.moveIO(state, world, pos, hit, getShape(state, pos, hit));
	}
	
	default ActionResultType moveIO(BlockState state, World world, BlockPos pos, BlockRayTraceResult hit, VoxelShape shape) {
		Direction direction = hit.getDirection().getOpposite();

		if (state.getValue(SOURCE) == direction || state.getValue(TARGET) == direction) {
			return ActionResultType.PASS;
		} else if (Shapes.SOURCE_SHAPES.contains(shape)) {
			world.setBlockAndUpdate(pos, state.setValue(SOURCE, direction));
			return ActionResultType.SUCCESS;
		} else if (Shapes.TARGET_SHAPES.contains(shape)) {
			world.setBlockAndUpdate(pos, state.setValue(TARGET, direction));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
