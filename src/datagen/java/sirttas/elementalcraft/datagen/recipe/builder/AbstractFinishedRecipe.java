package sirttas.elementalcraft.datagen.recipe.builder;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractFinishedRecipe implements FinishedRecipe {
	
	private final ResourceLocation id;
	private final RecipeSerializer<?> serializer;

	protected AbstractFinishedRecipe(ResourceLocation idIn, RecipeSerializer<?> serializerIn) {
		this.id = idIn;
		this.serializer = serializerIn;
	}


	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getType() {
		return this.serializer;
	}

	@Override
	@Nullable
	public JsonObject serializeAdvancement() {
		return null;
	}

	@Override
	@Nullable
	public ResourceLocation getAdvancementId() {
		return null;
	}
}