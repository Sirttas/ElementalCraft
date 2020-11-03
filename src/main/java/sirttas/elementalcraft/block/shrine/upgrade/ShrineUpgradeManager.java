package sirttas.elementalcraft.block.shrine.upgrade;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.block.Block;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;

public class ShrineUpgradeManager extends JsonReloadListener {

	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ShrineUpgrade.class, ShrineUpgrade.SEZRIALIZER).create();

	private Map<ResourceLocation, ShrineUpgrade> upgrades = ImmutableMap.of();

	public ShrineUpgradeManager() {
		super(GSON, "elementalcraft_shrine_upgrades");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objects, IResourceManager resourceManagerIn, IProfiler profilerIn) {
		ImmutableMap.Builder<ResourceLocation, ShrineUpgrade> builder = ImmutableMap.builder();

		objects.forEach((loc, jsonObject) -> builder.put(loc, GSON.fromJson(jsonObject, ShrineUpgrade.class)));
		upgrades = builder.build();
		refresh();
		ElementalCraft.LOGGER.info("Loaded {} ElementalCraft shrine upgrades", upgrades.size());
	}

	private void refresh() {
		upgrades.forEach((loc, upgrade) -> {
			upgrade.setId(loc);
			upgrade.refresh(this);
		});
		for (Block block : ForgeRegistries.BLOCKS) {
			if (block instanceof BlockShrineUpgrade) {
				((BlockShrineUpgrade) block).setUpgrade(getUpgrade(block.getRegistryName()));
			}
		}
	}

	public ShrineUpgrade getUpgrade(ResourceLocation loc) {
		return upgrades.get(loc);
	}

	public Map<ResourceLocation, ShrineUpgrade> getUpgrades() {
		return upgrades;
	}

	public void setUpgrades(Map<ResourceLocation, ShrineUpgrade> map) {
		upgrades = ImmutableMap.copyOf(map);
		refresh();
		ElementalCraft.LOGGER.info("Reloaded {} ElementalCraft shrine upgrades", upgrades.size());
	}

}
