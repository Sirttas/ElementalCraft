package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.SpellHelper;

import java.util.function.Consumer;

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
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
		var spell = SpellHelper.getSpell(itemStack);

		SpellHelper.getSpellList(itemStack).forEachSpell((s, i) -> {
			ChatFormatting style = s.value() == spell ? ChatFormatting.AQUA : ChatFormatting.GRAY;

			if (i == 1) {
                builder.accept(Component.empty().append(s.value().getDisplayName()).withStyle(style));
			} else {
                builder.accept(Component.literal(i + " ").append(s.value().getDisplayName()).withStyle(style));
			}
		});
		if (SpellHelper.isValid(spell)) {
			addAttributeTooltip(builder, SpellHelper.getSpell(itemStack).value());
		}
	}
}
