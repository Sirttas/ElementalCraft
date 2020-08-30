package sirttas.elementalcraft.recipe.instrument;

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
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.ECNames;

public class BinderRecipe extends AbstractInstrumentRecipe<TileBinder> {

	public static final String NAME = "binding";
	public static final IRecipeType<BinderRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(NAME), new IRecipeType<BinderRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static IRecipeSerializer<BinderRecipe> SERIALIZER;

	private NonNullList<Ingredient> ingredients;
	private ItemStack output;
	private int elementPerTick;
	private int duration;

	public BinderRecipe(ResourceLocation id, ElementType type, int elementPerTick, int duration, ItemStack output, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.from(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.output = output;
		this.elementPerTick = elementPerTick;
		this.duration = duration;
	}

	@Override
	public int getElementPerTick() {
		return elementPerTick;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public boolean matches(TileBinder inv) {
		if (inv.getItemCount() != ingredients.size()) {
			return false;
		}
		return Boolean.TRUE.equals(ECConfig.CONFIG.binderRecipeMatchOrder.get()) ? matchesOrdered(inv) : matchesUnordered(inv);
	}

	private boolean matchesOrdered(TileBinder inv) {
		for (int i = 0; i < inv.getItemCount(); i++) {
			if (!ingredients.get(i).test(inv.getStackInSlot(i))) {
				return false;
			}
		}
		return true;
	}

	private boolean matchesUnordered(TileBinder inv) {
		return ingredients.stream().allMatch(ing -> {
			for (int i = 0; i < inv.getItemCount(); i++) {
				if (ing.test(inv.getStackInSlot(i))) {
					return true;
				}
			}
			return false;
		});
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return output;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public void process(TileBinder instrument) {
		instrument.clear();
		super.process(instrument);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BinderRecipe> {
		final IRecipeFactory factory;

		public Serializer(IRecipeFactory factory) {
			this.factory = factory;
		}

		@Override
		public BinderRecipe read(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getString(json, "element"));
			int elementPerTick = JSONUtils.getInt(json, "consumption", 25);
			int duration = JSONUtils.getInt(json, "duration", 100);
			NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
			ItemStack output = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, ECNames.OUTPUT))));

			return this.factory.create(recipeId, type, elementPerTick, duration, output, ingredients);
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
		public BinderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readString(32767));
			int elementPerTick = buffer.readInt();
			int duration = buffer.readInt();
			ItemStack output = buffer.readItemStack();
			int i = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.read(buffer));
			}

			return this.factory.create(recipeId, type, elementPerTick, duration, output, ingredients);
		}

		@Override
		public void write(PacketBuffer buffer, BinderRecipe recipe) {
			buffer.writeString(recipe.getElementType().getString());
			buffer.writeInt(recipe.getElementPerTick());
			buffer.writeInt(recipe.getDuration());
			buffer.writeItemStack(recipe.getRecipeOutput());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.write(buffer);
			}
		}

		public interface IRecipeFactory {
			BinderRecipe create(ResourceLocation id, ElementType type, int elementPerTick, int duration, ItemStack output, List<Ingredient> ingredients);
		}
	}
}
