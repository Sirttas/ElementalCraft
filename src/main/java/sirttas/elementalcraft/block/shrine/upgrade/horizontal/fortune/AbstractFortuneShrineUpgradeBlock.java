package sirttas.elementalcraft.block.shrine.upgrade.horizontal.fortune;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("enchantment.minecraft.fortune")
				.append(Component.literal(" "))
				.append(Component.translatable("enchantment.level." + getFortuneLevel()))
				.withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flag);
	}
}
