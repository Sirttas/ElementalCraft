package sirttas.elementalcraft.block.shrine.upgrade.crystalgrowth;

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

public class CrystalGrowthShrineUpgradeBlock extends ShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_crystal_growth";
	public static final MapCodec<CrystalGrowthShrineUpgradeBlock> CODEC = simpleCodec(CrystalGrowthShrineUpgradeBlock::new);

	private static final VoxelShape TOP = Block.box(5D, 10D, 5D, 11D, 14D, 11D);
	private static final VoxelShape PIPE_1 = Block.box(4D, 5D, 4D, 6D, 15D, 6D);
	private static final VoxelShape PIPE_2 = Block.box(4D, 5D, 10D, 6D, 15D, 12D);
	private static final VoxelShape PIPE_3 = Block.box(10D, 5D, 4D, 12D, 15D, 6D);
	private static final VoxelShape PIPE_4 = Block.box(10D, 5D, 10D, 12D, 15D, 12D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.BONELESS_GROWTH, TOP, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public CrystalGrowthShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.CRYSTAL_GROWTH, properties);
	}

	@Override
	protected @NotNull MapCodec<CrystalGrowthShrineUpgradeBlock> codec() {
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
