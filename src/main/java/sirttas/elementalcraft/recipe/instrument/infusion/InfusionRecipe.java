package sirttas.elementalcraft.recipe.instrument.infusion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

import javax.annotation.Nonnull;

public class InfusionRecipe extends AbstractInstrumentRecipe<IInfuser> implements IInfusionRecipe {

	public static final Codec<InfusionRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(InfusionRecipe::getElementType),
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InfusionRecipe::getElementAmount),
			Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(InfusionRecipe::getInput),
			ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
	).apply(builder, InfusionRecipe::new));

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
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return output;
	}

	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.INFUSION.get();
	}

	public static class Serializer implements RecipeSerializer<InfusionRecipe> {

		@Override
		@Nonnull
		public Codec<InfusionRecipe> codec() {
			return CODEC;
		}

		@Override
		public InfusionRecipe fromNetwork(FriendlyByteBuf buffer) {
			ElementType type = ElementType.byName(buffer.readUtf());
			int elementAmount = buffer.readInt();
			Ingredient input = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new InfusionRecipe(type, elementAmount, input, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, InfusionRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
			recipe.getInput().toNetwork(buffer);
			buffer.writeItem(recipe.output);
		}
	}
}
