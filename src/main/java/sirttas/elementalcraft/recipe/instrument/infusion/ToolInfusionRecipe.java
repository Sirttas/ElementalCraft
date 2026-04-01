package sirttas.elementalcraft.recipe.instrument.infusion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;

import javax.annotation.Nonnull;

public class ToolInfusionRecipe implements IInfusionRecipe {

	public static final String NAME = "tool_" + IInfusionRecipe.NAME;
    public static final MapCodec<ToolInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ToolInfusion.HOLDER_CODEC.fieldOf(ECNames.TOOL_INFUSION).forGetter(r -> r.toolInfusion),
            Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(ToolInfusionRecipe::getInput),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(ToolInfusionRecipe::getElementAmount)
    ).apply(builder, ToolInfusionRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ToolInfusionRecipe> STREAM_CODEC = StreamCodec.of(ToolInfusionRecipe::toNetwork, ToolInfusionRecipe::fromNetwork); // TODO rework recipe stream codecs

	private final Ingredient input;
	private final int elementAmount;
	private final Holder<ToolInfusion> toolInfusion;
	
	public ToolInfusionRecipe(Holder<ToolInfusion> toolInfusion, Ingredient input, int elementAmount) {
		this.toolInfusion = toolInfusion;
		this.input = input;
		this.elementAmount = elementAmount;
	}
	
	@Override
	public boolean matches(@NotNull SingleItemSingleElementRecipeInput input, @Nonnull Level level) {
		return IInfusionRecipe.super.matches(input, level) && !getToolInfusion().equals(ToolInfusionHelper.getInfusion(input.getItem(0)));
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

	@Nonnull
    @Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public @NotNull ElementType getElementType() {
		return getToolInfusion().value().getElementType();
	}

	public Holder<ToolInfusion> getToolInfusion() {
		return toolInfusion;
	}

    public static ToolInfusionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var elementAmount = buffer.readInt();
        var input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var toolInfusion = ElementalCraftApi.TOOL_INFUSION_MANAGER.getOrCreateHolder(buffer.readIdentifier());

        return new ToolInfusionRecipe(toolInfusion, input, elementAmount);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, ToolInfusionRecipe recipe) {
        buffer.writeInt(recipe.getElementAmount());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
        buffer.writeIdentifier(ElementalCraftApi.TOOL_INFUSION_MANAGER.getId(recipe.getToolInfusion()));
    }
}
