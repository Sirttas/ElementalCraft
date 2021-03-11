package sirttas.elementalcraft.rune;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.rune.handler.IRuneHandler;
import sirttas.elementalcraft.upgrade.AbstractUpgrade;

public class Rune extends AbstractUpgrade<Rune.BonusType> {

	public static final Codec<Rune> CODEC = RecordCodecBuilder.create(builder -> codec(builder, BonusType.CODEC).and(builder.group(
			ResourceLocation.CODEC.fieldOf(ECNames.MODEL).forGetter(Rune::getModelName),
			ResourceLocation.CODEC.fieldOf(ECNames.EFFECT_SPRITE).forGetter(Rune::getSpriteName)
	)).apply(builder, Rune::new));

	private ResourceLocation modelName;
	private ResourceLocation fxSpriteName;

	private Rune(IBlockPosPredicate predicate, Map<BonusType, Float> bonuses, int maxAmount, ResourceLocation modelName, ResourceLocation fxSpriteName) {
		super(predicate, new EnumMap<>(bonuses), maxAmount);
		this.modelName = modelName;
		this.fxSpriteName = fxSpriteName;
	}

	public boolean canUpgrade(IWorldReader world, BlockPos pos, IRuneHandler handler) {
		return handler.getRuneCount() < handler.getMaxRunes() && canUpgrade(world, pos, handler.getRuneCount(this));
	}

	public void addInformation(List<ITextComponent> tooltip) {
		bonuses.forEach((type, multiplier) -> tooltip.add(
				new TranslationTextComponent("rune_bonus.elementalcraft." + type.getString(), formatMultiplier(multiplier)).mergeStyle(multiplier > 0 ? TextFormatting.BLUE : TextFormatting.RED)));
		if (maxAmount > 0) {
			tooltip.add(new StringTextComponent(""));
			tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.max_amount", maxAmount).mergeStyle(TextFormatting.YELLOW));
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

	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public RenderMaterial getSprite() {
		return new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, fxSpriteName);
	}

	public ITextComponent getDisplayName() {
		ResourceLocation id = getId();

		return new TranslationTextComponent("elementalcraft_rune." + id.getNamespace() + '.' + id.getPath());
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

	public enum BonusType implements IStringSerializable {
		NONE("none"), SPEED("speed"), ELEMENT_PRESERVATION("element_preservation"), LUCK("luck");

		public static final Codec<BonusType> CODEC = IStringSerializable.createEnumCodec(BonusType::values, BonusType::byName);

		private final String name;

		private BonusType(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getString() {
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

		public Builder match(INamedTag<Block> tag) {
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
