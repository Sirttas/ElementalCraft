package sirttas.elementalcraft.block.shrine.upgrade.horizontal.fortune.greater;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.fortune.AbstractFortuneShrineUpgradeBlock;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import java.util.Map;

public class GreaterFortuneShrineUpgradeBlock extends AbstractFortuneShrineUpgradeBlock implements EntityBlock {

	public static final String NAME = "shrine_upgrade_greater_fortune";
	public static final MapCodec<GreaterFortuneShrineUpgradeBlock> CODEC = simpleCodec(GreaterFortuneShrineUpgradeBlock::new);

	private static final VoxelShape CORE = Shapes.or(Block.box(6D, 6D, 4D, 10D, 10D, 10D),
			Block.box(2D, 6D, 4D, 14D, 10D, 8D));
	private static final VoxelShape CENTRAL_PILAR = Block.box(7D, 2D, 7D, 9D, 6D, 9D);
	private static final VoxelShape WEST_PILAR = Block.box(3D, 2D, 5D, 5D, 6D, 7D);
	private static final VoxelShape EST_PILAR = Block.box(11D, 2D, 5D, 13D, 6D, 7D);
	private static final VoxelShape CENTRAL_BASE_1 = Block.box(5D, 0D, 5D, 11D, 1D, 11D);
	private static final VoxelShape CENTRAL_BASE_2 = Block.box(6D, 1D, 6D, 10D, 2D, 10D);
	private static final VoxelShape WEST_BASE_1 = Block.box(1D, 0D, 3D, 7D, 1D, 9D);
	private static final VoxelShape WEST_BASE_2 = Block.box(2D, 1D, 4D, 6D, 2D, 8D);
	private static final VoxelShape EST_BASE_1 = Block.box(9D, 0D, 3D, 15D, 1D, 9D);
	private static final VoxelShape EST_BASE_2 = Block.box(10D, 1D, 4D, 14D, 2D, 8D);
	private static final VoxelShape PIPE = Block.box(7D, 7D, 0D, 9D, 9D, 4D);

	private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Direction.NORTH, Shapes.or(CORE, CENTRAL_PILAR, WEST_PILAR, EST_PILAR, CENTRAL_BASE_1, CENTRAL_BASE_2, WEST_BASE_1, WEST_BASE_2, EST_BASE_1, EST_BASE_2, PIPE));

	public GreaterFortuneShrineUpgradeBlock(Properties properties) {
		super(ShrineUpgrades.GREATER_FORTUNE,  properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		return new GreaterFortuneShrineUpgradeBlockEntity(blockPos, blockState);
	}

	@Override
	protected @NotNull MapCodec<GreaterFortuneShrineUpgradeBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public int getFortuneLevel() {
		return ECConfig.COMMON.greaterFortuneShrineUpgradeLevel.get();
	}
}
