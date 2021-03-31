package sirttas.elementalcraft.recipe.instrument.infusion;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class ToolInfusionRecipe implements IInfusionRecipe {

	public static final String NAME = "tool_" + IInfusionRecipe.NAME;
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final IRecipeSerializer<ToolInfusionRecipe> SERIALIZER = null;
	
	private final Ingredient input;
	private final int elementAmount;
	private final Lazy<ToolInfusion> toolInfusion;
	protected final ResourceLocation id;
	
	public ToolInfusionRecipe(ResourceLocation id, ResourceLocation toolInfusion, Ingredient input, int elementAmount) {
		this.id = id;
		this.toolInfusion = ElementalCraft.TOOL_INFUSION_MANAGER.getLazy(toolInfusion);
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
	public ItemStack getCraftingResult(IInfuser instrument) {
		ItemStack stack = instrument.getItem();

		ToolInfusionHelper.setInfusion(stack, getToolInfusion());
		return stack;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

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
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ToolInfusionRecipe> {

		@Override
		public ToolInfusionRecipe read(ResourceLocation recipeId, JsonObject json) {
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient input = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ResourceLocation toolInfusion = new ResourceLocation(JSONUtils.getString(json, ECNames.TOOL_INFUSION));

			return new ToolInfusionRecipe(recipeId, toolInfusion, input, elementAmount);
		}

		@Override
		public ToolInfusionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int elementAmount = buffer.readInt();
			Ingredient input = Ingredient.read(buffer);
			ResourceLocation toolInfusion = buffer.readResourceLocation();

			return new ToolInfusionRecipe(recipeId, toolInfusion, input, elementAmount);
		}

		@Override
		public void write(PacketBuffer buffer, ToolInfusionRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			recipe.getInput().write(buffer);
			buffer.writeResourceLocation(recipe.getToolInfusion().getId());
		}
	}
}
