package sirttas.elementalcraft.recipe.instrument;

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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;

import javax.annotation.Nonnull;
import java.util.List;

public class InscriptionRecipe extends AbstractInstrumentRecipe<MultipleItemsSingleElementRecipeInput> {

	public static final String NAME = "inscription";
    public static final MapCodec<InscriptionRecipe> CODEC =  RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            ElementType.forGetter(IElementTypeProvider::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InstrumentRecipe::getElementAmount),
            Codec.lazyInitialized(() -> Ingredient.CODEC.listOf(4, 4)).fieldOf("ingredients").forGetter(o -> o.ingredients),
            ItemStackTemplate.MAP_CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
    ).apply(builder, InscriptionRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull InscriptionRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            ElementType.STREAM_CODEC, r -> r.elementType,
            ByteBufCodecs.INT, r -> r.elementAmount,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.ingredients,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            InscriptionRecipe::new);

	private final List<Ingredient> ingredients;
	private final ItemStackTemplate result;

	public InscriptionRecipe(CommonInfo commonInfo, ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStackTemplate result) {
        super(commonInfo, type, elementAmount);
		this.ingredients = List.copyOf(ingredients);
		this.result = result;
	}

	@Override
	public boolean matches(@Nonnull MultipleItemsSingleElementRecipeInput input, @Nonnull Level level) {
        if (input.getElementType() != getElementType() || input.size() < 4) {
            return false;
        }
        return RecipeHelper.matchesUnordered(input.stacks(), ingredients);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull MultipleItemsSingleElementRecipeInput input) {
        return result.create();
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull InscriptionRecipe> getSerializer() {
        return ECRecipeSerializers.INSCRIPTION.get();
    }

    @Override
    public @NotNull RecipeType<@NotNull InscriptionRecipe> getType() {
        return ECRecipeTypes.INSCRIPTION.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredients);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.INSCRIPTION.get();
    }
}
