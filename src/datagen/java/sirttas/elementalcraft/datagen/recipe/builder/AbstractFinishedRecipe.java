package sirttas.elementalcraft.datagen.recipe.builder;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractFinishedRecipe implements FinishedRecipe {
	
	private final ResourceLocation id;
	private final RecipeSerializer<?> serializer;

	protected AbstractFinishedRecipe(ResourceLocation id, RecipeSerializer<?> serializer) {
		this.id = id;
		this.serializer = serializer;
	}


	@Nonnull
    @Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Nonnull
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
