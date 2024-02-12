package sirttas.elementalcraft.pureore.loader;

import com.mojang.datafixers.Products.P6;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.pureore.PureOre;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractPureOreLoader implements IPureOreLoader {
	final HolderSet<Item> source;
	final int elementConsumption;
	final int inputSize;
	final int outputSize;
	final double luckRatio;
	private final int order;

	protected AbstractPureOreLoader(HolderSet<Item> source, int elementConsumption, int inputSize, int outputSize, double luckRatio, int order) {
		this.source = source;
		this.elementConsumption = elementConsumption;
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		this.luckRatio = luckRatio;
		this.order = order;
	}

	protected static <U extends AbstractPureOreLoader> P6<Mu<U>, HolderSet<Item>, Integer, Integer, Integer, Double, Integer> codec(RecordCodecBuilder.Instance<U> builder) {
		return builder.group(
				RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("source").forGetter(l -> l.source),
				Codec.INT.optionalFieldOf(ECNames.ELEMENT_CONSUMPTION, 2500).forGetter(l -> l.elementConsumption),
				Codec.INT.optionalFieldOf("input_size", 1).forGetter(l -> l.inputSize),
				Codec.INT.optionalFieldOf("output_size", 2).forGetter(l -> l.outputSize),
				Codec.DOUBLE.optionalFieldOf("luck_ratio", 0D).forGetter(l -> l.luckRatio),
				Codec.INT.optionalFieldOf("order", Integer.MAX_VALUE).forGetter(AbstractPureOreLoader::getOrder)
		);
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public List<PureOre> generate(RegistryAccess registry) {
		return List.copyOf(this.generatePureOres(registry).values());
	}

	private Map<ResourceLocation, PureOre> generatePureOres(RegistryAccess registry) {
		var list = streamSource().toList();
		var id = this.getId();

		if (list.isEmpty()) {
			ElementalCraftApi.LOGGER.debug("No source items found for {}", id);
			return Collections.emptyMap();
		}

		Map<ResourceLocation, PureOre> pureOres = new HashMap<>();
		ElementalCraftApi.LOGGER.info("Loading pure ores: {}.\r\n\tSource ores: {}",
				() -> id,
				() -> list.stream()
						.mapMulti(ElementalCraftUtils.cast(Holder.Reference.class))
						.map(r -> r.key().location().toString())
						.collect(Collectors.joining(", ")));

		list.forEach(holder -> {
			var ore = holder.value();
			var entry = findOrCreateEntry(pureOres, ore);
		});
		return pureOres;
	}

	@Nonnull
	public ResourceLocation getId() {
		return ElementalCraft.PURE_ORE_LOADERS_MANAGER.getId(this);
	}

	@Nonnull
	public Stream<Holder<Item>> streamSource() {
		return this.source.stream();
	}

	private PureOre findOrCreateEntry(Map<ResourceLocation, PureOre> pureOres, Item ore) {
		for (PureOre pureOre : pureOres.values()) {
			if (pureOre.contains(ore)) {
				return pureOre;
			}
		}

		var generated = load(pureOres, ore);
		var entry = pureOres.computeIfAbsent(generated.id(), i -> new PureOre(i, elementConsumption, inputSize, outputSize, luckRatio));

		entry.getOres().add(ore);
		generated.tags().forEach(entry::addTag);
		return entry;
	}

	protected abstract GeneratedPureOre load(Map<ResourceLocation, PureOre> pureOres, Item ore);

	public record GeneratedPureOre(
			ResourceLocation id,
			List<TagKey<Item>> tags
	) {}
}
