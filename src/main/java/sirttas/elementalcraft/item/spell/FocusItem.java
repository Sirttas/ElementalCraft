package sirttas.elementalcraft.item.spell;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

public class FocusItem extends AbstractSpellHolderItem {

	public static final String NAME = "focus";

	public FocusItem() {
		this(ECProperties.Items.ITEM_UNSTACKABLE);
	}
	
	protected FocusItem(Properties properties) {
		super(properties);
	}

	@Override
	protected void consume(ItemStack stack) {
		SpellHelper.removeSpell(stack, SpellHelper.getSpell(stack));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		Spell spell = SpellHelper.getSpell(stack);

		SpellHelper.forEachSpell(stack, (s, i) -> {
			TextFormatting formating = s == spell ? TextFormatting.AQUA : TextFormatting.GRAY;

			if (i == 1) {
				tooltip.add(new StringTextComponent("").append(s.getDisplayName()).withStyle(formating));
			} else {
				tooltip.add(new StringTextComponent(i + " ").append(s.getDisplayName()).withStyle(formating));
			}
		});
		if (spell != Spells.NONE) {
			addAttributeTooltip(tooltip, SpellHelper.getSpell(stack));
		}
	}
}
