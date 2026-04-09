package sirttas.elementalcraft.block.shrine.upgrade.stempollination;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;

public class StemPollinationShrineUpgradeBlock extends ShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_stem_pollination";
	public static final MapCodec<StemPollinationShrineUpgradeBlock> CODEC = simpleCodec(StemPollinationShrineUpgradeBlock::new);

	private static final VoxelShape TOP = Block.box(6D, 8D, 6D, 10D, 11D, 10D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 3D, 9D, 9D, 6D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 10D, 9D, 9D, 13D);
	private static final VoxelShape PIPE_WEST = Block.box(3D, 7D, 7D, 6D, 9D, 9D);
	private static final VoxelShape PIPE_EAST = Block.box(10D, 7D, 7D, 13D, 9D, 9D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.BONELESS_GROWTH, TOP, PIPE_NORTH, PIPE_SOUTH, PIPE_WEST, PIPE_EAST);

	public StemPollinationShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.STEM_POLLINATION, properties);
	}

	@Override
	protected @NotNull MapCodec<StemPollinationShrineUpgradeBlock> codec() {
		return CODEC;
	}

	@Nonnull
	@Override
	public Direction getFacing(@Nonnull BlockState state) {
		return Direction.DOWN;
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
