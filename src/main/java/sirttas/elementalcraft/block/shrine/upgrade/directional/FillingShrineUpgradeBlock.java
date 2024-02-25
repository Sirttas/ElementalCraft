package sirttas.elementalcraft.block.shrine.upgrade.directional;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class FillingShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_filling";
	public static final MapCodec<FillingShrineUpgradeBlock> CODEC = simpleCodec(FillingShrineUpgradeBlock::new);

	private static final VoxelShape BASE = Block.box(3D, 4D, 3D, 13D, 8D, 13D);
	private static final VoxelShape PIPE_UP = Block.box(7D, 8D, 7D, 9D, 16D, 9D);
	
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 0D, 4D, 9D, 4D, 6D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 0D, 10D, 9D, 4D, 12D);
	private static final VoxelShape PIPE_WEST = Block.box(4D, 0D, 7D, 6D, 4D, 9D);
	private static final VoxelShape PIPE_EAST = Block.box(10D, 0D, 7D, 12D, 4D, 9D);
	
	private static final VoxelShape PIPE_NORTH_WEST = Block.box(4D, 0D, 4D, 6D, 4D, 6D);
	private static final VoxelShape PIPE_NORTH_EAST = Block.box(10D, 0D, 4D, 12D, 4D, 6D);
	private static final VoxelShape PIPE_SOUTH_WEST = Block.box(4D, 0D, 10D, 6D, 4D, 12D);
	private static final VoxelShape PIPE_SOUTH_EAST = Block.box(10D, 0D, 10D, 12D, 4D, 12D);

	private static final VoxelShape DOWN_SHAPE = Shapes.or(BASE, PIPE_UP, PIPE_NORTH, PIPE_SOUTH, PIPE_WEST, PIPE_EAST, PIPE_NORTH_WEST, PIPE_NORTH_EAST, PIPE_SOUTH_WEST, PIPE_SOUTH_EAST);

	private static final Map<Direction, VoxelShape> VERTICAL_SHAPES = ShapeHelper.directionShapes(DOWN_SHAPE);

	private static final VoxelShape NORTH_SHAPE = Shapes.or(VERTICAL_SHAPES.get(Direction.SOUTH),
			Block.box(4D, 10D, -3D, 6D, 12D, 0D),
			Block.box(7D, 10D, -3D, 9D, 12D, 0D),
			Block.box(10D, 10D, -3D, 12D, 12D, 0D),
			Block.box(4D, 7D, -3D, 6D, 9D, 0D),
			Block.box(10D, 7D, -3D, 12D, 9D, 0D),
			Block.box(4D, 4D, -1D, 6D, 6D, 0D),
			Block.box(7D, 4D, -1D, 9D, 6D, 0D),
			Block.box(10D, 4D, -1D, 12D, 6D, 0D)
	);

	private static final Map<Direction, VoxelShape> HORIZONTAL_SHAPES = ShapeHelper.directionShapes(Direction.NORTH, NORTH_SHAPE);

	private static final Map<Direction, VoxelShape> SHAPES = Map.of(
			Direction.DOWN, DOWN_SHAPE,
			Direction.UP, VERTICAL_SHAPES.get(Direction.DOWN),
			Direction.NORTH, HORIZONTAL_SHAPES.get(Direction.NORTH),
			Direction.SOUTH, HORIZONTAL_SHAPES.get(Direction.SOUTH),
			Direction.WEST, HORIZONTAL_SHAPES.get(Direction.WEST),
			Direction.EAST, HORIZONTAL_SHAPES.get(Direction.EAST)
	);

	public FillingShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.FILLING, properties);
	}

	@Override
	protected @NotNull MapCodec<FillingShrineUpgradeBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.filling").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flag);
	}
}
