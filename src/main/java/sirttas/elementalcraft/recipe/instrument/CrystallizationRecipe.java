package sirttas.elementalcraft.recipe.instrument;

import java.util.List;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.codec.CodecHelper;
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
	
	private final NonNullList<Ingredient> ingredients;
	private final List<ResultEntry> outputs;
	private final int elementAmount;

	public CrystallizationRecipe(ResourceLocation id, ElementType type, int elementAmount, List<ResultEntry> outputs, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.from(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
		this.outputs = ImmutableList.copyOf(outputs);
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

	public List<ResultEntry> getOutputs() {
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
	public ItemStack getCraftingResult(TileCrystallizer instrument, float luck) {
		ItemStack gem = instrument.getInventory().getStackInSlot(0);
		int index = IntStream.range(0, outputs.size()).filter(i -> ItemHandlerHelper.canItemStacksStack(outputs.get(i).result, gem)).findFirst().orElse(0);
		int weight = getTotalWeight(outputs.subList(index, outputs.size()), luck);
		int roll = Math.min(instrument.getWorld().rand.nextInt(weight), weight - 1);
		
		for (ResultEntry entry : outputs) {
			roll -= entry.getEffectiveWeight(luck) + luck;
			if (roll < 0) {
				return entry.getResult();
			}
		}
		return ItemStack.EMPTY;
	}

	public int getTotalWeight() {
		return getTotalWeight(outputs, 0);
	}
	
	public int getTotalWeight(List<ResultEntry> list, float luck) {
		return list.stream().mapToInt(result -> result.getEffectiveWeight(luck)).sum();
	}
	
	public float getWeight(ItemStack stack) {
		return outputs.stream().filter(r -> r.result.equals(stack)).findAny().map(r -> r.weight).orElse(0F);
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static ResultEntry createResult(ItemStack result, float weight) {
		return createResult(result, weight, 1);
	}
	
	public static ResultEntry createResult(ItemStack result, float weight, float quality) {
		return new ResultEntry(result, weight, quality);
	}
	
	public static class ResultEntry {
		public static final Codec<ResultEntry> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				ItemStack.CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result),
				Codec.FLOAT.fieldOf(ECNames.WEIGHT).forGetter(r -> r.weight),
				Codec.FLOAT.optionalFieldOf(ECNames.QUALITY, 1F).forGetter(r -> r.quality)
		).apply(builder, ResultEntry::new));
		public static final Codec<List<ResultEntry>> LIST_CODEC = CODEC.listOf();
		
		private final float weight;
		private final float quality;
		private final ItemStack result;
		
		private ResultEntry(ItemStack result, float weight, float quality) {
			this.result = result;
			this.weight = weight;
			this.quality = quality;
		}
		
		public ItemStack getResult() {
			return result.copy();
		}
		
		public int getEffectiveWeight(float luck) {
			return Math.max(MathHelper.floor(weight + quality * luck), 0);
		}
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrystallizationRecipe> {


		@Override
		public CrystallizationRecipe read(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonObject(json, ECNames.INGREDIENTS));
			List<ResultEntry> outputs = CodecHelper.decode(ResultEntry.LIST_CODEC, json.get(ECNames.OUTPUTS));
			
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
			List<ResultEntry> outputs = CodecHelper.decode(ResultEntry.LIST_CODEC, buffer);
			
			int i = buffer.readInt();
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
			CodecHelper.encode(ResultEntry.LIST_CODEC, recipe.outputs, buffer);
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.write(buffer));
		}
	}
}
