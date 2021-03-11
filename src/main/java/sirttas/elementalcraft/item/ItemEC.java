package sirttas.elementalcraft.item;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;

import mezz.jei.color.ColorGetter;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.property.ECProperties;

public class ItemEC extends Item {

	private static boolean noJeiLogged = false;

	public ItemEC() {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
	}

	public ItemEC(Properties properties) {
		super(properties);
	}

	private boolean glint = false;

	/**
	 * Returns true if this item has an enchantment glint. By default, this returns
	 * <code>stack.isItemEnchanted()</code>, but other items can override it (for
	 * instance, written books always return true).
	 * 
	 * Note that if you override this method, you generally want to also call the
	 * super version (on {@link Item}) to get the glint for enchanted items. Of
	 * course, that is unnecessary if the overwritten version always returns true.
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return glint || super.hasEffect(stack);
	}

	public ItemEC setEffect(boolean glint) {
		this.glint = glint;
		return this;
	}

	@OnlyIn(Dist.CLIENT)
	public static final int lookupColor(ItemStack stack) {
		try {
			List<Integer> colors = ColorGetter.getColors(stack, 2);
	
			if (colors != null && !colors.isEmpty()) {
				return colors.get(0);
			}
		} catch (NoClassDefFoundError e) {
			if (!noJeiLogged) {
				ElementalCraft.LOGGER.warn("JEI not present, can't lookup item colors", e);
				noJeiLogged = true;
			}
		}
		return -1;
	}

	@OnlyIn(Dist.CLIENT)
	public static void addAttributeMultimapToTooltip(List<ITextComponent> tooltip, Multimap<Attribute, AttributeModifier> multimap, ITextComponent title) {
		if (!multimap.isEmpty()) {
			tooltip.add(new StringTextComponent(""));
			tooltip.add(title);
			for (Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
				AttributeModifier attributemodifier = entry.getValue();
				double d0 = attributemodifier.getAmount();

				double d1;
				if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
						d1 = d0 * 10.0D;
					} else {
						d1 = d0;
					}
				} else {
					d1 = d0 * 100.0D;
				}

				if (d0 > 0.0D) {
					tooltip.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1),
							new TranslationTextComponent(entry.getKey().getAttributeName()))).mergeStyle(TextFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					tooltip.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1),
							new TranslationTextComponent(entry.getKey().getAttributeName()))).mergeStyle(TextFormatting.RED));
				}
			}
		}
	}
}
