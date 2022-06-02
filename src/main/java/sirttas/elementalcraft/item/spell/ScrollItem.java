package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ScrollItem extends AbstractSpellHolderItem {

	public static final String NAME = "scroll";

	public ScrollItem() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}

	@Override
	protected void consume(ItemStack stack) {
		stack.setCount(0);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell != Spells.NONE.get()) {
			tooltip.add(new TextComponent("").append(spell.getDisplayName()).withStyle(ChatFormatting.GRAY));
			addAttributeTooltip(tooltip, spell);
		}
	}

	@Nonnull
    @Override
	public Component getName(@Nonnull ItemStack stack) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell != Spells.NONE.get()) {
			return new TranslatableComponent("tooltip.elementalcraft.scroll_of", spell.getDisplayName());
		}
		return super.getName(stack);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			Spells.REGISTRY.get().forEach(s -> {
				if (s.isVisible()) {
					ItemStack stack = new ItemStack(this);

					SpellHelper.setSpell(stack, s);
					items.add(stack);
				}
			});
		}
	}
}
