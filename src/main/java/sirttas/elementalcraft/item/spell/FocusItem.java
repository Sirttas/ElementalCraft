package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

import net.minecraft.world.item.Item.Properties;

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
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
		Spell spell = SpellHelper.getSpell(stack);

		SpellHelper.forEachSpell(stack, (s, i) -> {
			ChatFormatting style = s == spell ? ChatFormatting.AQUA : ChatFormatting.GRAY;

			if (i == 1) {
				tooltip.add(Component.empty().append(s.getDisplayName()).withStyle(style));
			} else {
				tooltip.add(Component.literal(i + " ").append(s.getDisplayName()).withStyle(style));
			}
		});
		if (spell != Spells.NONE.get()) {
			addAttributeTooltip(tooltip, SpellHelper.getSpell(stack));
		}
	}
}
