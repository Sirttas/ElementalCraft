package sirttas.elementalcraft;

import java.util.Random;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

import net.minecraft.block.BlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.property.ECProperties;

public enum ElementType implements IStringSerializable {

	NONE(0, 0, 0, "none"), WATER(43, 173, 255, "water"), FIRE(247, 107, 27, "fire"), EARTH(76, 133, 102, "earth"), AIR(238, 255, 219, "air");

	public static final Codec<ElementType> CODEC = new PrimitiveCodec<ElementType>() {

		@Override
		public <T> DataResult<ElementType> read(final DynamicOps<T> ops, final T input) {
			return ops.getStringValue(input).map(ElementType::byName);
		}

		@Override
		public <T> T write(final DynamicOps<T> ops, final ElementType value) {
			return ops.createString(value.toString());
		}
		
		@Override
		public String toString() {
			return "ElementType";
		}
	};

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
		this.color = (r << 16) | (g << 8) | b;
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

	public static ElementType byName(String name) {
		for (ElementType elementType : values()) {
			if (elementType.name.equals(name)) {
				return elementType;
			}
		}
		return NONE;
	}

	public static ElementType getElementType(BlockState state) {
		return state.get(ECProperties.ELEMENT_TYPE);
	}

	public String getTranslationKey() {
		return "element.elementalcraft." + getString();
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(getTranslationKey());
	}

}
