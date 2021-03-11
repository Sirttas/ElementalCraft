package sirttas.elementalcraft.api.element;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.api.name.ECNames;

public enum ElementType implements IStringSerializable, IElementTypeProvider {

	NONE(0, 0, 0, "none"), WATER(43, 173, 255, "water"), FIRE(247, 107, 27, "fire"), EARTH(76, 133, 102, "earth"), AIR(238, 255, 219, "air");

	public static final Codec<ElementType> CODEC = IStringSerializable.createEnumCodec(ElementType::values, ElementType::byName);
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
		switch (random) {
		case 0:
			return WATER;
		case 1:
			return FIRE;
		case 2:
			return EARTH;
		case 3:
			return AIR;
		default:
			return NONE;
		}
	}

	@Nonnull
	@Override
	public String getString() {
		return this.name;
	}

	@Override
	public ElementType getElementType() {
		return this;
	}

	public String getTranslationKey() {
		return "element.elementalcraft." + getString();
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(getTranslationKey());
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
		return state.get(STATE_PROPERTY);
	}

	public static List<ElementType> allValid() {
		return Stream.of(values()).filter(type -> type != NONE).collect(Collectors.toList());
	}

	public static <T> RecordCodecBuilder<T, ElementType> forGetter(final Function<T, ElementType> getter) {
		return CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(getter);
	}
}
