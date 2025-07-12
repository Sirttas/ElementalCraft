package sirttas.elementalcraft.world.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

public class ECFeatures {

	private static final DeferredRegister<Feature<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.FEATURE, ElementalCraftApi.MODID);

	public static final DeferredHolder<Feature<?>, Feature<IElementTypeFeatureConfig>> SOURCE = DEFERRED_REGISTER.register(SourceFeature.NAME, SourceFeature::new);

	private ECFeatures() {}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}

}
