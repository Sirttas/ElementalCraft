package sirttas.elementalcraft.interaction.jei.ingredient.source;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import javax.annotation.Nonnull;
import java.util.List;

public record IngredientSource(ElementType elementType) implements IElementTypeProvider {

	public static final Codec<IngredientSource> CODEC = ElementType.CODEC.xmap(IngredientSource::new, IngredientSource::getElementType);

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}

	public Component getDisplayName() {
		return Component.translatable(getTranslationKey());
	}

	@Nonnull
	public String getTranslationKey() {
		return "block.elementalcraft." + elementType.getSerializedName() + "_source";
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
