package sirttas.elementalcraft.item.spell;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

public class ItemScroll extends AbstractItemSpellHolder {

	public static final String NAME = "scroll";

	public ItemScroll() {
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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell != Spells.NONE) {
			tooltip.add(new StringTextComponent("").appendSibling(spell.getDisplayName()).mergeStyle(TextFormatting.GRAY));
			addAttributeTooltip(tooltip, spell);
		}
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell != Spells.NONE) {
			return new TranslationTextComponent("tooltip.elementalcraft.scroll_of", spell.getDisplayName());
		}
		return super.getDisplayName(stack);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			Spell.REGISTRY.forEach(s -> {
				if (s.isValid()) {
					ItemStack stack = new ItemStack(this);

					SpellHelper.setSpell(stack, s);
					items.add(stack);
				}
			});
		}
	}
}
