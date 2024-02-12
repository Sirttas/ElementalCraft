package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BonelessGrowthShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_boneless_growth";
	public static final MapCodec<BonelessGrowthShrineUpgradeBlock> CODEC = simpleCodec(BonelessGrowthShrineUpgradeBlock::new);


	public BonelessGrowthShrineUpgradeBlock(BlockBehaviour.Properties properties) {
		super(ShrineUpgrades.BONELESS_GROWTH, properties);
	}

	@Override
	protected @NotNull MapCodec<BonelessGrowthShrineUpgradeBlock> codec() {
		return CODEC;
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
		return ECShapes.BONELESS_GROWTH;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.boneless_growth").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flag);
	}
}
