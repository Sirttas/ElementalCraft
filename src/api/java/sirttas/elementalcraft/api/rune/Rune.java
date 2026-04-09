package sirttas.elementalcraft.api.rune;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.upgrade.AbstractUpgrade;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class Rune extends AbstractUpgrade<Rune.BonusType> {
	
	public static final Codec<Rune> CODEC = RecordCodecBuilder.create(builder -> codec(builder, BonusType.CODEC).apply(builder, Rune::new));

	private Rune(IBlockPosPredicate predicate, Map<BonusType, Float> bonuses, int maxAmount) {
		super(predicate, Maps.immutableEnumMap(bonuses), maxAmount);
	}

	public void appendHoverText(@NotNull Consumer<Component> builder, @Nonnull TooltipFlag flag) {
		getBonuses().forEach((type, multiplier) -> builder.accept(Component.translatable("rune_bonus.elementalcraft." + type.getSerializedName(), formatMultiplier(multiplier)).withStyle(multiplier > 0 ? ChatFormatting.BLUE : ChatFormatting.RED)));
		if (maxAmount > 0) {
            builder.accept(Component.empty());
            builder.accept(Component.translatable("tooltip.elementalcraft.max_amount", maxAmount).withStyle(ChatFormatting.YELLOW));
		}
		if (flag.isAdvanced()) {
            this.getPredicateTooltip().forEach(builder);
		}
	}

	private String formatMultiplier(Float multiplier) {
		return String.format("%+d%%", Math.round(multiplier * 100));
	}

	public enum BonusType implements StringRepresentable {
		NONE(ECNames.NONE),
		SPEED(ECNames.SPEED),
		ELEMENT_PRESERVATION(ECNames.ELEMENT_PRESERVATION),
		LUCK(ECNames.LUCK),
		RANGE(ECNames.RANGE);

		public static final Codec<BonusType> CODEC = StringRepresentable.fromEnum(BonusType::values);

		private final String name;

		BonusType(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static BonusType byName(String name) {
			for (BonusType bonusType : values()) {
				if (bonusType.name.equals(name)) {
					return bonusType;
				}
			}
			return NONE;
		}
	}

	public static class Builder {
		public static final Encoder<Builder> ENCODER = Rune.CODEC.comap(builder -> new Rune(builder.predicate, builder.bonuses, builder.maxAmount));

		private IBlockPosPredicate predicate;
		private final Map<BonusType, Float> bonuses;
		private int maxAmount;

		private Builder() {
			this.bonuses = new EnumMap<>(BonusType.class);
			this.predicate = null;
			this.maxAmount = 0;
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder match(Block... block) {
			return predicate(IBlockPosPredicate.match(block));
		}

		public Builder match(TagKey<@NotNull Block> tag) {
			return predicate(IBlockPosPredicate.match(tag));
		}

		public Builder predicate(IBlockPosPredicate predicate) {
			this.predicate = predicate;
			return this;
		}

		public Builder max(int max) {
			maxAmount = max;
			return this;
		}

		public Builder addBonus(BonusType type, float value) {
			this.bonuses.put(type, value);
			return this;
		}
	}
}
