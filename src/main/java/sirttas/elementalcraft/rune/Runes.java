package sirttas.elementalcraft.rune;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;

public class Runes {

	private Runes() {}
	
	@OnlyIn(Dist.CLIENT)
	public static void registerModels() {
		ElementalCraftApi.RUNE_MANAGER.getData().values().forEach(rune -> addModel(rune.getModelName()));
		Minecraft.getInstance().getResourceManager().listResources("models/item/" + Rune.FOLDER, fileName -> fileName.endsWith(".json")).forEach(Runes::addModel); // TODO change this
	}

	@OnlyIn(Dist.CLIENT)
	private static void addModel(ResourceLocation runeModel) {
		String path = StringUtils.removeStart(StringUtils.removeEnd(runeModel.getPath(), ".json"), "models/item/");

		ModelLoader.addSpecialModel(new ModelResourceLocation(new ResourceLocation(runeModel.getNamespace(), path), "inventory"));
	}

}
