package sirttas.elementalcraft.recipe.pure.infusion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlock;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

import javax.annotation.Nonnull;
import java.util.Map;

public class PureInfusionRecipe implements Recipe<PureInfusionRecipeInput> {

	public static final String NAME = "pureinfusion";

	private static final Codec<Map<ElementType, Ingredient>> INGREDIENTS_CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Ingredient.CODEC.fieldOf(PureInfuserBlock.NAME).forGetter(i -> i.get(ElementType.NONE)),
			Ingredient.CODEC.fieldOf(ElementType.FIRE.getSerializedName()).forGetter(i -> i.get(ElementType.FIRE)),
			Ingredient.CODEC.fieldOf(ElementType.WATER.getSerializedName()).forGetter(i -> i.get(ElementType.WATER)),
			Ingredient.CODEC.fieldOf(ElementType.EARTH.getSerializedName()).forGetter(i -> i.get(ElementType.EARTH)),
			Ingredient.CODEC.fieldOf(ElementType.AIR.getSerializedName()).forGetter(i -> i.get(ElementType.AIR))
	).apply(builder, (i, f, w, e, a) -> Map.of(ElementType.NONE, i, ElementType.FIRE, f, ElementType.WATER, w, ElementType.EARTH, e, ElementType.AIR, a)));

	public static final MapCodec<PureInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(PureInfusionRecipe::getElementAmount),
			INGREDIENTS_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(r -> r.ingredients),
			ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
	).apply(builder, PureInfusionRecipe::new));

	private final Map<ElementType, Ingredient> ingredients;
	private final ItemStack output;
	private final int elementAmount;

	public PureInfusionRecipe(int elementAmount, Map<ElementType, Ingredient> ingredients, ItemStack output) {
		this.ingredients = ingredients;
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return output;
	}

	@Override
	public boolean matches(@Nonnull PureInfusionRecipeInput input, @Nonnull Level level) {
		return ingredients.get(ElementType.NONE).test(input.pureInfuserInput())
				&& ingredients.get(ElementType.FIRE).test(input.getStackInPedestal(ElementType.FIRE))
				&& ingredients.get(ElementType.WATER).test(input.getStackInPedestal(ElementType.WATER))
				&& ingredients.get(ElementType.EARTH).test(input.getStackInPedestal(ElementType.EARTH))
				&& ingredients.get(ElementType.AIR).test(input.getStackInPedestal(ElementType.AIR));
	}

	@Nonnull
	public Map<ElementType, Ingredient> getIngredientsMap() {
		return ingredients;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.copyOf(ingredients.values());
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.PURE_INFUSION.get();
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.PURE_INFUSION.get();
	}

	@Override
	public @NotNull ItemStack assemble(@Nonnull PureInfusionRecipeInput input, @Nonnull HolderLookup.Provider provider) {
		var result = this.getResultItem(provider).copy();
		var target = result.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM);

		if (target == null) {
			return result;
		}

		for (var stack : input.getStacksInPedestals()) {
			var storage = stack.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM);

			if (storage != null) {
				storage.transferAll(target);
			}
		}
		return result;
	}

	public int getElementAmount() {
		return elementAmount;
	}
}
