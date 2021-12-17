package sirttas.elementalcraft.recipe.instrument.infusion;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.recipe.RecipeHelper;

import javax.annotation.Nonnull;

public class ToolInfusionRecipe implements IInfusionRecipe {

	public static final String NAME = "tool_" + IInfusionRecipe.NAME;
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final RecipeSerializer<ToolInfusionRecipe> SERIALIZER = null;
	
	private final Ingredient input;
	private final int elementAmount;
	private final IDataWrapper<ToolInfusion> toolInfusion;
	protected final ResourceLocation id;
	
	public ToolInfusionRecipe(ResourceLocation id, ResourceLocation toolInfusion, Ingredient input, int elementAmount) {
		this.id = id;
		this.toolInfusion = ElementalCraftApi.TOOL_INFUSION_MANAGER.getWrapper(toolInfusion);
		this.input = input;
		this.elementAmount = elementAmount;
	}
	
	@Override
	public boolean matches(IInfuser instrument) {
		return IInfusionRecipe.super.matches(instrument) && !getToolInfusion().equals(ToolInfusionHelper.getInfusion(instrument.getItem()));
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public Ingredient getInput() {
		return input;
	}
	
	@Override
	public ItemStack assemble(IInfuser instrument) {
		ItemStack stack = instrument.getItem();

		ToolInfusionHelper.setInfusion(stack, getToolInfusion());
		return stack;
	}

	@Nonnull
    @Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}
	
	@Nonnull
    @Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public ElementType getElementType() {
		return getToolInfusion().getElementType();
	}

	public ToolInfusion getToolInfusion() {
		return  toolInfusion.get();
	}
	
	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ToolInfusionRecipe> {

		@Nonnull
        @Override
		public ToolInfusionRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient input = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ResourceLocation toolInfusion = new ResourceLocation(GsonHelper.getAsString(json, ECNames.TOOL_INFUSION));

			return new ToolInfusionRecipe(recipeId, toolInfusion, input, elementAmount);
		}

		@Override
		public ToolInfusionRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int elementAmount = buffer.readInt();
			Ingredient input = Ingredient.fromNetwork(buffer);
			ResourceLocation toolInfusion = buffer.readResourceLocation();

			return new ToolInfusionRecipe(recipeId, toolInfusion, input, elementAmount);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ToolInfusionRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			recipe.getInput().toNetwork(buffer);
			buffer.writeResourceLocation(recipe.getToolInfusion().getId());
		}
	}
}
