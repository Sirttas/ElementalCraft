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

public class FocusItem extends AbstractSpellHolderItem {

	public static final String NAME = "focus";
	
	public FocusItem(Properties properties) {
		super(properties);
	}

	@Override
	protected void consume(ItemStack stack) {
		SpellHelper.removeSpell(stack, SpellHelper.getSpell(stack));
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		var spell = SpellHelper.getSpell(stack);

		SpellHelper.getSpellList(stack).forEachSpell((s, i) -> {
			ChatFormatting style = s.value() == spell ? ChatFormatting.AQUA : ChatFormatting.GRAY;

			if (i == 1) {
				tooltip.add(Component.empty().append(s.value().getDisplayName()).withStyle(style));
			} else {
				tooltip.add(Component.literal(i + " ").append(s.value().getDisplayName()).withStyle(style));
			}
		});
		if (SpellHelper.isValid(spell)) {
			addAttributeTooltip(tooltip, SpellHelper.getSpell(stack).value());
		}
	}
}
