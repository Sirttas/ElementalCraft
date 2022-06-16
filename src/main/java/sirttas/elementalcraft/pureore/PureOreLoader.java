package sirttas.elementalcraft.pureore;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.util.Lazy;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PureOreLoader {

	private static final Pattern DEEPSLATE_PATTERN = Pattern.compile("^deepslate_");

	private final TagKey<Item> sourceTag;
	private final Lazy<HolderSet.Named<Item>> source;

	private Pattern pattern;
	private String tagFolder;
	private int inputSize;
	private int outputSize;
	private double luckRatio;

	private PureOreLoader(TagKey<Item> sourceTag) {
		this.sourceTag = sourceTag;
		this.source = Lazy.of(() -> ECTags.Items.getTag(sourceTag));
		this.inputSize = 1;
		this.outputSize = 2;
		this.luckRatio = 0;
	}

	public static PureOreLoader create(TagKey<Item> sourceTag) {
		return new PureOreLoader(sourceTag);
	}

	public PureOreLoader pattern(String pattern) {
		this.pattern = StringUtils.isNotBlank(pattern) ? Pattern.compile(pattern) : null;
		return this;
	}

	public PureOreLoader tagFolder(String tagFolder) {
		this.tagFolder = tagFolder;
		return this;
	}

	public PureOreLoader inputSize(int inputSize) {
		this.inputSize = inputSize;
		return this;
	}

	public PureOreLoader outputSize(int outputSize) {
		this.outputSize = outputSize;
		return this;
	}

	public PureOreLoader luckRatio(double luckRatio) {
		this.luckRatio = luckRatio;
		return this;
	}


	public List<PureOre> generate(Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> injectors) {
		return List.copyOf(this.generatePureOres(injectors).values());
	}

	private Map<ResourceLocation, PureOre> generatePureOres(Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> injectors) {
		Map<ResourceLocation, PureOre> pureOres = new HashMap<>();

		ElementalCraftApi.LOGGER.info("Loading pure ores.\r\n\tSource ores: {}",
				() -> streamSourceTag()
						.mapMulti(ElementalCraftUtils.cast(Holder.Reference.class))
						.map(r -> r.key().location().toString())
						.collect(Collectors.joining(", ")));

		streamSourceTag().forEach(holder -> {
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
	private Stream<Holder<Item>> streamSourceTag() {
		var value = this.source.get();

		if (value == null) {
			ElementalCraftApi.LOGGER.error("Source tag: {} is null!", sourceTag::location);
			return Stream.empty();
		}

		return  value.stream();
	}

	private PureOre findOrCreateEntry(Map<ResourceLocation, PureOre> pureOres, Item ore) {
		for (PureOre pureOre : pureOres.values()) {
			if (pureOre.contains(ore)) {
				return pureOre;
			}
		}

		var id = ore.getRegistryName();
		var path = DEEPSLATE_PATTERN.matcher(id.getPath()).replaceAll("");

		Matcher matcher = pattern != null ? pattern.matcher(path) : null;
		HolderSet.Named<Item> tag = null;

		if (tagFolder != null && matcher != null && matcher.find()) {
			path = matcher.replaceAll("");

			var namespace = id.getNamespace();
			var folderPath = tagFolder + "/"; // NOSONAR

			try {
				tag = ECTags.Items.getTag(k -> k.location().getNamespace().equals("forge") && StringUtils.startsWith(k.location().getPath(), folderPath) && ore.builtInRegistryHolder().is(k));

				if (tag != null) {
					namespace = "forge";
					path = StringUtils.removeStart(tag.key().location().getPath(), folderPath);
				}
			} catch (Exception e) {
				ElementalCraftApi.LOGGER.trace("Tag Not found with {}: {}", ore::getRegistryName, e::getMessage);
			}
			id = new ResourceLocation(namespace, path);
		}
		
		var entry = pureOres.computeIfAbsent(id, i -> new PureOre(i, inputSize, outputSize, luckRatio));

		entry.getOres().add(ore);
		if (tag != null) {
			entry.addTag(tag);
		}
		return entry;
	}
}
