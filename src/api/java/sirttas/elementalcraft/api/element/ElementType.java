package sirttas.elementalcraft.api.element;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public enum ElementType implements StringRepresentable, IElementTypeProvider {

	NONE(0, 0, 0, "none"),
	WATER(43, 173, 255, "water"),
	FIRE(247, 107, 27, "fire"),
	EARTH(76, 133, 102, "earth"),
	AIR(238, 255, 219, "air");

	public static final List<ElementType> ALL_VALID = ImmutableList.copyOf(Stream.of(values()).filter(type -> type != NONE).toList());
	public static final Codec<ElementType> CODEC = StringRepresentable.fromEnum(ElementType::values, ElementType::byName);
	public static final EnumProperty<ElementType> STATE_PROPERTY = EnumProperty.create(ECNames.ELEMENT_TYPE, ElementType.class);
	
	private final float r;
	private final float g;
	private final float b;
	private final int color;
	private final String name;

	ElementType(int r, int g, int b, String name) {
		this.r = r / 255F;
		this.g = g / 255F;
		this.b = b / 255F;
		this.name = name;
		this.color = r << 16 | g << 8 | b;
	}

	public float getRed() {
		return r;
	}

	public float getGreen() {
		return g;
	}

	public float getBlue() {
		return b;
	}

	public int getColor() {
		return this == NONE ? -1 : color;
	}

	public static ElementType random() {
		return random(new Random());
	}

	public static ElementType random(Random rand) {
		int random = rand.nextInt(4);
		return switch (random) {
			case 0 -> WATER;
			case 1 -> FIRE;
			case 2 -> EARTH;
			case 3 -> AIR;
			default -> NONE;
		};
	}

	@Nonnull
	@Override
	public String getSerializedName() {
		return this.name;
	}

	@Override
	public ElementType getElementType() {
		return this;
	}

	public String getTranslationKey() {
		return "element.elementalcraft." + getSerializedName();
	}

	public Component getDisplayName() {
		return new TranslatableComponent(getTranslationKey());
	}

	public static ElementType byName(String name) {
		for (ElementType elementType : values()) {
			if (elementType.name.equals(name)) {
				return elementType;
			}
		}
		return NONE;
	}

	public static ElementType getElementType(BlockState state) {
		if (state.hasProperty(STATE_PROPERTY)) {
			return state.getValue(STATE_PROPERTY);
		} else if (state.getBlock() instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.NONE;
	}

	public static <T> RecordCodecBuilder<T, ElementType> forGetter(final Function<T, ElementType> getter) {
		return CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(getter);
	}
}
