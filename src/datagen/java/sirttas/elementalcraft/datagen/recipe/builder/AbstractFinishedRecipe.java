package sirttas.elementalcraft.datagen.recipe.builder;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractFinishedRecipe implements IFinishedRecipe {
	
	private final ResourceLocation id;
	private final IRecipeSerializer<?> serializer;

	protected AbstractFinishedRecipe(ResourceLocation idIn, IRecipeSerializer<?> serializerIn) {
		this.id = idIn;
		this.serializer = serializerIn;
	}


	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getType() {
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