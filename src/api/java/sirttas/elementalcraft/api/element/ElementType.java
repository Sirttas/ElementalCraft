package sirttas.elementalcraft.api.element;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.resource.Resource;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public enum ElementType implements StringRepresentable, IElementTypeProvider, Resource {

	NONE("none", 0, 0, 0),
	WATER("water", 43, 173, 255),
	FIRE("fire", 247, 107, 27),
	EARTH("earth", 13, 128, 37),
	AIR("air", 238, 255, 219);

	public static final List<ElementType> ALL_VALID = ImmutableList.copyOf(Stream.of(values()).filter(type -> type != NONE).toList());
	public static final Codec<ElementType> CODEC = StringRepresentable.fromEnum(ElementType::values);
	public static final StreamCodec<@NotNull ByteBuf, @NotNull ElementType> STREAM_CODEC = ByteBufCodecs.INT.map(i -> values()[i], Enum::ordinal);
	
	private final String name;
	private final float r;
	private final float g;
	private final float b;
	private final int color;

    ElementType(String name, int r, int g, int b) {
        this.name = name;
        this.r = r / 255F;
		this.g = g / 255F;
		this.b = b / 255F;
		this.color = ARGB.color(r, g, b);
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
		return random(RandomSource.create());
	}

	public static ElementType random(RandomSource rand) {
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
	public @NotNull ElementType getElementType() {
		return this;
	}

    @Override
    public boolean isEmpty() {
        return this == NONE;
    }

	public String getDescriptionId() {
		return "element.elementalcraft." + getSerializedName();
	}

	public Component getDisplayName() {
		return Component.translatable(getDescriptionId());
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
		if (state.getBlock() instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.NONE;
	}

	public static ElementType getElementType(ItemStack stack) {
		if (stack.isEmpty()) {
			return ElementType.NONE;
		} else if (stack.getItem() instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.NONE;
	}

	public static <T> RecordCodecBuilder<T, ElementType> forGetter(final Function<T, ElementType> getter) {
		return CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(getter);
	}
}
