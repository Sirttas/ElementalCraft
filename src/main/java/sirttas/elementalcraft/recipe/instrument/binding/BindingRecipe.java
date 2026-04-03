package sirttas.elementalcraft.recipe.instrument.binding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.InstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class BindingRecipe extends AbstractBindingRecipe {

    public static final MapCodec<BindingRecipe> CODEC =  RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            ElementType.forGetter(IElementTypeProvider::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InstrumentRecipe::getElementAmount),
            Codec.lazyInitialized(() -> Ingredient.CODEC.sizeLimitedListOf(BinderBlockEntity.MAX_INVENTORY_SIZE)).fieldOf("ingredients").forGetter(o -> o.ingredients),
            ItemStackTemplate.MAP_CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.result)
    ).apply(builder, BindingRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull BindingRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            ElementType.STREAM_CODEC, r -> r.elementType,
            ByteBufCodecs.INT, r -> r.elementAmount,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.ingredients,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            BindingRecipe::new);

	private final List<Ingredient> ingredients;
	private final ItemStackTemplate result;

	public BindingRecipe(CommonInfo commonInfo, ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStackTemplate result) {
        super(commonInfo, type, elementAmount);
		this.ingredients = List.copyOf(ingredients);
		this.result = result;
	}

	@Override
	public boolean matches(MultipleItemsSingleElementRecipeInput input, @Nonnull Level level) {
		if (input.getElementType() != getElementType() || input.size() != ingredients.size()) {
			return false;
		}
		return ECConfig.SERVER.binderRecipeMatchOrder.get() ? matchesOrdered(input) : RecipeHelper.matchesUnordered(input.stacks(), ingredients);
	}

    @Override
    public @NotNull ItemStack assemble(@NotNull MultipleItemsSingleElementRecipeInput input) {
        return result.create();
    }

    @Override
    public @NotNull String group() {
        return AbstractBindingRecipe.NAME;
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull BindingRecipe> getSerializer() {
        return ECRecipeSerializers.BINDING.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredients);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.BINDING.get();
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
}
