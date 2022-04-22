package sirttas.elementalcraft.rune;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;

import java.util.function.Consumer;

public class Runes {

	private Runes() {}
	
	@OnlyIn(Dist.CLIENT)
	public static void registerModels(Consumer<ResourceLocation> addModel) {
		ElementalCraftApi.RUNE_MANAGER.getData().values().forEach(rune -> addModel.accept(rune.getModelName()));
		Minecraft.getInstance().getResourceManager().listResources("models/item/" + Rune.FOLDER, fileName -> fileName.endsWith(".json")).forEach(addModel);
	}

}
