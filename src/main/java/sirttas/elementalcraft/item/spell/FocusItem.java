package sirttas.elementalcraft.item.spell;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		Spell spell = SpellHelper.getSpell(stack);

		SpellHelper.forEachSpell(stack, (s, i) -> {
			ChatFormatting formating = s == spell ? ChatFormatting.AQUA : ChatFormatting.GRAY;

			if (i == 1) {
				tooltip.add(new TextComponent("").append(s.getDisplayName()).withStyle(formating));
			} else {
				tooltip.add(new TextComponent(i + " ").append(s.getDisplayName()).withStyle(formating));
			}
		});
		if (spell != Spells.NONE) {
			addAttributeTooltip(tooltip, SpellHelper.getSpell(stack));
		}
	}
}
