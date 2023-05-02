package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

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
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CrystalGrowthShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_crystal_growth";

	private static final VoxelShape TOP = Block.box(5D, 10D, 5D, 11D, 14D, 11D);
	private static final VoxelShape PIPE_1 = Block.box(4D, 5D, 4D, 6D, 15D, 6D);
	private static final VoxelShape PIPE_2 = Block.box(4D, 5D, 10D, 6D, 15D, 12D);
	private static final VoxelShape PIPE_3 = Block.box(10D, 5D, 4D, 12D, 15D, 6D);
	private static final VoxelShape PIPE_4 = Block.box(10D, 5D, 10D, 12D, 15D, 12D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.BONELESS_GROWTH, TOP, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public CrystalGrowthShrineUpgradeBlock() {
		super(ShrineUpgrades.CRYSTAL_GROWTH);
	}
	
	@Nonnull
	@Override
	public Direction getFacing(@Nonnull BlockState state) {
		return Direction.DOWN;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.crystal_growth").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flag);
	}
}
