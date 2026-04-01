package sirttas.elementalcraft.recipe.instrument.infusion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

import javax.annotation.Nonnull;

public class InfusionRecipe extends AbstractInstrumentRecipe<SingleItemSingleElementRecipeInput> implements IInfusionRecipe {

    public static final MapCodec<InfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ElementType.forGetter(InfusionRecipe::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InfusionRecipe::getElementAmount),
            Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(InfusionRecipe::getInput),
            ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
    ).apply(builder, InfusionRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull InfusionRecipe> STREAM_CODEC = StreamCodec.of(InfusionRecipe::toNetwork, InfusionRecipe::fromNetwork); // TODO rework recipe stream codecs

	private final Ingredient input;
	private final ItemStack output;
	private final int elementAmount;

	public InfusionRecipe(ElementType type, int elementAmount, Ingredient input, ItemStack output) {
		super(type);
		this.input = input;
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}
	
	@Override
	public Ingredient getInput() {
		return input;
	}

	@Nonnull
    @Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return output;
	}

    public static InfusionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var type = ElementType.byName(buffer.readUtf());
        var elementAmount = buffer.readInt();
        var input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var output = ItemStack.STREAM_CODEC.decode(buffer);

        return new InfusionRecipe(type, elementAmount, input, output);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, InfusionRecipe recipe) {
        buffer.writeUtf(recipe.getElementType().getSerializedName());
        buffer.writeInt(recipe.getElementAmount());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
    }

    @Override
    public ItemStack assemble(SingleItemSingleElementRecipeInput input) {
        return null;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleItemSingleElementRecipeInput>> getSerializer() {
        return null;
    }

    @Override
    public PlacementInfo placementInfo() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
}
