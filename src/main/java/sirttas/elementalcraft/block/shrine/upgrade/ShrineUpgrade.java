package sirttas.elementalcraft.block.shrine.upgrade;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.tag.ECTags;

public class ShrineUpgrade {

	public static final Serializer SEZRIALIZER = new Serializer();

	private Predicate<Block> predicate;
	private String predicateInfos;
	private final Map<BonusType, Float> bonuses;
	private int maxAmount;
	private ResourceLocation id;
	private final List<ResourceLocation> incompatibilitiesLocs;
	private Set<ShrineUpgrade> incompatibilities;

	ShrineUpgrade() {
		this.predicate = null;
		this.bonuses = new EnumMap<>(BonusType.class);
		this.maxAmount = 0;
		this.id = null;
		this.incompatibilitiesLocs = Lists.newArrayList();
	}

	boolean canUpgrade(TileShrine shrine) {
		return predicate.test(shrine.getBlockState().getBlock()) && (maxAmount == 0 || shrine.getUpgradeCount(this) < maxAmount)
				&& shrine.getAllUpgrades().stream().noneMatch(incompatibilities::contains);
	}

	public final Map<BonusType, Float> getBonuses() {
		return bonuses;
	}

	protected final void setId(ResourceLocation id) {
		this.id = id;
	}

	protected final void refresh(ShrineUpgradeManager manager) {
		incompatibilities = incompatibilitiesLocs.stream().map(manager::getUpgrade).collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return id != null ? id.toString() : super.toString();
	}


	public void addInformation(List<ITextComponent> tooltip) {
		bonuses.forEach(
				(type, multiplier) -> tooltip.add(new TranslationTextComponent("shrine_upgrade_bonus.elementalcraft." + type.getString(), String.format("%+d%%", Math.round((multiplier - 1) * 100)))
						.mergeStyle(type.isPositive() ^ multiplier < 1 ? TextFormatting.BLUE : TextFormatting.RED)));
		if (maxAmount > 0) {
			tooltip.add(new StringTextComponent(""));
			tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.max_amount", maxAmount).mergeStyle(TextFormatting.YELLOW));
		}
		if (!incompatibilitiesLocs.isEmpty()) {
			if (maxAmount <= 0) {
				tooltip.add(new StringTextComponent(""));
			}
			tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.incompatibilities").mergeStyle(TextFormatting.YELLOW));
			incompatibilitiesLocs.forEach(loc -> tooltip.add(new StringTextComponent("    ").append(ForgeRegistries.BLOCKS.getValue(loc).getTranslatedName().mergeStyle(TextFormatting.YELLOW))));
		}
	}

	public enum BonusType implements IStringSerializable {
		NONE("none", false), SPEED("speed", false), ELEMENT_CONSUMPTION("element_consuption", false), CAPACITY("capacity", true), RANGE("range", true);

		private final String name;
		private final boolean positive;

		private BonusType(String name, boolean positive) {
			this.name = name;
			this.positive = positive;
		}

		@Nonnull
		@Override
		public String getString() {
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

	public static class Serializer implements JsonDeserializer<ShrineUpgrade> {

		private Serializer() {
		}

		@Override
		public ShrineUpgrade deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
			ShrineUpgrade upgrade = new ShrineUpgrade();
			JsonObject jsonObject = (JsonObject) json;
			JsonObject matchJson = JSONUtils.getJsonObject(jsonObject, ECNames.PREDICATE);

			upgrade.predicate = readPredicate(matchJson);
			upgrade.predicateInfos = matchJson.toString();

			for (JsonElement bonusJson : JSONUtils.getJsonArray(jsonObject, ECNames.BONUSES)) {
				if (bonusJson.isJsonObject()) {
					JsonObject bonusjsonObject = (JsonObject) bonusJson;

					upgrade.bonuses.put(BonusType.byName(JSONUtils.getString(bonusjsonObject, ECNames.TYPE)), JSONUtils.getFloat(bonusjsonObject, ECNames.MULTIPLIER));
				} else {
					throw new JsonSyntaxException("Malformated shrime upgrade bonus");
				}
			}
			if (jsonObject.has(ECNames.MAX_AMOUNT)) {
				upgrade.maxAmount = JSONUtils.getInt(jsonObject, ECNames.MAX_AMOUNT);
			}
			if (jsonObject.has(ECNames.INCOMPATIBILITIES)) {
				JsonArray incompatibleJson = JSONUtils.getJsonArray(jsonObject, ECNames.INCOMPATIBILITIES);

				incompatibleJson.forEach(j -> {
					if (j.isJsonPrimitive() && j.getAsJsonPrimitive().isString()) {
						upgrade.incompatibilitiesLocs.add(new ResourceLocation(j.getAsString()));
					} else {
						throw new JsonSyntaxException("Malformated shrime upgrade incompatibility");
					}
				});
			}

			return upgrade;
		}

		private Predicate<Block> readPredicate(JsonObject matchJson) {
			if (matchJson.has(ECNames.BLOCK)) {
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(matchJson, ECNames.BLOCK)));

				return block::equals;
			} else if (matchJson.has(ECNames.TAG)) {
				ResourceLocation loc = new ResourceLocation(JSONUtils.getString(matchJson, ECNames.TAG));
				ITag<Block> tag = ECTags.Blocks.getTag(loc);

				if (tag == null) {
					throw new JsonSyntaxException("Unknown block tag '" + loc + "'");
				}
				return tag::contains;
			}
			throw new JsonSyntaxException("No block or tag in shrine upgrade");
		}

