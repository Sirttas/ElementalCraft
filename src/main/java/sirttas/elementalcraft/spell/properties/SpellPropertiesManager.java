package sirttas.elementalcraft.spell.properties;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.spell.Spell;

public class SpellPropertiesManager extends JsonReloadListener {

	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(SpellProperties.class, SpellProperties.SEZRIALIZER).create();

	private Map<ResourceLocation, SpellProperties> properties = ImmutableMap.of();

	public SpellPropertiesManager() {
		super(GSON, "elementalcraft_spell_properties");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objects, IResourceManager resourceManagerIn, IProfiler profilerIn) {
		ImmutableMap.Builder<ResourceLocation, SpellProperties> builder = ImmutableMap.builder();

		objects.forEach((loc, jsonObject) -> builder.put(loc, GSON.fromJson(jsonObject, SpellProperties.class)));
		properties = builder.build();
		refresh();
		ElementalCraft.LOGGER.info("Loaded {} ElementalCraft spell properites", properties.size());
	}

	private void refresh() {
		for (Spell spell : Spell.REGISTRY.getValues()) {
			SpellProperties prop = properties.get(spell.getRegistryName());

			if (prop != null) {
				spell.setProperties(prop);
			} else {
				spell.setProperties(SpellProperties.NONE);
			}
		}
	}

	public Map<ResourceLocation, SpellProperties> getProperties() {
		return properties;
	}

	public void setProperties(Map<ResourceLocation, SpellProperties> map) {
		properties = ImmutableMap.copyOf(map);
		refresh();
		ElementalCraft.LOGGER.info("Reloaded {} ElementalCraft spell properites", properties.size());
	}

}
