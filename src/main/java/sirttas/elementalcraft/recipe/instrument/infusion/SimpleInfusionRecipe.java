package sirttas.elementalcraft.recipe.instrument.infusion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.InstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipeDisplay;

import java.util.List;

public class SimpleInfusionRecipe extends AbstractInstrumentRecipe<SingleItemSingleElementRecipeInput> implements InfusionRecipe {

    public static final MapCodec<SimpleInfusionRecipe> CODEC =  RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            ElementType.MAP_CODEC.forGetter(IElementTypeProvider::getElementType),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InstrumentRecipe::getElementAmount),
            Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(o -> o.input),
            ItemStackTemplate.MAP_CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
    ).apply(builder, SimpleInfusionRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SimpleInfusionRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            ElementType.STREAM_CODEC, r -> r.elementType,
            ByteBufCodecs.INT, r -> r.elementAmount,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.input,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            SimpleInfusionRecipe::new);

	private final Ingredient input;
	private final ItemStackTemplate result;

	public SimpleInfusionRecipe(CommonInfo commonInfo, ElementType type, int elementAmount, Ingredient input, ItemStackTemplate result) {
		super(commonInfo, type, elementAmount);
		this.input = input;
		this.result = result;
	}
	
	@Override
	public Ingredient getInput() {
		return input;
	}

    @Override
    public @NotNull ItemStack assemble(SingleItemSingleElementRecipeInput input) {
        return result.create();
    }

    @Override
    public @NotNull String group() {
        return InfusionRecipe.NAME;
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull SimpleInfusionRecipe> getSerializer() {
        return ECRecipeSerializers.INFUSION.get();
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.INFUSION.get();
    }

    @Override
    public @NotNull List<RecipeDisplay> display() {
        return List.of(new IOInstrumentRecipeDisplay(
                getElementType(),
                getElementAmount(),
                input.display(),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(ECBlocks.INFUSER.get().asItem())));
    }
}
