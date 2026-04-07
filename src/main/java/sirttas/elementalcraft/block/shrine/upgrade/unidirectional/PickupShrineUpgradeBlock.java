package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PickupShrineUpgradeBlock extends ShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_pickup";
	public static final MapCodec<PickupShrineUpgradeBlock> CODEC = simpleCodec(PickupShrineUpgradeBlock::new);

	private static final VoxelShape BASE = Block.box(6D, -1D, 6D, 10D, 10D, 10D);
	private static final VoxelShape PIPE_1 = Block.box(7D, 7D, 3D, 9D, 9D, 6D);
	private static final VoxelShape PIPE_2 = Block.box(7D, 7D, 10D, 9D, 9D, 13D);
	private static final VoxelShape PIPE_3 = Block.box(3D, 7D, 7D, 6D, 9D, 9D);
	private static final VoxelShape PIPE_4 = Block.box(10D, 7D, 7D, 13D, 9D, 9D);

	private static final VoxelShape SHAPE = Shapes.or(BASE, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public PickupShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.PICKUP, properties);
	}

	@Override
	protected @NotNull MapCodec<PickupShrineUpgradeBlock> codec() {
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

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.shrine_upgrade.pickup").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, tooltipContext, tooltip, flag);
	}
}
