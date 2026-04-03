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
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;

import javax.annotation.Nonnull;
import java.util.List;

public class CrystallizationRecipe extends AbstractInstrumentRecipe<MultipleItemsSingleElementRecipeInput> {

	public static final String NAME = "crystallization";
    public static final MapCodec<CrystallizationRecipe> CODEC =  RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            ElementType.forGetter(IElementTypeProvider::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InstrumentRecipe::getElementAmount),
            Ingredient.CODEC.fieldOf(ECNames.GEM).forGetter(r -> r.gem),
            Ingredient.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(r -> r.crystal),
            ItemStackTemplate.MAP_CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
    ).apply(builder, CrystallizationRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull CrystallizationRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            ElementType.STREAM_CODEC, r -> r.elementType,
            ByteBufCodecs.INT, r -> r.elementAmount,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.gem,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.crystal,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            CrystallizationRecipe::new);

    private final Ingredient gem;
    private final Ingredient crystal;
    private final ItemStackTemplate result;

	public CrystallizationRecipe(CommonInfo commonInfo, ElementType type, int elementAmount, Ingredient gem, Ingredient crystal, ItemStackTemplate result) {
        super(commonInfo, type, elementAmount);
        this.gem = gem;
        this.crystal = crystal;
        this.result = result;
    }

	@Override
	public boolean matches(@Nonnull MultipleItemsSingleElementRecipeInput input, @Nonnull Level level) {
        if (input.getElementType() != getElementType() || input.size() < 2) {
            return false;
        }
        return gem.test(input.getItem(0)) && crystal.test(input.getItem(1));
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
    public @NotNull RecipeSerializer<@NotNull CrystallizationRecipe> getSerializer() {
        return ECRecipeSerializers.CRYSTALLIZATION.get();
    }

    @Nonnull
	@Override
	public RecipeType<@NotNull CrystallizationRecipe> getType() {
		return ECRecipeTypes.CRYSTALLIZATION.get();
	}

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(List.of(gem, crystal));
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.CRYSTALLIZATION.get();
    }
}
