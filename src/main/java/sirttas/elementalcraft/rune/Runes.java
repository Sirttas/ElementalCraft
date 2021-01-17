package sirttas.elementalcraft.rune;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class Runes {


	public static final String NAME = "runes";
	public static final String FOLDER = ElementalCraft.MODID + '_' + NAME;

	@OnlyIn(Dist.CLIENT)
	public static void registerModels(ModelRegistryEvent event) {
		ElementalCraft.RUNE_MANAGER.getData().values().forEach(rune -> addModel(rune.getModelName()));
		Minecraft.getInstance().getResourceManager().getAllResourceLocations("models/item/" + FOLDER, fileName -> fileName.endsWith(".json")).forEach(Runes::addModel); // TODO change this
	}

	@OnlyIn(Dist.CLIENT)
	private static void addModel(ResourceLocation runeModel) {
		String path = StringUtils.removeStart(StringUtils.removeEnd(runeModel.getPath(), ".json"), "models/item/");

		ModelLoader.addSpecialModel(new ModelResourceLocation(new ResourceLocation(runeModel.getNamespace(), path), "inventory"));
	}

}
