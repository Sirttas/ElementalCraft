package sirttas.elementalcraft.recipe.instrument.infusion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;

import javax.annotation.Nonnull;

public class ToolInfusionRecipe implements IInfusionRecipe {

	public static final String NAME = "tool_" + IInfusionRecipe.NAME;

	public static final Codec<ToolInfusionRecipe> CODEC =  RecordCodecBuilder.create(builder -> builder.group(
			ElementalCraftApi.TOOL_INFUSION_MANAGER.holderCodec().fieldOf(ECNames.TOOL_INFUSION).forGetter(r -> r.toolInfusion),
			Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(ToolInfusionRecipe::getInput),
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(ToolInfusionRecipe::getElementAmount)
	).apply(builder, ToolInfusionRecipe::new));

	private final Ingredient input;
	private final int elementAmount;
	private final Holder<ToolInfusion> toolInfusion;
	
	public ToolInfusionRecipe(Holder<ToolInfusion> toolInfusion, Ingredient input, int elementAmount) {
		this.toolInfusion = toolInfusion;
		this.input = input;
		this.elementAmount = elementAmount;
	}
	
	@Override
	public boolean matches(@NotNull IInfuser instrument, @Nonnull Level level) {
		return IInfusionRecipe.super.matches(instrument, level) && !getToolInfusion().equals(ToolInfusionHelper.getInfusion(instrument.getItem()));
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
	public @NotNull ItemStack assemble(@NotNull IInfuser instrument, @Nonnull RegistryAccess registry) {
		ItemStack stack = instrument.getItem().copy();

		ToolInfusionHelper.setInfusion(stack, getToolInfusion());
		return stack;
	}

	@Nonnull
    @Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ElementType getElementType() {
		return getToolInfusion().getElementType();
	}

	public ToolInfusion getToolInfusion() {
		return  toolInfusion.value();
	}
	
	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.TOOL_INFUSION.get();
	}

	public static class Serializer implements RecipeSerializer<ToolInfusionRecipe> {

		@Override
	 	@Nonnull
		public Codec<ToolInfusionRecipe> codec() {
			return CODEC;
		}

		@Override
		public ToolInfusionRecipe fromNetwork(FriendlyByteBuf buffer) {
			var elementAmount = buffer.readInt();
			var input = Ingredient.fromNetwork(buffer);
			var toolInfusion = ElementalCraftApi.TOOL_INFUSION_MANAGER.getOrCreateHolder(buffer.readResourceLocation());

			return new ToolInfusionRecipe(toolInfusion, input, elementAmount);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ToolInfusionRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			recipe.getInput().toNetwork(buffer);
			buffer.writeResourceLocation(recipe.getToolInfusion().getId());
		}
	}
}
