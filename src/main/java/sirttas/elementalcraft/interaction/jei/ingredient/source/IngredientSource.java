package sirttas.elementalcraft.interaction.jei.ingredient.source;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.network.chat.Component;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import javax.annotation.Nonnull;
import java.util.List;

public record IngredientSource(ElementType elementType) implements IElementTypeProvider {

	public static final IIngredientType<IngredientSource> TYPE = () -> IngredientSource.class;


	@Override
	public ElementType getElementType() {
		return elementType;
	}

	public Component getDisplayName() {
		return Component.translatable(getTranslationKey());
	}

	@Nonnull
	public String getTranslationKey() {
		return "block.elementalcraft.source." + elementType.getSerializedName();
	}

	public IngredientSource copy() {
		return new IngredientSource(elementType);
	}


	public static List<IngredientSource> all() {
		return ElementType.ALL_VALID.stream()
				.map(IngredientSource::new)
				.toList();
	}
}
