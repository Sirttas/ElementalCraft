package sirttas.elementalcraft.item.pureore;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class PureOreHandler {

	private static boolean tagsReceived = false;
	private static boolean recipesReceived = false;

	private static RecipeManager recipeManager = null;

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onRecipesUpdate(RecipesUpdatedEvent event) {
		recipeManager = event.getRecipeManager();
		if (tagsReceived) {
			process();
		} else {
			ElementalCraft.LOGGER.info("Pure ore generation: Recipes received, waiting for Tags...");
			recipesReceived = true;
		}
	}

	@SubscribeEvent
	public static void onTagsUpdate(TagsUpdatedEvent event) {
		if (recipesReceived) {
			process();
		} else {
			ElementalCraft.LOGGER.info("Pure ore generation: Tags received, waiting for Recipes...");
			tagsReceived = true;
		}
	}

	private static void process() {
		PureOreHelper.generatePureOres(recipeManager);
		if (recipesReceived) {
			ElementalCraft.LOGGER.info("JEI loaded before pure ore generation, atempting to reload JEI");
			IResourceManager manager = Minecraft.getInstance().getResourceManager();

			Optional.ofNullable(manager).filter(SimpleReloadableResourceManager.class::isInstance).map(SimpleReloadableResourceManager.class::cast).map(r -> r.reloadListeners.stream())
					.orElse(Stream.empty()).filter(r -> "JeiReloadListener".equals(r.getClass().getSimpleName())).map(ISelectiveResourceReloadListener.class::cast) // NOSONAR
					.forEach(r -> r.onResourceManagerReload(manager, t -> true));
		}
		recipeManager = null;
		recipesReceived = false;
		tagsReceived = false;
	}
}
