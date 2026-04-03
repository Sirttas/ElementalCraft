package sirttas.elementalcraft.recipe.instrument.infusion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;

import javax.annotation.Nonnull;

public class ToolInfusionRecipe implements InfusionRecipe {

	public static final String NAME = "tool_" + InfusionRecipe.NAME;
    public static final MapCodec<ToolInfusionRecipe> CODEC =  RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(ToolInfusionRecipe::getElementAmount),
            ToolInfusion.HOLDER_CODEC.fieldOf(ECNames.TOOL_INFUSION).forGetter(r -> r.toolInfusion),
            Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(o -> o.input)
    ).apply(builder, ToolInfusionRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ToolInfusionRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            ByteBufCodecs.INT, r -> r.elementAmount,
            ToolInfusion.STREAM_CODEC, r -> r.toolInfusion,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.input,
            ToolInfusionRecipe::new);

    private final Recipe.CommonInfo commonInfo;
    private final int elementAmount;
	private final Ingredient input;
	private final Holder<@NotNull ToolInfusion> toolInfusion;
	
	public ToolInfusionRecipe(Recipe.CommonInfo commonInfo, int elementAmount, Holder<@NotNull ToolInfusion> toolInfusion, Ingredient input) {
        this.commonInfo = commonInfo;
		this.toolInfusion = toolInfusion;
		this.input = input;
		this.elementAmount = elementAmount;
	}
	
	@Override
	public boolean matches(@NotNull SingleItemSingleElementRecipeInput input, @Nonnull Level level) {
		return InfusionRecipe.super.matches(input, level) && !getToolInfusion().equals(ToolInfusionHelper.getInfusion(input.getItem(0)));
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public Ingredient getInput() {
		return input;
	}
	
	@Override
	public @NotNull ItemStack assemble(@NotNull SingleItemSingleElementRecipeInput input) {
		var stack = input.getItem(0).copy();

		ToolInfusionHelper.setInfusion(stack, toolInfusion);
		return stack;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull ToolInfusionRecipe> getSerializer() {
        return ECRecipeSerializers.TOOL_INFUSION.get();
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.TOOL_INFUSION.get();
    }

    @Override
	public @NotNull ElementType getElementType() {
		return getToolInfusion().value().getElementType();
	}

	public Holder<@NotNull ToolInfusion> getToolInfusion() {
		return toolInfusion;
	}
}
