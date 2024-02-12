package sirttas.elementalcraft.rune;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.function.Consumer;

public class Runes {

	private Runes() {}

	@OnlyIn(Dist.CLIENT)
	public static void registerModels(Consumer<ResourceLocation> addModel) {
		ElementalCraftApi.RUNE_MANAGER.getData().values().forEach(rune -> addModel.accept(rune.getModelName()));
		Minecraft.getInstance().getResourceManager().listResources("models/" + ElementalCraftApi.RUNE_MANAGER.getFolder(), fileName -> fileName.getPath().endsWith(".json")).keySet().forEach(addModel);
	}

}
