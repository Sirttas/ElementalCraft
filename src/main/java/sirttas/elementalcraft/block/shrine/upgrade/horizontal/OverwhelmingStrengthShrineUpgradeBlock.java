package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;
import java.util.Map;

public class OverwhelmingStrengthShrineUpgradeBlock extends AbstractHorizontalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_overwhelming_strength";
	public static final MapCodec<OverwhelmingStrengthShrineUpgradeBlock> CODEC = simpleCodec(OverwhelmingStrengthShrineUpgradeBlock::new);


	private static final VoxelShape BASE = Block.box(6D, 6D, 5D, 10D, 10D, 9D);
	private static final VoxelShape PIPE = Block.box(7D, 7D, 0D, 9D, 9D, 5D);
	private static final VoxelShape PIPE_SIDE_1 = Block.box(4D, 7D, 6D, 6D, 9D, 8D);
	private static final VoxelShape PIPE_SIDE_2 = Block.box(10D, 7D, 6D, 12D, 9D, 8D);
	private static final VoxelShape PIPE_SIDE_3 = Block.box(7D, 10D, 6D, 9D, 12D, 8D);
	private static final VoxelShape PIPE_SIDE_4 = Block.box(7D, 4D, 6D, 9D, 6D, 8D);
	private static final VoxelShape PLATE_1 = Block.box(3D, 5D, 4D, 4D, 11D, 10D);
	private static final VoxelShape PLATE_2 = Block.box(12D, 5D, 4D, 13D, 11D, 10D);
	private static final VoxelShape PLATE_3 = Block.box(5D, 12D, 4D, 11D, 13D, 10D);
	private static final VoxelShape PLATE_4 = Block.box(5D, 3D, 4D, 11D, 4D, 10D);
	private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Direction.NORTH, Shapes.or(BASE, PIPE, PIPE_SIDE_1, PIPE_SIDE_2, PIPE_SIDE_3, PIPE_SIDE_4, PLATE_1, PLATE_2, PLATE_3, PLATE_4));


	public OverwhelmingStrengthShrineUpgradeBlock(Properties properties) {
		super(ShrineUpgrades.STRENGTH, properties);
	}

	@Override
	protected @NotNull MapCodec<OverwhelmingStrengthShrineUpgradeBlock> codec() {
		return CODEC;
	}
	
	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}
}
