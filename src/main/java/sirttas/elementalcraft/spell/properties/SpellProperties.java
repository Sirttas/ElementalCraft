package sirttas.elementalcraft.spell.properties;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.gui.ECColorHelper;
import sirttas.elementalcraft.spell.Spell;

import java.util.Collections;

public record SpellProperties(
		Spell.Type spellType,
		ElementType elementType,
		int weight,
		int useDuration,
		int consumeAmount,
		int cooldown,
		float range,
		int color,
		boolean hidden,
		Multimap<Attribute, AttributeModifier> attributes
) implements IElementTypeProvider {

	public static final String NAME = "spell_properties";
	public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;

	public static final SpellProperties NONE = new SpellProperties();
	public static final Codec<SpellProperties> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Spell.Type.CODEC.fieldOf(ECNames.SPELL_TYPE).forGetter(SpellProperties::spellType),
			ElementType.forGetter(SpellProperties::getElementType),
			Codec.INT.optionalFieldOf(ECNames.WEIGHT, 0).forGetter(SpellProperties::weight),
			Codec.INT.optionalFieldOf(ECNames.USE_DURATION, 0).forGetter(SpellProperties::useDuration),
			Codec.INT.optionalFieldOf(ECNames.ELEMENT_CONSUMPTION, 0).forGetter(SpellProperties::consumeAmount),
			Codec.INT.optionalFieldOf(ECNames.COOLDOWN, 0).forGetter(SpellProperties::cooldown),
			Codec.FLOAT.optionalFieldOf(ECNames.RANGE, 0F).forGetter(SpellProperties::range),
			Codecs.COLOR.optionalFieldOf(ECNames.COLOR, -1).forGetter(SpellProperties::color),
			Codec.BOOL.optionalFieldOf("hidden", false).forGetter(SpellProperties::hidden),
			Codecs.ATTRIBUTE_MULTIMAP.optionalFieldOf(ECNames.ATTRIBUTES, Multimaps.forMap(Collections.emptyMap())).forGetter(SpellProperties::getAttributes)
	).apply(builder, SpellProperties::new));

	public SpellProperties() {
		this(Spell.Type.NONE, ElementType.NONE, 0, 0, 0, 0, 0, -1, true, null);
	}

	public SpellProperties(Spell.Type spellType, ElementType elementType, int weight, int useDuration, int consumeAmount, int cooldown, float range, int color, boolean hidden, Multimap<Attribute, AttributeModifier> attributes) {
		this.spellType = spellType;
		this.elementType = elementType;
		this.weight = weight;
		this.useDuration = useDuration;
		this.consumeAmount = consumeAmount;
		this.cooldown = cooldown;
		this.range = range;
		this.color = color;
		this.hidden = hidden;
		this.attributes = attributes != null ? Multimaps.unmodifiableMultimap(attributes) : Multimaps.forMap(Collections.emptyMap());
	}

    public static ResourceKey<SpellProperties> getKey(ResourceKey<Spell> key) {
		return IDataManager.createKey(ElementalCraft.SPELL_PROPERTIES_MANAGER_KEY, key.location());
    }

    @Override
	public ElementType getElementType() {
		return elementType;
	}

	public Multimap<Attribute, AttributeModifier> getAttributes() {
		return attributes;
	}

	public static final class Builder {

		public static final Encoder<Builder> ENCODER = CodecHelper.remapField(SpellProperties.CODEC, Codecs.HEX_COLOR.fieldOf(ECNames.COLOR), p -> p.color)
				.comap(builder -> new SpellProperties(builder.type, builder.elementType, builder.weight, builder.useDuration, builder.consumeAmount, builder.cooldown, (float) builder.range, builder.color, builder.hidden, builder.attributes));

		private int cooldown;
		private int consumeAmount;
		private int useDuration;
		private int weight;
		private int color;
		private double range;
		private boolean hidden;
		private ElementType elementType;
		private final Spell.Type type;
		private final Multimap<Attribute, AttributeModifier> attributes;

		private Builder(Spell.Type type) {
			this.type = type;
			this.elementType = ElementType.NONE;
			cooldown = 0;
			consumeAmount = 0;
			useDuration = 0;
			weight = 0;
			hidden = false;
			attributes = HashMultimap.create();
		}

		public static Builder create(Spell.Type type) {
			return new Builder(type);
		}

		public Builder cooldown(int cooldown) {
			this.cooldown = cooldown;
			return this;
		}

		public Builder consumeAmount(int consumeAmount) {
			this.consumeAmount = consumeAmount;
			return this;
		}

		public Builder useDuration(int useDuration) {
			this.useDuration = useDuration;
			return this;
		}


		public Builder elementType(ElementType elementType) {
			this.elementType = elementType;
			return this;
		}

		public Builder weight(int weight) {
			this.weight = weight;
			return this;
		}

		public Builder range(double range) {
			this.range = range;
			return this;
		}

		public Builder color(int color) {
			this.color = color;
			return this;
		}

		public Builder hidden() {
			this.hidden = true;
			return this;
		}

		public Builder addAttribute(Attribute attribute, AttributeModifier modifier) {
			this.attributes.put(attribute, modifier);
			return this;
		}
		
		public Builder color(int r, int g, int b) {
			return color(ECColorHelper.packColor(r, g, b));
		}
	}
}
