package sirttas.elementalcraft.interaction.jei.ingredient.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import java.util.List;

public record IngredientElementType(
		ElementType elementType,
		int amount
) implements IElementTypeProvider {

	public static final Codec<IngredientElementType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.forGetter(IngredientElementType::elementType),
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(IngredientElementType::amount)
	).apply(builder, IngredientElementType::new));

	public IngredientElementType(ElementType elementType, int amount) {
		this.elementType = elementType;
		this.amount = Mth.clamp(amount, -1, 4);
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}

	public Component getDisplayName() {
		return elementType.getDisplayName();
	}

	public IngredientElementType copy() {
		return new IngredientElementType(elementType, amount);
	}

	public static int getGaugeValue(int amount) {
		return (int) Math.log10(amount) - 1;
	}

	@Nonnull
	public static IngredientElementType fromIngredient(@Nonnull Ingredient ingredient) {
		return new IngredientElementType(getElementType(ingredient), 1);
	}

	private static ElementType getElementType(Ingredient recipe) {
		ItemStack[] stacks = recipe.getItems();

		if (stacks.length > 0) {
			return ElementType.getElementType(stacks[0]);
		}
		return ElementType.NONE;
	}

	public static List<IngredientElementType> all() {
		return all(-1);
	}

	public static List<IngredientElementType> all(int amount) {
		return ElementType.ALL_VALID.stream()
				.map(type -> new IngredientElementType(type, amount))
				.toList();
	}
}
