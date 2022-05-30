package sirttas.elementalcraft.api.rune;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.upgrade.AbstractUpgrade;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Rune extends AbstractUpgrade<Rune.BonusType> {

	public static final String NAME = "runes";
	public static final String FOLDER = ElementalCraftApi.MODID + '_' + NAME;
	
	public static final Codec<Rune> CODEC = RecordCodecBuilder.create(builder -> codec(builder, BonusType.CODEC).and(builder.group(
			ResourceLocation.CODEC.fieldOf(ECNames.MODEL).forGetter(Rune::getModelName),
			ResourceLocation.CODEC.fieldOf(ECNames.EFFECT_SPRITE).forGetter(Rune::getSpriteName)
	)).apply(builder, Rune::new));

	private final ResourceLocation modelName;
	private final ResourceLocation fxSpriteName;
	
	@OnlyIn(Dist.CLIENT)
	private Material sprite;

	private Rune(IBlockPosPredicate predicate, Map<BonusType, Float> bonuses, int maxAmount, ResourceLocation modelName, ResourceLocation fxSpriteName) {
		super(predicate, new EnumMap<>(bonuses), maxAmount);
		this.modelName = modelName;
		this.fxSpriteName = fxSpriteName;
	}

	public boolean canUpgrade(LevelReader world, BlockPos pos, IRuneHandler handler) {
		return handler.getRuneCount() < handler.getMaxRunes() && canUpgrade(world, pos, handler.getRuneCount(this));
	}

	public void addInformation(List<Component> tooltip) {
		bonuses.forEach((type, multiplier) -> tooltip.add(
				new TranslatableComponent("rune_bonus.elementalcraft." + type.getSerializedName(), formatMultiplier(multiplier)).withStyle(multiplier > 0 ? ChatFormatting.BLUE : ChatFormatting.RED)));
		if (maxAmount > 0) {
			tooltip.add(new TextComponent(""));
			tooltip.add(new TranslatableComponent("tooltip.elementalcraft.max_amount", maxAmount).withStyle(ChatFormatting.YELLOW));
		}
	}

	private String formatMultiplier(Float multiplier) {
		return String.format("%+d%%", Math.round(multiplier * 100));
	}

	public ResourceLocation getModelName() {
		return modelName;
	}

	public ResourceLocation getSpriteName() {
		return fxSpriteName;
	}

	@OnlyIn(Dist.CLIENT)
	public Material getSprite() {
		if (sprite == null) {
			sprite = ForgeHooksClient.getBlockMaterial(fxSpriteName);
		}
		return sprite;
	}

	public Component getDisplayName() {
		ResourceLocation id = getId();

		return new TranslatableComponent("elementalcraft_rune." + id.getNamespace() + '.' + id.getPath());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Rune) {
			return super.equals(other);
		}
		return false;
	}
	
	public static Rune merge(Stream<Rune> runes) {
		AtomicReference<Rune> atomicValue = new AtomicReference<>();
		
		runes.forEach(rune -> {
			Rune value = atomicValue.get();
			
			if (value == null) {
				atomicValue.set(rune);
			} else {
				value.merge(rune);
			}
		});
		return atomicValue.get();
	}

	public enum BonusType implements StringRepresentable {
		NONE(ECNames.NONE),
		SPEED(ECNames.SPEED),
		ELEMENT_PRESERVATION(ECNames.ELEMENT_PRESERVATION),
		LUCK(ECNames.LUCK);

		public static final Codec<BonusType> CODEC = StringRepresentable.fromEnum(BonusType::values, BonusType::byName);

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
		public static final Encoder<Builder> ENCODER = Rune.CODEC.comap(builder -> new Rune(builder.predicate, builder.bonuses, builder.maxAmount, builder.model, builder.sprite));

		private IBlockPosPredicate predicate;
		private final Map<BonusType, Float> bonuses;
		private int maxAmount;
		private ResourceLocation model;
		private ResourceLocation sprite;

		private Builder() {
			this.bonuses = new EnumMap<>(BonusType.class);
			this.predicate = null;
			this.maxAmount = 0;
			this.model = null;
			this.sprite = null;
		}

		public static Builder create() {
			return new Builder();
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

		public Builder model(ItemModelBuilder model) {
			ResourceLocation modelLoc = model.getLocation();
			
			this.model = new ResourceLocation(modelLoc.getNamespace(), modelLoc.getPath().substring(5));
			return this;
		}

		public Builder sprite(ResourceLocation sprite) {
			this.sprite = sprite;
			return this;
		}

		public Builder addBonus(BonusType type, float value) {
			this.bonuses.put(type, value);
			return this;
		}

		public JsonElement toJson() {
			return CodecHelper.encode(ENCODER, this);
		}
	}
}
