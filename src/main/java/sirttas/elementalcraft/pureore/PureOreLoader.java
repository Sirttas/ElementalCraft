package sirttas.elementalcraft.pureore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PureOreLoader(
		HolderSet<Item> source,
		Optional<ResourceLocation> fixedName,
		Optional<Pattern> tagPattern,
		List<Pattern> patterns,
		int elementConsumption,
		int inputSize,
		int outputSize,
		double luckRatio
) {
	public static final String NAME = "pure_ore_loaders";
	public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;

	public static Codec<PureOreLoader> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			RegistryCodecs.homogeneousList(Registry.ITEM_REGISTRY).fieldOf("source").forGetter(PureOreLoader::source),
			ResourceLocation.CODEC.optionalFieldOf("fixed_name").forGetter(PureOreLoader::fixedName),
			Codecs.PATTERN.optionalFieldOf("tag_pattern").forGetter(PureOreLoader::tagPattern),
			Codecs.PATTERN.listOf().optionalFieldOf("patterns", Collections.emptyList()).forGetter(PureOreLoader::patterns),
			Codec.INT.optionalFieldOf(ECNames.ELEMENT_CONSUMPTION, 2500).forGetter(PureOreLoader::elementConsumption),
			Codec.INT.optionalFieldOf("input_size", 1).forGetter(PureOreLoader::inputSize),
			Codec.INT.optionalFieldOf("output_size", 2).forGetter(PureOreLoader::outputSize),
			Codec.DOUBLE.optionalFieldOf("luck_ratio", 0D).forGetter(PureOreLoader::luckRatio)
	).apply(builder, PureOreLoader::new));

	private static final Pattern DEEPSLATE_PATTERN = Pattern.compile("^deepslate_");


	public PureOreLoader {
		if (fixedName.isEmpty() && tagPattern.isEmpty()) {
			throw new IllegalArgumentException("Either fixed_name or tag_folder must be set");
		}
		patterns = List.copyOf(patterns);
	}
	public List<PureOre> generate(Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> injectors) {
		return List.copyOf(this.generatePureOres(injectors).values());
	}

	private Map<ResourceLocation, PureOre> generatePureOres(Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> injectors) {
		var list = streamSourceTag().toList();

		if (list.isEmpty()) {
			ElementalCraftApi.LOGGER.debug("No source items found for {}", this::getId);
			return Collections.emptyMap();
		}

		Map<ResourceLocation, PureOre> pureOres = new HashMap<>();
		ElementalCraftApi.LOGGER.info("Loading pure ores: {}.\r\n\tSource ores: {}",
				this::getId,
				() -> streamSourceTag()
						.mapMulti(ElementalCraftUtils.cast(Holder.Reference.class))
						.map(r -> r.key().location().toString())
						.collect(Collectors.joining(", ")));

		list.forEach(holder -> {
			var ore = holder.value();
			PureOre entry = findOrCreateEntry(pureOres, ore);
			boolean isInBlacklist = holder.is(ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST);

			injectors.forEach(injector -> {
				if (!injector.isModProcessing() || !isInBlacklist) {
					injector.getRecipe(ore).ifPresent(entry::addRecipe);
				}
			});
		});
		return pureOres;
	}

	@Nonnull
	public ResourceLocation getId() {
		return ElementalCraft.PURE_ORE_LOADERS_MANAGER.getId(this);
	}

	@Nonnull
	private Stream<Holder<Item>> streamSourceTag() {
		return this.source.stream();
	}

	private PureOre findOrCreateEntry(Map<ResourceLocation, PureOre> pureOres, Item ore) {
		for (PureOre pureOre : pureOres.values()) {
			if (pureOre.contains(ore)) {
				return pureOre;
			}
		}

		ResourceLocation id;
		List<TagKey<Item>> tags;

		if (fixedName.isPresent()) {
			id = fixedName.get();
			tags = Collections.emptyList();
		} else {
			id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ore));
			tags = ore.builtInRegistryHolder().tags()
					.filter(t -> {
						var location = t.location();

						return location.getNamespace().equals(ECNames.FORGE) && tagPattern.get().matcher(location.getPath()).find();
					}).toList();

			if (!tags.isEmpty()) {
				if (tags.size() > 1) {
					ElementalCraftApi.LOGGER.warn("Item {} has multiple tags matching {}:\r\n\t{}",
							id::toString,
							() -> tagPattern.map(Pattern::pattern).orElse(""),
							() -> tags.stream().map(t -> t.location().toString()).collect(Collectors.joining(", ")));
				}
				id = new ResourceLocation(ECNames.FORGE, cleanPath(tagPattern.get().matcher(tags.get(0).location().getPath()).replaceAll("")));
			} else {
				id = new ResourceLocation(id.getNamespace(), cleanPath(id.getPath()));
			}
		}
		
		var entry = pureOres.computeIfAbsent(id, i -> new PureOre(i, elementConsumption, inputSize, outputSize, luckRatio));

		entry.getOres().add(ore);
		tags.forEach(entry::addTag);
		return entry;
	}

	private String cleanPath(String path) {
		path = DEEPSLATE_PATTERN.matcher(path).replaceAll("");
		for (var pattern : patterns) {
			path = pattern.matcher(path).replaceAll("");
		}
		return path;
	}

	public static Builder builder(HolderSet<Item> source) {
		return new Builder(source);
	}
	public static class Builder {

		public static final Encoder<Builder> ENCODER = PureOreLoader.CODEC.comap(builder -> new PureOreLoader(
				builder.source,
				Optional.ofNullable(builder.fixedName),
				StringUtils.isNotBlank(builder.tagPattern) ? Optional.of(Pattern.compile(builder.tagPattern)) : Optional.empty(),
				builder.patterns.stream()
						.map(Pattern::compile)
						.toList(),
				builder.elementConsumption,
				builder.inputSize,
				builder.outputSize,
				builder.luckRatio)
		);

		private final HolderSet<Item> source;
		private ResourceLocation fixedName;
		private String tagPattern;
		private final List<String> patterns;
		private int elementConsumption;
		private int inputSize;
		private int outputSize;
		private double luckRatio;

		private Builder(HolderSet<Item> source) {
			this.source = source;
			this.elementConsumption = 2500;
			this.inputSize = 1;
			this.outputSize = 2;
			this.luckRatio = 0;
			this.patterns = new ArrayList<>();
			this.tagPattern = "";
		}

		public Builder fixedName(ResourceLocation fixedName) {
			if (StringUtils.isNotBlank(this.tagPattern)) {
				throw new IllegalStateException("Cannot set fixed name when tag pattern is set");
			}
			this.fixedName = fixedName;
			return this;
		}

		public Builder tagPattern(String tagPattern) {
			if (this.fixedName != null) {
				throw new IllegalStateException("Cannot set tag pattern when fixed name is set");
			}
			this.tagPattern = tagPattern;
			return this;
		}

		public Builder pattern(String pattern) {
			this.patterns.add(pattern);
			return this;
		}

		public Builder patterns(String... patterns) {
			this.patterns.addAll(Arrays.asList(patterns));
			return this;
		}

		public Builder consumption(int elementConsumption) {
			this.elementConsumption = elementConsumption;
			return this;
		}

		public Builder inputSize(int inputSize) {
			this.inputSize = inputSize;
			return this;
		}

		public Builder outputSize(int outputSize) {
			this.outputSize = outputSize;
			return this;
		}

		public Builder luckRatio(double luckRatio) {
			this.luckRatio = luckRatio;
			return this;
		}
	}
}
