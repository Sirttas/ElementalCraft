package sirttas.elementalcraft.jewel;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;

public class AttributeJewel extends AbstractJewel {
	
	public static final String NAME = ECNames.ATTRIBUTE;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final JewelType<AttributeJewel> TYPE = null;
	public static final Codec<AttributeJewel> CODEC = RecordCodecBuilder.<AttributeJewel>create(builder -> codec(builder).and(
			Codecs.ATTRIBUTE_MULTIMAP.fieldOf(ECNames.ATTRIBUTES).forGetter(AttributeJewel::getAttributes)
	).apply(builder, AttributeJewel::new));
	
	private final Multimap<Attribute, AttributeModifier> attributes;
	
	protected AttributeJewel(ElementType elementType, int consumption, Multimap<Attribute, AttributeModifier> attributes) {
		super(elementType, consumption);
		this.attributes = ImmutableMultimap.copyOf(attributes);
	}

	@Override
	JewelType<AttributeJewel> getType() {
		return TYPE;
	}

	public Multimap<Attribute, AttributeModifier> getAttributes() {
		return attributes;
	}
}
