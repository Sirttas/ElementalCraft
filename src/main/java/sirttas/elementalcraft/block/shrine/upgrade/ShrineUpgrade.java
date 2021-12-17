package sirttas.elementalcraft.block.shrine.upgrade;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag.Named;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.dpanvil.api.predicate.block.logical.OrBlockPredicate;
import sirttas.elementalcraft.api.upgrade.AbstractUpgrade;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;

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
		return canUpgrade(shrine.getLevel(), shrine.getBlockPos(), count);
	}

	public void addInformation(List<Component> tooltip) {
		bonuses.forEach((type, multiplier) -> tooltip.add(new TranslatableComponent("shrine_upgrade_bonus.elementalcraft." + type.getSerializedName(), formatMultiplier(multiplier))
				.withStyle(type.isPositive() ^ multiplier < 1 ? ChatFormatting.BLUE : ChatFormatting.RED)));
		if (maxAmount > 0) {
			tooltip.add(new TextComponent(""));
			tooltip.add(new TranslatableComponent("tooltip.elementalcraft.max_amount", maxAmount).withStyle(ChatFormatting.YELLOW));
		}
	}

	private String formatMultiplier(Float multiplier) {
		if (multiplier >= 10) {
			return new DecimalFormat("�#.##").format(multiplier);
		}
		return String.format("%+d%%", Math.round((multiplier - 1) * 100));
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
		NONE("none", false), 
		SPEED("speed", false), 
		ELEMENT_CONSUMPTION("element_consumption", false), 
		CAPACITY("capacity", true), 
		RANGE("range", true), 
		STRENGTH("strength", true);

		public static final Codec<BonusType> CODEC = StringRepresentable.fromEnum(BonusType::values, BonusType::byName);

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

	public static class Builder {

		public static final Encoder<Builder> ENCODER = ShrineUpgrade.CODEC.comap(builder -> new ShrineUpgrade(builder.predicate, builder.bonuses, builder.maxAmount));

		private IBlockPosPredicate predicate;
		private final Map<BonusType, Float> bonuses;
		private int maxAmount;
		private final Set<ResourceLocation> incompatibilities;

		private Builder() {
			this.bonuses = new EnumMap<>(BonusType.class);
			this.predicate = null;
			this.maxAmount = 0;
			this.incompatibilities = Sets.newHashSet();
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder match(Block... block) {
			return predicate(IBlockPosPredicate.match(block));
		}

		public Builder match(Named<Block> tag) {
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

		public Builder incompatibleWith(ResourceLocation... locs) {
			Collections.addAll(incompatibilities, locs);
			return this;
		}
		
        @SafeVarargs
        public final Builder incompatibleWith(IDataWrapper<ShrineUpgrade>... upgrades) {
            Stream.of(upgrades)
                    .map(IDataWrapper::getId)
                    .forEach(incompatibilities::add);
            return this;
        }

		public Builder addBonus(BonusType type, float value) {
			this.bonuses.put(type, value);
			return this;
		}

		public JsonElement toJson() {
			if (!incompatibilities.isEmpty()) {
				predicate = predicate.and(getIncompatibilitiesPredicate());
			}
			return CodecHelper.encode(ENCODER, this);
		}

		private IBlockPosPredicate getIncompatibilitiesPredicate() {
			return (incompatibilities.size() == 1 ? new HasShrineUpgradePredicate(Iterables.getOnlyElement(incompatibilities))
					: new OrBlockPredicate(incompatibilities.stream().map(HasShrineUpgradePredicate::new).collect(Collectors.toList()))).not();
		}
	}
}
