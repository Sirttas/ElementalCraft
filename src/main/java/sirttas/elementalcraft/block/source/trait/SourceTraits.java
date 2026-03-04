package sirttas.elementalcraft.block.source.trait;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class SourceTraits {

	private SourceTraits() {}
	
	public static final ResourceKey<SourceTrait> ELEMENT_CAPACITY_KEY = key(ECNames.ELEMENT_CAPACITY);
	public static final Holder<SourceTrait> ELEMENT_CAPACITY = holder(ELEMENT_CAPACITY_KEY);
	public static final ResourceKey<SourceTrait> DIURNAL_NOCTURNAL_KEY = key("diurnal_nocturnal");
	public static final Holder<SourceTrait> DIURNAL_NOCTURNAL = holder(DIURNAL_NOCTURNAL_KEY);
	public static final ResourceKey<SourceTrait> GENEROSITY_KEY = key("generosity");
	public static final Holder<SourceTrait> GENEROSITY = holder(GENEROSITY_KEY);
	public static final ResourceKey<SourceTrait> THRIFTINESS_KEY = key("thriftiness");
	public static final Holder<SourceTrait> THRIFTINESS = holder(THRIFTINESS_KEY);
	public static final ResourceKey<SourceTrait> FERTILITY_KEY = key("fertility");
	public static final Holder<SourceTrait> FERTILITY = holder(FERTILITY_KEY);

	@Nonnull
	public static SortedMap<Holder<SourceTrait>, ISourceTraitValue> createTraitMap() {
		return new TreeMap<>(Comparator.comparingInt(SourceTraits::getOrder));
	}

	public static int getOrder(Holder<SourceTrait> trait) {
		return trait.isBound() ? trait.value().getOrder() : Integer.MAX_VALUE;
	}

	private static Holder<SourceTrait> holder(ResourceKey<SourceTrait> key) {
		return ElementalCraftApi.SOURCE_TRAIT_MANAGER.getOrCreateHolder(key);
	}

	private static ResourceKey<SourceTrait> key(String name) {
		return key(ElementalCraftApi.createRL(name));
	}

	private static ResourceKey<SourceTrait> key(Identifier name) {
		return IDataManager.createKey(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, name);
	}
}
