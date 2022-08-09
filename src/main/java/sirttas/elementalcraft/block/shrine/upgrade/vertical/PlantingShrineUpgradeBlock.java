package sirttas.elementalcraft.block.shrine.upgrade.vertical;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PlantingShrineUpgradeBlock extends AbstractVerticalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_planting";

	private static final VoxelShape BASE_1_UP = Block.box(6D, 14D, 6D, 10D, 16D, 10D);
	private static final VoxelShape BASE_2_UP = Block.box(2D, 12D, 2D, 14D, 14D, 14D);
	private static final VoxelShape PIPE_1_UP = Block.box(13D, 11D, 1D, 15D, 18D, 3D);
	private static final VoxelShape PIPE_2_UP = Block.box(13D, 11D, 13D, 15D, 18D, 15D);
	private static final VoxelShape PIPE_3_UP = Block.box(1D, 11D, 13D, 3D, 18D, 15D);
	private static final VoxelShape PIPE_4_UP = Block.box(1D, 11D, 1D, 3D, 18D, 3D);

	private static final VoxelShape SHAPE_UP = Shapes.or(BASE_1_UP, BASE_2_UP, PIPE_1_UP, PIPE_2_UP, PIPE_3_UP, PIPE_4_UP);

	private static final VoxelShape BASE_1_DOWN = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape BASE_2_DOWN = Block.box(2D, 2D, 2D, 14D, 4D, 14D);
	private static final VoxelShape PIPE_1_DOWN = Block.box(13D, -2D, 1D, 15D, 5D, 3D);
	private static final VoxelShape PIPE_2_DOWN = Block.box(13D, -2D, 13D, 15D, 5D, 15D);
	private static final VoxelShape PIPE_3_DOWN = Block.box(1D, -2D, 13D, 3D, 5D, 15D);
	private static final VoxelShape PIPE_4_DOWN = Block.box(1D, -2D, 1D, 3D, 5D, 3D);

	private static final VoxelShape SHAPE_DOWN = Shapes.or(BASE_1_DOWN, BASE_2_DOWN, PIPE_1_DOWN, PIPE_2_DOWN, PIPE_3_DOWN, PIPE_4_DOWN);

	public PlantingShrineUpgradeBlock() {
		super(ShrineUpgrades.PLANTING);
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return state.getValue(FACING) == Direction.UP ? SHAPE_UP : SHAPE_DOWN;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.planting").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flag);
	}
}
