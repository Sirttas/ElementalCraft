package sirttas.elementalcraft.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.BinderRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;

@JeiPlugin
public class ElementalCraftJEIPlugin implements IModPlugin {
	private static final ResourceLocation ID = new ResourceLocation(ElementalCraft.MODID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registry) {
		registry.useNbtForSubtypes(ECItems.receptacle);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new InfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new BindingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new PureInfusionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ECItems.fireFurnace), VanillaRecipeCategoryUid.FURNACE);
		registry.addRecipeCatalyst(new ItemStack(ECItems.infuser), InfusionRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.binder), BindingRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ECItems.pureInfuser), PureInfusionRecipeCategory.UID);
	}

	@SuppressWarnings("resource")
	@Override
	public void registerRecipes(@Nonnull IRecipeRegistration registry) {
		RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();

		registry.addRecipes(recipeManager.getRecipes(AbstractInfusionRecipe.TYPE).values(), InfusionRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(BinderRecipe.TYPE).values(), BindingRecipeCategory.UID);
		registry.addRecipes(recipeManager.getRecipes(PureInfusionRecipe.TYPE).values(), PureInfusionRecipeCategory.UID);
	}
}