		public ShrineUpgrade read(PacketBuffer buf) {
			ShrineUpgrade upgrade = new ShrineUpgrade();

			upgrade.predicateInfos = buf.readString();
			upgrade.predicate = readPredicate(new Gson().fromJson(upgrade.predicateInfos, JsonObject.class));
			upgrade.maxAmount = buf.readInt();
			IntStream.range(0, buf.readInt()).forEach(i -> upgrade.bonuses.put(BonusType.byName(buf.readString()), buf.readFloat()));
			IntStream.range(0, buf.readInt()).forEach(i -> upgrade.incompatibilitiesLocs.add(buf.readResourceLocation()));
			return upgrade;
		}

		public void write(ShrineUpgrade upgrade, PacketBuffer buf) {
			buf.writeString(upgrade.predicateInfos);
			buf.writeInt(upgrade.maxAmount);
			buf.writeInt(upgrade.bonuses.size());
			upgrade.bonuses.forEach((type, multiplier) -> {
				buf.writeString(type.getString());
				buf.writeFloat(multiplier);
			});
			buf.writeInt(upgrade.incompatibilitiesLocs.size());
			upgrade.incompatibilitiesLocs.forEach(buf::writeResourceLocation);
		}
	}

	public static class Builder {

		private INamedTag<Block> tag;
		private Block block;
		private final Map<BonusType, Float> bonuses;
		private int maxAmount;
		private Set<ResourceLocation> incompatibilities;

		public Builder() {
			this.bonuses = new EnumMap<>(BonusType.class);
			this.tag = null;
			this.block = null;
			this.maxAmount = 0;
			this.incompatibilities = Sets.newHashSet();
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder block(Block block) {
			this.block = block;
			this.tag = null;
			return this;
		}

		public Builder tag(INamedTag<Block> tag) {
			this.tag = tag;
			this.block = null;
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

		public Builder addBonus(BonusType type, float value) {
			this.bonuses.put(type, value);
			return this;
		}

		public JsonElement toJson() {
			JsonObject json = new JsonObject();
			JsonObject predicate = new JsonObject();
			JsonArray bonusesJson = new JsonArray();

			if (maxAmount > 0) {
				json.addProperty(ECNames.MAX_AMOUNT, maxAmount);
			}
			if (tag != null) {
				predicate.addProperty(ECNames.TAG, tag.getName().toString());
			} else if (block != null) {
				predicate.addProperty(ECNames.BLOCK, block.getRegistryName().toString());
			} else {
				throw new IllegalStateException("No block or tag in shrine upgrade");
			}
			json.add(ECNames.PREDICATE, predicate);
			bonuses.forEach((type, value) -> {
				JsonObject bonus = new JsonObject();

				bonus.addProperty(ECNames.TYPE, type.getString());
				bonus.addProperty(ECNames.MULTIPLIER, value);
				bonusesJson.add(bonus);
			});
			json.add(ECNames.BONUSES, bonusesJson);
			if (!incompatibilities.isEmpty()) {
				JsonArray incompatibleJson = new JsonArray();

				incompatibilities.forEach(l -> incompatibleJson.add(l.toString()));
				json.add(ECNames.INCOMPATIBILITIES, incompatibleJson);
			}
			return json;
		}
	}
}
