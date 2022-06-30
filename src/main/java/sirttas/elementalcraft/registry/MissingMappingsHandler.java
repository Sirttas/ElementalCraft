package sirttas.elementalcraft.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;
import sirttas.elementalcraft.item.elemental.LensItem;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class MissingMappingsHandler {

	private static final Map<String, String> NAME_REMAP = Map.of(
			"tank", ElementContainerBlock.NAME,
			"tank_small", SmallElementContainerBlock.NAME,
			"tank_creative", CreativeElementContainerBlock.NAME,
			"inertcrystal", "inert_crystal",
			"crystalore", "inert_crystal_ore",
			"containedcrystal", "contained_crystal",
			"fire_lense", LensItem.NAME_FIRE,
			"water_lense", LensItem.NAME_WATER,
			"earth_lense", LensItem.NAME_EARTH,
			"air_lense", LensItem.NAME_AIR);

	private MissingMappingsHandler() {}
	@SubscribeEvent
	public static void remapMissingMappings(MissingMappingsEvent event) {
		remapMissingMappings(event, event.getRegistry());
	}
	
	private static <T> void remapMissingMappings(MissingMappingsEvent event, IForgeRegistry<T> registry) {
		var key = registry.getRegistryKey();

		if (!key.equals(event.getKey())) {
			return;
		}

		var defaultValue = registry.getDefaultKey() != null ? registry.getValue(registry.getDefaultKey()) : null;

		for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(key, ElementalCraftApi.MODID)) {
			ResourceLocation oldId = mapping.getKey();
			var oldName = oldId.getPath();
			String newName = NAME_REMAP.get(oldName);

			if (newName != null) {
				T entity = registry.getValue(ElementalCraft.createRL(newName));

				if (entity != null && entity != defaultValue) {
					mapping.remap(entity);
					ElementalCraftApi.LOGGER.info(ForgeRegistry.REGISTRIES, "Re-mapped {} \"{}\" to \"{}\"", registry.getRegistryName(), oldName, newName);
				}
			}
		}
	}
	
}
