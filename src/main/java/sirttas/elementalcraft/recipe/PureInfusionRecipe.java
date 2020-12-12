package sirttas.elementalcraft.recipe;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;
import sirttas.elementalcraft.nbt.ECNames;

public class PureInfusionRecipe implements IInventoryTileRecipe<TilePureInfuser> {

	public static final String NAME = "pureinfusion";
	public static final IRecipeType<PureInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<PureInfusionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static IRecipeSerializer<PureInfusionRecipe> SERIALIZER;

	private NonNullList<Ingredient> ingredients;
	private ItemStack output;
	private int elementPerTick;
	private int duration;
	private ResourceLocation id;

	public PureInfusionRecipe(ResourceLocation id, int elementPerTick, int duration, ItemStack output, List<Ingredient> ingredients) {
		this.id = id;
		this.ingredients = NonNullList.from(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.output = output;
		this.elementPerTick = elementPerTick;
		this.duration = duration;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public boolean matches(TilePureInfuser inv) {
		return ingredients.get(0).test(inv
				.getItem()) 
				&& ingredients.get(1).test(inv.getStackInPedestal(ElementType.WATER)) 
				&& ingredients.get(2).test(inv.getStackInPedestal(ElementType.FIRE))
				&& ingredients.get(3).test(inv.getStackInPedestal(ElementType.EARTH)) 
				&& ingredients.get(4).test(inv.getStackInPedestal(ElementType.AIR));
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public ItemStack getCraftingResult(TilePureInfuser inv) {
		return this.getRecipeOutput().copy();
	}

	@Override
	public void process(TilePureInfuser instrument) {
		instrument.getInventory().setInventorySlotContents(0, this.getCraftingResult(instrument));
		instrument.emptyPedestals();
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public int getElementPerTick() {
		return elementPerTick;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PureInfusionRecipe> {
		final IRecipeFactory factory;

		public Serializer(IRecipeFactory factory) {
			this.factory = factory;
		}

		@Override
		public PureInfusionRecipe read(ResourceLocation recipeId, JsonObject json) {
			int elementPerTick = JSONUtils.getInt(json, "consumption", 100);
			int duration = JSONUtils.getInt(json, "duration", 600);
			NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
			ItemStack output = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, ECNames.OUTPUT))));

			return this.factory.create(recipeId, elementPerTick, duration, output, ingredients);
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray json) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for (int i = 0; i < json.size(); ++i) {
				Ingredient ingredient = Ingredient.deserialize(json.get(i));

				if (!ingredient.hasNoMatchingItems()) {
					nonnulllist.add(ingredient);
				}
			}

			return nonnulllist;
		}

		@Override
		public PureInfusionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int elementPerTick = buffer.readInt();
			int duration = buffer.readInt();
			ItemStack output = buffer.readItemStack();
			int i = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.read(buffer));
			}

			return this.factory.create(recipeId, elementPerTick, duration, output, ingredients);
		}

		@Override
		public void write(PacketBuffer buffer, PureInfusionRecipe recipe) {
			buffer.writeInt(recipe.getElementPerTick());
			buffer.writeInt(recipe.getDuration());
			buffer.writeItemStack(recipe.getRecipeOutput());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.write(buffer);
			}
		}

		public interface IRecipeFactory {
			PureInfusionRecipe create(ResourceLocation id, int elementPerTick, int duration, ItemStack output, List<Ingredient> ingredients);
		}
	}
}
