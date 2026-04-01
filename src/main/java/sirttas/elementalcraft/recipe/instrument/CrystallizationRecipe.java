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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;

import javax.annotation.Nonnull;
import java.util.List;

public class CrystallizationRecipe extends AbstractInstrumentRecipe<MultipleItemsSingleElementRecipeInput> {

	public static final String NAME = "crystallization";
    private static final Codec<List<Ingredient>> INGREDIENTS_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Ingredient.CODEC.fieldOf(ECNames.GEM).forGetter(List::getFirst),
            Ingredient.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(i -> i.get(1))
    ).apply(builder, List::of));
    public static final MapCodec<CrystallizationRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.forGetter(CrystallizationRecipe::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(CrystallizationRecipe::getElementAmount),
            INGREDIENTS_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(CrystallizationRecipe::getIngredients),
            ItemStack.CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
    ).apply(builder, CrystallizationRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull CrystallizationRecipe> STREAM_CODEC = StreamCodec.of(CrystallizationRecipe::toNetwork, CrystallizationRecipe::fromNetwork); // TODO rework recipe stream codecs
	
	private final NonNullList<Ingredient> ingredients;
	private final ItemStack result;
	private final int elementAmount;

	public CrystallizationRecipe(ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStack result) {
		super(type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.result = result;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(@Nonnull MultipleItemsSingleElementRecipeInput input, @Nonnull Level level) {
		if (input.getElementType() == getElementType() && input.size() >= 2) {
			for (int i = 0; i < 2; i++) {
				if (!ingredients.get(i).test(input.getItem(i))) {
					return false;
				}
			}
			return true;
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
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return result;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.CRYSTALLIZATION.get();
	}

    private static CrystallizationRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var type = ElementType.byName(buffer.readUtf());
        var elementAmount = buffer.readInt();
        var output = ItemStack.STREAM_CODEC.decode(buffer);
        var i = buffer.readInt();
        var ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

        for (int j = 0; j < i; ++j) {
            ingredients.set(j, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        }
        return new CrystallizationRecipe(type, elementAmount, ingredients, output);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, CrystallizationRecipe recipe) {
        buffer.writeUtf(recipe.getElementType().getSerializedName());
        buffer.writeInt(recipe.getElementAmount());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        buffer.writeInt(recipe.getIngredients().size());
        recipe.getIngredients().forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient));
    }
}
