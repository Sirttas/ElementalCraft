package sirttas.elementalcraft.block.shrine.upgrade.protection;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shrine.upgrade.HorizontalShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;

public class ProtectionShrineUpgradeBlock extends HorizontalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_protection";
	public static final MapCodec<ProtectionShrineUpgradeBlock> CODEC = simpleCodec(ProtectionShrineUpgradeBlock::new);


	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 2D);
	private static final VoxelShape SHIELD_1_NORTH = Block.box(5D, 5D, 2D, 11D, 12D, 3D);
	private static final VoxelShape SHIELD_2_NORTH = Block.box(6D, 4D, 2D, 10D, 5D, 3D);
	private static final VoxelShape SHAPE_NORTH = Shapes.or(PIPE_NORTH, SHIELD_1_NORTH, SHIELD_2_NORTH);

	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 14D, 9D, 9D, 16D);
	private static final VoxelShape SHIELD_1_SOUTH = Block.box(5D, 5D, 13D, 11D, 12D, 14D);
	private static final VoxelShape SHIELD_2_SOUTH = Block.box(6D, 4D, 13D, 10D, 5D, 14D);
	private static final VoxelShape SHAPE_SOUTH = Shapes.or(PIPE_SOUTH, SHIELD_1_SOUTH, SHIELD_2_SOUTH);

	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 2D, 9D, 9D);
	private static final VoxelShape SHIELD_1_WEST = Block.box(2D, 5D, 5D, 3D, 12D, 11D);
	private static final VoxelShape SHIELD_2_WEST = Block.box(2D, 4D, 6D, 3D, 5D, 10D);
	private static final VoxelShape SHAPE_WEST = Shapes.or( PIPE_WEST, SHIELD_1_WEST, SHIELD_2_WEST);

	private static final VoxelShape PIPE_EAST = Block.box(14D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape SHIELD_1_EAST = Block.box(13D, 5D, 5D, 14D, 12D, 11D);
	private static final VoxelShape SHIELD_2_EAST = Block.box(13D, 4D, 6D, 14D, 5D, 10D);
	private static final VoxelShape SHAPE_EAST = Shapes.or(PIPE_EAST, SHIELD_1_EAST, SHIELD_2_EAST);

	public ProtectionShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.PROTECTION, properties);
	}

	@Override
	protected @NotNull MapCodec<ProtectionShrineUpgradeBlock> codec() {
		return CODEC;
	}
	
	@Nonnull
    @Override
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case SOUTH -> SHAPE_SOUTH;
			case WEST -> SHAPE_WEST;
			case EAST -> SHAPE_EAST;
			default -> SHAPE_NORTH;
		};
	}
}
