package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class ScrollItem extends AbstractSpellHolderItem {

	public static final String NAME = "scroll";

	public ScrollItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected void consume(ItemStack stack) {
		stack.shrink(1);
	}

    @Override
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
		var spell = SpellHelper.getSpell(itemStack);

		if (SpellHelper.isValid(spell)) {
            builder.accept(Component.empty().append(spell.value().getDisplayName()).withStyle(ChatFormatting.GRAY));
			addAttributeTooltip(builder, spell.value());
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
