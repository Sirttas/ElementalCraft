package sirttas.elementalcraft.recipe.instrument.binding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;

import javax.annotation.Nonnull;
import java.util.List;

public class BindingRecipe extends AbstractBindingRecipe {

    public static final MapCodec<BindingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.forGetter(BindingRecipe::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(BindingRecipe::getElementAmount),
            Ingredient.LIST_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(BindingRecipe::getIngredients),
            ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
    ).apply(builder, BindingRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull BindingRecipe> STREAM_CODEC = StreamCodec.of(BindingRecipe::toNetwork, BindingRecipe::fromNetwork); // TODO rework recipe stream codecs

	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;
	private final int elementAmount;

	public BindingRecipe(ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStack output) {
		super(type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(MultipleItemsSingleElementRecipeInput input, @Nonnull Level level) {
		if (input.getElementType() != getElementType() || input.size() != ingredients.size()) {
			return false;
		}
		return Boolean.TRUE.equals(ECConfig.SERVER.binderRecipeMatchOrder.get()) ? matchesOrdered(input) : RecipeHelper.matchesUnordered(input.stacks(), ingredients);
	}

	private boolean matchesOrdered(MultipleItemsSingleElementRecipeInput input) {
		int ingredientIndex = 0;

		for (int i = 0; i < input.size(); i++) {
			var s = input.getItem(i);

			if (s.isEmpty()) {
				continue;
			} else if (ingredientIndex >= ingredients.size() || !ingredients.get(ingredientIndex).test(s)) {
				return false;
			}
			ingredientIndex++;
		}
		return true;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return output;
	}


    public static BindingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var type = ElementType.byName(buffer.readUtf());
        var elementAmount = buffer.readInt();
        var output = ItemStack.STREAM_CODEC.decode(buffer);
        var i = buffer.readVarInt();
        var ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

        ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        return new BindingRecipe(type, elementAmount, ingredients, output);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, BindingRecipe recipe) {
        buffer.writeUtf(recipe.getElementType().getSerializedName());
        buffer.writeInt(recipe.getElementAmount());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
        buffer.writeVarInt(recipe.getIngredients().size());

        for (Ingredient ingredient : recipe.getIngredients()) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
    }
}
