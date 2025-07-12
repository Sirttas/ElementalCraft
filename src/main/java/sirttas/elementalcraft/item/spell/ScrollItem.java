package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ScrollItem extends AbstractSpellHolderItem {

	public static final String NAME = "scroll";

	public ScrollItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected void consume(ItemStack stack) {
		stack.shrink(1);
	}

	/**
	 * allows stacks to add custom lines of information to the mouseover description
	 */
	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		var spell = SpellHelper.getSpell(stack);

		if (SpellHelper.isValid(spell)) {
			tooltip.add(Component.empty().append(spell.value().getDisplayName()).withStyle(ChatFormatting.GRAY));
			addAttributeTooltip(tooltip, spell.value());
		}
	}

	@Nonnull
    @Override
	public Component getName(@Nonnull ItemStack stack) {
		var spell = SpellHelper.getSpell(stack);

		if (SpellHelper.isValid(spell)) {
			return Component.translatable("tooltip.elementalcraft.scroll_of", spell.value().getDisplayName());
		}
		return super.getName(stack);
	}
}
