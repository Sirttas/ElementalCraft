package sirttas.elementalcraft.api.block.shrine.upgrade;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.TooltipFlag;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.upgrade.AbstractUpgrade;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.function.Consumer;

public class ShrineUpgrade extends AbstractUpgrade<ShrineUpgrade.BonusType> {

	public static final Codec<ShrineUpgrade> CODEC = RecordCodecBuilder.create(builder -> AbstractUpgrade.codec(builder, BonusType.CODEC).apply(builder, ShrineUpgrade::new));

	public ShrineUpgrade(IBlockPosPredicate predicate, Map<BonusType, Float> bonuses, int maxAmount) {
		super(predicate, Maps.immutableEnumMap(bonuses), maxAmount);
	}

	public void addInformation(Consumer<Component> builder, @Nonnull TooltipFlag flag) {
		getBonuses().forEach((type, multiplier) -> builder.accept(Component.translatable("shrine_upgrade_bonus.elementalcraft." + type.getSerializedName(), formatMultiplier(multiplier))
				.withStyle(type.isPositive() ^ multiplier < 1 ? ChatFormatting.BLUE : ChatFormatting.RED)));
		if (maxAmount > 0) {
			builder.accept(Component.empty());
			builder.accept(Component.translatable("tooltip.elementalcraft.max_amount", maxAmount).withStyle(ChatFormatting.YELLOW));
		}
		if (flag.isAdvanced()) {
            this.getPredicateTooltip().forEach(builder);
		}
	}

	private String formatMultiplier(Float multiplier) {
		if (multiplier >= 10) {
			return new DecimalFormat("\u00D7#.##").format(multiplier);
		}
		return String.format("%+d%%", Math.round((multiplier - 1) * 100));
	}
	
	public enum BonusType implements StringRepresentable {
		NONE(ECNames.NONE, false),
		SPEED(ECNames.SPEED, false),
		ELEMENT_CONSUMPTION(ECNames.ELEMENT_CONSUMPTION, false), 
		CAPACITY(ECNames.ELEMENT_CAPACITY, true),
		RANGE(ECNames.RANGE, true),
		STRENGTH(ECNames.STRENGTH, true);

		public static final Codec<BonusType> CODEC = StringRepresentable.fromEnum(BonusType::values);

		private final String name;
		private final boolean positive;

		BonusType(String name, boolean positive) {
			this.name = name;
			this.positive = positive;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}

		public boolean isPositive() {
			return positive;
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
}
