package sirttas.elementalcraft.registry;

import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class MissingMappingsHandler {

	private static final Map<String, String> NAME_REMAP = Map.of(
			"tank", ElementContainerBlock.NAME,
			"tank_small", SmallElementContainerBlock.NAME,
			"tank_creative", CreativeElementContainerBlock.NAME);
	
	@SubscribeEvent
	public static void remapMissingBlockMappings(MissingMappings<Block> event) {
		remapMissingMappings(event, ForgeRegistries.BLOCKS);
	}
	
	@SubscribeEvent
	public static void remapMissingBlockEntityTypeMappings(MissingMappings<BlockEntityType<?>> event) {
		remapMissingMappings(event, ForgeRegistries.BLOCK_ENTITIES);
	}
	
	@SubscribeEvent
	public static void remapMissingItemMappings(MissingMappings<Item> event) {
		remapMissingMappings(event, ForgeRegistries.ITEMS);
	}
	
	private static <T extends IForgeRegistryEntry<T>> void remapMissingMappings(MissingMappings<T> event, IForgeRegistry<T> registry) {
		var defaultValue = registry.getDefaultKey() != null ? registry.getValue(registry.getDefaultKey()) : null;

		for (Mapping<T> mapping : event.getMappings(ElementalCraftApi.MODID)) {
			ResourceLocation oldId = mapping.key;
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
