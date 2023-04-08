package sirttas.elementalcraft.item;

import com.google.common.collect.Multimap;
import mezz.jei.library.color.ColorGetter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class ECItem extends Item {

	private static final Comparator<Integer> COLOR_BRIGHTNESS_COMPARATOR = Comparator.comparingInt(ECItem::getBrightness);

	private static boolean noJeiLogged = false;

	public ECItem() {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES);
	}

	public ECItem(Properties properties) {
		super(properties);
	}

	private boolean foil = false;

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(@Nonnull ItemStack stack) {
		return foil || super.isFoil(stack);
	}

	public ECItem setFoil(boolean foil) {
		this.foil = foil;
		return this;
	}

	@OnlyIn(Dist.CLIENT)
	public static int[] lookupColors(ItemStack stack) {
		try {
			var colors = new ColorGetter().getColors(stack, 3); // FIXME extract ColorGetter from JEI
	
			if (colors != null && !colors.isEmpty()) {
				var array = colors.stream()
						.map(color -> color == null ? -1 : color)
						.sorted(COLOR_BRIGHTNESS_COMPARATOR.reversed())
						.mapToInt(Integer::intValue)
						.toArray();

				if (array.length == 1) {
					return new int[] { array[0], array[0], array[0] };
				} else if (array.length == 2) {
					return new int[] { array[0], array[0], array[1] };
				} else {
					return new int[] { array[0], array[1], array[2] };
				}
			}
		} catch (NoClassDefFoundError e) {
			if (!noJeiLogged) {
				ElementalCraftApi.LOGGER.warn("JEI not present, can't lookup item colors", e);
				noJeiLogged = true;
			}
		}
		return new int[] { -1, -1, -1 };
	}

	@OnlyIn(Dist.CLIENT)
	private static int getBrightness(int color) {
		return ((color & 0xFF) + ((color >> 8) & 0xFF) + ((color >> 16) & 0xFF)) / 3;
	}

	@OnlyIn(Dist.CLIENT)
	public static void addAttributeMultiMapToTooltip(List<Component> tooltip, Multimap<Attribute, AttributeModifier> multiMap) {
		addAttributeMultiMapToTooltip(tooltip, multiMap, null);
	}

	@OnlyIn(Dist.CLIENT)
	public static void addAttributeMultiMapToTooltip(List<Component> tooltip, Multimap<Attribute, AttributeModifier> multiMap, @Nullable Component title) {
		if (!multiMap.isEmpty()) {
			tooltip.add(Component.empty());
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
			return Component.translatable("attribute.modifier.plus." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
					Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.BLUE);
		} else if (d0 < 0.0D) {
			d1 = d1 * -1.0D;
			return Component.translatable("attribute.modifier.take." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1),
					Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.RED);
		}
		return null;
	}
}
