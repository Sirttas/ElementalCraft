package sirttas.elementalcraft.block.sorter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shape.ShapeHelper;

public interface ISorterBlock {

	public static final DirectionProperty SOURCE = DirectionProperty.create("source", Direction.values());
	public static final DirectionProperty TARGET = DirectionProperty.create("target", Direction.values());

	default VoxelShape getSourceShape(BlockState state) {
		return ECShapes.sourceShape(state.getValue(SOURCE));
	}

	default VoxelShape getTargetShape(BlockState state) {
		return ECShapes.targetShape(state.getValue(TARGET));
	}
	
	VoxelShape getCoreShape(BlockState state);
	
	default VoxelShape getCurentShape(BlockState state) {
		return Shapes.or(getSourceShape(state), getTargetShape(state), getCoreShape(state)).optimize();
	}
	
	default VoxelShape getShape(BlockState state, BlockPos pos, HitResult result) {
		if (result != null && result.getType() == HitResult.Type.BLOCK && ((BlockHitResult) result).getBlockPos().equals(pos)) {
			final Vec3 hit = result.getLocation();
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
	
	default InteractionResult moveIO(BlockState state, Level world, BlockPos pos, BlockHitResult hit) {
		return this.moveIO(state, world, pos, hit, getShape(state, pos, hit));
	}
	
	default InteractionResult moveIO(BlockState state, Level world, BlockPos pos, BlockHitResult hit, VoxelShape shape) {
		Direction direction = hit.getDirection().getOpposite();

		if (state.getValue(SOURCE) == direction || state.getValue(TARGET) == direction) {
			return InteractionResult.PASS;
		} else if (ECShapes.SOURCE_SHAPES.contains(shape)) {
			world.setBlockAndUpdate(pos, state.setValue(SOURCE, direction));
			return InteractionResult.SUCCESS;
		} else if (ECShapes.TARGET_SHAPES.contains(shape)) {
			world.setBlockAndUpdate(pos, state.setValue(TARGET, direction));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
