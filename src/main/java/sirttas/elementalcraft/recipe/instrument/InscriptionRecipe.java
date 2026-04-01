package sirttas.elementalcraft.recipe.instrument;

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
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;

import javax.annotation.Nonnull;
import java.util.List;

public class InscriptionRecipe extends AbstractInstrumentRecipe<MultipleItemsSingleElementRecipeInput> {

	public static final String NAME = "inscription";
    public static final MapCodec<InscriptionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.forGetter(InscriptionRecipe::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InscriptionRecipe::getElementAmount),
            Ingredient.LIST_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(InscriptionRecipe::getIngredients),
            ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
    ).apply(builder, InscriptionRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull InscriptionRecipe> STREAM_CODEC = StreamCodec.of(InscriptionRecipe::toNetwork, InscriptionRecipe::fromNetwork); // TODO rework recipe stream codecs

	private final NonNullList<Ingredient> ingredients;
	private final int elementAmount;
	private final ItemStack output;

	public InscriptionRecipe(ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStack output) {
		super(type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.elementAmount = elementAmount;
		this.output = output;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(@Nonnull MultipleItemsSingleElementRecipeInput input, @Nonnull Level level) {
		if (input.getElementType() == getElementType()) {
			return RecipeHelper.matchesUnordered(input.stacks(), ingredients);
		}
		return false;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
		return output;
	}

    private static InscriptionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var type = ElementType.byName(buffer.readUtf());
        var elementAmount = buffer.readInt();
        var output = ItemStack.STREAM_CODEC.decode(buffer);
        var i = buffer.readInt();
        var ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

        for (int j = 0; j < i; ++j) {
            ingredients.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        }

        return new InscriptionRecipe(type, elementAmount, ingredients, output);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, InscriptionRecipe recipe) {
        buffer.writeUtf(recipe.getElementType().getSerializedName());
        buffer.writeInt(recipe.getElementAmount());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
        buffer.writeInt(recipe.getIngredients().size());
        recipe.getIngredients().forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient));
    }
}
