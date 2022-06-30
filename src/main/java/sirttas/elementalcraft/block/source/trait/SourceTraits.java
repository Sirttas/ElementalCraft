package sirttas.elementalcraft.block.source.trait;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.event.DataManagerReloadEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class SourceTraits {

	private static final Map<ResourceKey<SourceTrait>, Integer> SOURCE_TRAITS_ORDERS = new HashMap<>();

	private SourceTraits() {}
	
	public static final ResourceKey<SourceTrait> ELEMENT_CAPACITY = key(ECNames.ELEMENT_CAPACITY);
	public static final ResourceKey<SourceTrait> RECOVER_RATE = key(ECNames.RECOVER_RATE);
	public static final ResourceKey<SourceTrait> DIURNAL_NOCTURNAL = key("diurnal_nocturnal");
	public static final ResourceKey<SourceTrait> GENEROSITY = key("generosity");
	public static final ResourceKey<SourceTrait> THRIFTINESS = key("thriftiness");

	public static final ResourceKey<SourceTrait> FLEETING = key("fleeting");


	public static int getOrder(ResourceKey<SourceTrait> trait) {
		return SOURCE_TRAITS_ORDERS.getOrDefault(trait, Integer.MAX_VALUE);
	}

	public static ResourceKey<SourceTrait> key(String name) {
		return key(ElementalCraft.createRL(name));
	}

	public static ResourceKey<SourceTrait> key(ResourceLocation name) {
		return IDataManager.createKey(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, name);
	}

	@SubscribeEvent
	public static void onSourceTraitReloaded(DataManagerReloadEvent<SourceTrait> event) {
		SOURCE_TRAITS_ORDERS.clear();
		event.getDataManager().getData().forEach((key, trait) -> SOURCE_TRAITS_ORDERS.put(IDataManager.createKey(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, key), trait.getOrder()));
	}
}
