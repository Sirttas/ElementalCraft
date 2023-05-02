package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
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
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.interaction.ECinteractions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MysticalGroveShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_mystical_grove";

	private static final VoxelShape BASE = Block.box(3D, 2D, 3D, 13D, 7D, 3D);
	private static final VoxelShape CONNECTOR = Block.box(7D, 0D, 7D, 9D, 2D, 9D);
	private static final VoxelShape PIPE_1 = Block.box(2D, 0D, 3D, 4D, 9D, 4D);
	private static final VoxelShape PIPE_2 = Block.box(12D, 0D, 2D, 14D, 9D, 4D);
	private static final VoxelShape PIPE_3 = Block.box(2D, 0D, 12D, 4D, 9D, 14D);
	private static final VoxelShape PIPE_4 = Block.box(12D, 0D, 12D, 14D, 9D, 14D);

	private static final VoxelShape SHAPE = Shapes.or(BASE, CONNECTOR, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public MysticalGroveShrineUpgradeBlock() {
		super(ShrineUpgrades.MYSTICAL_GROVE);
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
		tooltip.add(Component.translatable("tooltip.elementalcraft.mystical_grove").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flag);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
		if (ECinteractions.isBotaniaActive()) {
			super.fillItemCategory(group, items);
		}
	}
}
