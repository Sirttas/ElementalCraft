package sirttas.elementalcraft.item;

import com.google.common.collect.Multimap;
import mezz.jei.color.ColorGetter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map.Entry;

public class ECItem extends Item {

	private static boolean noJeiLogged = false;

	public ECItem() {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
	}

	public ECItem(Properties properties) {
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
	public boolean isFoil(@Nonnull ItemStack stack) {
		return glint || super.isFoil(stack);
	}

	public ECItem setEffect(boolean glint) {
		this.glint = glint;
		return this;
	}

	@OnlyIn(Dist.CLIENT)
	public static int lookupColor(ItemStack stack) {
		try {
			List<Integer> colors = ColorGetter.getColors(stack, 2);
	
			if (colors != null && !colors.isEmpty()) {
				return colors.get(0);
			}
		} catch (NoClassDefFoundError e) {
			if (!noJeiLogged) {
				ElementalCraftApi.LOGGER.warn("JEI not present, can't lookup item colors", e);
				noJeiLogged = true;
			}
		}
		return -1;
	}

	@OnlyIn(Dist.CLIENT)
	public static void addAttributeMultiMapToTooltip(List<Component> tooltip, Multimap<Attribute, AttributeModifier> multiMap) {
		addAttributeMultiMapToTooltip(tooltip, multiMap, null);
	}

	@OnlyIn(Dist.CLIENT)
	public static void addAttributeMultiMapToTooltip(List<Component> tooltip, Multimap<Attribute, AttributeModifier> multiMap, @Nullable Component title) {
		if (!multiMap.isEmpty()) {
			tooltip.add(new TextComponent(""));
			if (title != null) {
				tooltip.add(title);
			}
			for (Entry<Attribute, AttributeModifier> entry : multiMap.entries()) {
				tooltip.add(getAttributeTooltip(entry.getKey(), entry.getValue()));
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static MutableComponent getAttributeTooltip(Attribute attribute, AttributeModifier attributemodifier) {
		double d0 = attributemodifier.getAmount();

		double d1;
		if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
			if (attribute.equals(Attributes.KNOCKBACK_RESISTANCE)) {
				d1 = d0 * 10.0D;
			} else {
				d1 = d0;
			}
		} else {
			d1 = d0 * 100.0D;
		}

		if (d0 > 0.0D) {
			return new TranslatableComponent("attribute.modifier.plus." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
					new TranslatableComponent(attribute.getDescriptionId())).withStyle(ChatFormatting.BLUE);
		} else if (d0 < 0.0D) {
			d1 = d1 * -1.0D;
			return new TranslatableComponent("attribute.modifier.take." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
					new TranslatableComponent(attribute.getDescriptionId())).withStyle(ChatFormatting.RED);
		}
		return null;
	}
}
