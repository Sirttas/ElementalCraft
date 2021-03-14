package sirttas.elementalcraft.recipe.instrument;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.evaporator.BlockEvaporator;
import sirttas.elementalcraft.block.instrument.crystallizer.TileCrystallizer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.elemental.ItemShard;
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.rune.Rune.BonusType;

public class CrystallizationRecipe extends AbstractInstrumentRecipe<TileCrystallizer> {

	public static final String NAME = "crystallization";
	public static final IRecipeType<CrystallizationRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<CrystallizationRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final IRecipeSerializer<CrystallizationRecipe> SERIALIZER = null;

	private NonNullList<Ingredient> ingredients;
	private final ImmutableMap<ItemStack, Integer> outputs;
	private final int totalWeight;
	private int elementAmount;

	public CrystallizationRecipe(ResourceLocation id, ElementType type, int elementAmount, Map<ItemStack, Integer> outputs, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.from(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.outputs = ImmutableMap.<ItemStack, Integer>builder().putAll(outputs).orderEntriesByValue(Comparator.comparingInt(Integer::intValue).reversed()).build();
		this.totalWeight = outputs.values().stream().mapToInt(Integer::intValue).sum();
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(TileCrystallizer inv) {
		if (inv.getTankElementType() == getElementType() && inv.getItemCount() >= 2) {
			for (int i = 0; i < 2; i++) {
				if (!ingredients.get(i).test(inv.getInventory().getStackInSlot(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	public Map<ItemStack, Integer> getOutputs() {
		return outputs;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	@Override
	public void process(TileCrystallizer instrument) {
		int luck = (int) Math.round(instrument.getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.crystallizerLuckRatio.get());
		IInventory inv = instrument.getInventory();
		
		for (int i = 2; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			
			if (BlockEvaporator.getShardElementType(stack) == this.elementType) {
				luck += ((ItemShard) stack.getItem()).getElementAmount();
			}
		}
		instrument.clear();
		instrument.getInventory().setInventorySlotContents(0, this.getCraftingResult(instrument, luck));
	}

	@Override
	public ItemStack getCraftingResult(TileCrystallizer instrument) {
		return getCraftingResult(instrument, 0);
	}

	@SuppressWarnings("resource")
	public ItemStack getCraftingResult(TileCrystallizer instrument, int luck) {
		int weight = this.totalWeight + (luck * outputs.size());
		int roll = Math.min(instrument.getWorld().rand.nextInt(weight), weight - 1);
		
		for (Entry<ItemStack, Integer> entry : outputs.entrySet()) {
			roll -= entry.getValue() + luck;
			if (roll < 0) {
				return entry.getKey().copy();
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrystallizationRecipe> {


		@Override
		public CrystallizationRecipe read(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonObject(json, ECNames.INGREDIENTS));
			Map<ItemStack, Integer> outputs = StreamSupport.stream(JSONUtils.getJsonArray(json, ECNames.OUTPUTS).spliterator(), false).filter(JsonObject.class::isInstance).map(JsonObject.class::cast)
					.collect(Collectors.toMap(obj -> RecipeHelper.readRecipeOutput(obj, ECNames.ITEM), obj -> JSONUtils.getInt(obj, ECNames.WEIGHT)));
			
			return new CrystallizationRecipe(recipeId, type, elementAmount, outputs, ingredients);
		}

		public static NonNullList<Ingredient> readIngredients(JsonObject json) {
			NonNullList<Ingredient> list = NonNullList.create();

			list.add(RecipeHelper.deserializeIngredient(json, "gem"));
			list.add(RecipeHelper.deserializeIngredient(json, "crystal"));
			list.add(RecipeHelper.deserializeIngredient(json, "shard"));
			return list;
		}

		@Override
		public CrystallizationRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ElementType type = ElementType.byName(buffer.readString(32767));
			int elementAmount = buffer.readInt();
			int i = buffer.readInt();
			Map<ItemStack, Integer> outputs = Maps.newHashMap();

			for (int j = 0; j < i; ++j) {
				outputs.put(buffer.readItemStack(), buffer.readInt());
			}
			i = buffer.readInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < i; ++j) {
				ingredients.set(j, Ingredient.read(buffer));
			}

			return new CrystallizationRecipe(recipeId, type, elementAmount, outputs, ingredients);
		}

		@Override
		public void write(PacketBuffer buffer, CrystallizationRecipe recipe) {
			buffer.writeString(recipe.getElementType().getString());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeInt(recipe.getOutputs().size());
			recipe.getOutputs().forEach((stack, weight) -> {
				buffer.writeItemStack(stack);
				buffer.writeInt(weight);
			});
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.write(buffer));
		}
	}
}
