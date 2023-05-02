package sirttas.elementalcraft.block.shrine.upgrade;

import com.google.common.collect.Iterables;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.dpanvil.api.predicate.block.world.CacheBlockPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.upgrade.AbstractUpgrade;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ShrineUpgrade extends AbstractUpgrade<ShrineUpgrade.BonusType> {


	public static final Codec<ShrineUpgrade> CODEC = RecordCodecBuilder.create(builder -> AbstractUpgrade.codec(builder, BonusType.CODEC).apply(builder, ShrineUpgrade::new));

	private ShrineUpgrade(IBlockPosPredicate predicate, Map<BonusType, Float> bonuses, int maxAmount) {
		super(predicate, new EnumMap<>(bonuses), maxAmount);
	}

	boolean canUpgrade(AbstractShrineBlockEntity shrine, boolean update) {
		int count = shrine.getUpgradeCount(this);
		
		if (update && count > 0) {
			count--;
		}
		return canUpgrade(shrine.getLevel(), shrine.getBlockPos(), null, count);
	}

	public void addInformation(List<Component> tooltip) {
		bonuses.forEach((type, multiplier) -> tooltip.add(Component.translatable("shrine_upgrade_bonus.elementalcraft." + type.getSerializedName(), formatMultiplier(multiplier))
				.withStyle(type.isPositive() ^ multiplier < 1 ? ChatFormatting.BLUE : ChatFormatting.RED)));
		if (maxAmount > 0) {
			tooltip.add(Component.empty());
			tooltip.add(Component.translatable("tooltip.elementalcraft.max_amount", maxAmount).withStyle(ChatFormatting.YELLOW));
		}
	}

	private String formatMultiplier(Float multiplier) {
		if (multiplier >= 10) {
			return new DecimalFormat("\u00D7#.##").format(multiplier);
		}
		return String.format("%+d%%", Math.round((multiplier - 1) * 100));
	}

	public boolean is(ResourceKey<ShrineUpgrade> key) {
		return key.location().equals(this.getId());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ShrineUpgrade) {
			return super.equals(other);
		}
		return false;
	}
	
	public static ShrineUpgrade merge(Stream<ShrineUpgrade> upgrades) {
		AtomicReference<ShrineUpgrade> atomicValue = new AtomicReference<>();
		
		upgrades.forEach(upgrade -> {
			ShrineUpgrade value = atomicValue.get();
			
			if (value == null) {
				atomicValue.set(upgrade);
			} else {
				value.merge(upgrade);
			}
		});
		return atomicValue.get();
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		public static final Encoder<Builder> ENCODER = ShrineUpgrade.CODEC.comap(builder -> new ShrineUpgrade(builder.predicate, builder.bonuses, builder.maxAmount));

		private IBlockPosPredicate predicate;
		private final Map<BonusType, Float> bonuses;
		private int maxAmount;
		private final List<ResourceKey<ShrineUpgrade>> incompatibilities;

		private Builder() {
			this.bonuses = new EnumMap<>(BonusType.class);
			this.predicate = null;
			this.maxAmount = 0;
			this.incompatibilities = new ArrayList<>();
		}

		public Builder match(Block... block) {
			return predicate(IBlockPosPredicate.match(block));
		}

		public Builder match(TagKey<Block> tag) {
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

		@SafeVarargs
		public final Builder incompatibleWith(ResourceKey<ShrineUpgrade>... upgrades) {
			incompatibilities.addAll(Arrays.asList(upgrades));
			return this;
		}

		public Builder addBonus(BonusType type, float value) {
			this.bonuses.put(type, value);
			return this;
		}

		public JsonElement toJson() {
			if (!incompatibilities.isEmpty()) {
				predicate = (predicate instanceof CacheBlockPredicate cacheBlockPredicate ? cacheBlockPredicate.predicate().and(getIncompatibilitiesPredicate()).cache() : predicate.and(getIncompatibilitiesPredicate())).simplify();
			}
			return CodecHelper.encode(ENCODER, this);
		}

		private IBlockPosPredicate getIncompatibilitiesPredicate() {
			return (incompatibilities.size() == 1
					? new HasShrineUpgradePredicate(Iterables.getOnlyElement(incompatibilities))
					: IBlockPosPredicate.createOr(incompatibilities.stream()
							.map(HasShrineUpgradePredicate::new)
							.toArray(HasShrineUpgradePredicate[]::new))
			).not();
		}
	}
}
