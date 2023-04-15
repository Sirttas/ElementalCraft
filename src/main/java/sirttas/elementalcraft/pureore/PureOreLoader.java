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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record PureOreLoader(
		HolderSet<Item> source,
		Optional<ResourceLocation> fixedName,
		Optional<Pattern> tagPattern,
		List<Pattern> patterns,
		String namespace,
		Optional<Pattern> namespacePattern,
		int elementConsumption,
		int inputSize,
		int outputSize,
		double luckRatio,
		int order
) {
	public static final String NAME = "pure_ore_loaders";
	public static final String FOLDER = ElementalCraftApi.MODID + '/' + NAME;

	public static Codec<PureOreLoader> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			RegistryCodecs.homogeneousList(Registry.ITEM_REGISTRY).fieldOf("source").forGetter(PureOreLoader::source),
			ResourceLocation.CODEC.optionalFieldOf("fixed_name").forGetter(PureOreLoader::fixedName),
			Codecs.PATTERN.optionalFieldOf("tag_pattern").forGetter(PureOreLoader::tagPattern),
			Codecs.PATTERN.listOf().optionalFieldOf("patterns", Collections.emptyList()).forGetter(PureOreLoader::patterns),
			Codec.STRING.optionalFieldOf("namespace", ECNames.FORGE).forGetter(PureOreLoader::namespace),
			Codecs.PATTERN.optionalFieldOf("namespace_pattern").forGetter(PureOreLoader::namespacePattern),
			Codec.INT.optionalFieldOf(ECNames.ELEMENT_CONSUMPTION, 2500).forGetter(PureOreLoader::elementConsumption),
			Codec.INT.optionalFieldOf("input_size", 1).forGetter(PureOreLoader::inputSize),
			Codec.INT.optionalFieldOf("output_size", 2).forGetter(PureOreLoader::outputSize),
			Codec.DOUBLE.optionalFieldOf("luck_ratio", 0D).forGetter(PureOreLoader::luckRatio),
			Codec.INT.optionalFieldOf("order", 1000).forGetter(PureOreLoader::order)
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
		} else if (tagPattern.isPresent()) {
			var tp = tagPattern.get();
			var np = namespacePattern.orElseGet(() -> Pattern.compile("^" + namespace + "$"));

			id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ore));
			tags = ore.builtInRegistryHolder().tags()
					.filter(t -> {
						var location = t.location();

						return np.matcher(location.getNamespace()).find() && tp.matcher(location.getPath()).find();
					}).toList();

			if (!tags.isEmpty()) {
				if (tags.size() > 1) {
					ElementalCraftApi.LOGGER.warn("Item {} has multiple tags matching \"{}:{}\":\r\n\t{}",
							id::toString,
							np::pattern,
							tp::pattern,
							() -> tags.stream().map(t -> t.location().toString()).collect(Collectors.joining(", ")));
				}
				id = new ResourceLocation(namespace, cleanPath(tp.matcher(tags.get(0).location().getPath()).replaceAll("")));
			} else {
				id = new ResourceLocation(id.getNamespace(), cleanPath(id.getPath()));
			}
		} else {
			throw new IllegalStateException("Either fixed_name or tag_folder must be set");
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

		private static final AtomicInteger ORDER_INCREMENT = new AtomicInteger(0);

		public static final Encoder<Builder> ENCODER = PureOreLoader.CODEC.comap(builder -> new PureOreLoader(
				builder.source,
				Optional.ofNullable(builder.fixedName),
				StringUtils.isNotBlank(builder.tagPattern) ? Optional.of(Pattern.compile(builder.tagPattern)) : Optional.empty(),
				builder.patterns.stream()
						.map(Pattern::compile)
						.toList(),
				builder.namespace,
				StringUtils.isNotBlank(builder.namespacePattern) ? Optional.of(Pattern.compile(builder.namespacePattern)) : Optional.empty(),
				builder.elementConsumption,
				builder.inputSize,
				builder.outputSize,
				builder.luckRatio,
				builder.order)
		);

		private final HolderSet<Item> source;
		private ResourceLocation fixedName;
		private String tagPattern;
		private final List<String> patterns;
		private String namespace;
		private String namespacePattern;
		private int elementConsumption;
		private int inputSize;
		private int outputSize;
		private double luckRatio;
		private int order;

		private Builder(HolderSet<Item> source) {
			this.source = source;
			this.elementConsumption = 2500;
			this.inputSize = 1;
			this.outputSize = 2;
			this.luckRatio = 0;
			this.patterns = new ArrayList<>();
			this.namespace = ECNames.FORGE;
			this.namespacePattern = "";
			this.tagPattern = "";
			this.order = ORDER_INCREMENT.getAndIncrement();
		}

		public Builder fixedName(ResourceLocation fixedName) {
			this.tagPattern = "";
			this.namespace = ECNames.FORGE;
			this.fixedName = fixedName;
			return this;
		}

		public Builder tagPattern(String tagPattern) {
			this.fixedName = null;
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

		public Builder namespace(String namespace) {
			this.namespace = namespace;
			return this;
		}

		public Builder namespacePattern(String namespacePattern) {
			this.namespacePattern = namespacePattern;
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

		public Builder order(int order) {
			this.order = order;
			if (order >= ORDER_INCREMENT.get()) {
				ORDER_INCREMENT.set(order + 1);
			}
			return this;
		}
	}
}
