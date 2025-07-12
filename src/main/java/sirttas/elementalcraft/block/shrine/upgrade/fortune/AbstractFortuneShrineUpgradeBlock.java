package sirttas.elementalcraft.block.shrine.upgrade.fortune;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockBehaviour;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.AbstractHorizontalShrineUpgradeBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractFortuneShrineUpgradeBlock extends AbstractHorizontalShrineUpgradeBlock {

	protected AbstractFortuneShrineUpgradeBlock(ResourceKey<ShrineUpgrade> key, BlockBehaviour.Properties properties) {
		super(key, properties);
	}

	public abstract int getFortuneLevel();

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("enchantment.minecraft.fortune")
				.append(Component.literal(" "))
				.append(Component.translatable("enchantment.level." + getFortuneLevel()))
				.withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, tooltipContext, tooltip, flag);
	}
}
