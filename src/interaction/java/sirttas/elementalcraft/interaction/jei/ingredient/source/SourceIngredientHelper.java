package sirttas.elementalcraft.interaction.jei.ingredient.source;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class SourceIngredientHelper implements IIngredientHelper<@NotNull IngredientSource> {

	@Nonnull
    @Override
	public String getDisplayName(IngredientSource ingredient) {
		return I18n.get(ingredient.getTranslationKey());
	}


	@Nonnull
	private String getName(IngredientSource ingredient) {
		return ingredient.getElementType().getSerializedName() + "_source";
	}

	@Nonnull
	@Override
	public Object getUid(@NotNull IngredientSource ingredient, @Nonnull UidContext context) {
		return getName(ingredient); // TODO use block instead of element type
	}

	@Nonnull
	@Override
	public Identifier getIdentifier(@Nonnull IngredientSource ingredient) {
		return ElementalCraftApi.createRL(getName(ingredient));
	}

	@Nonnull
    @Override
	public IngredientSource copyIngredient(IngredientSource ingredient) {
		return ingredient.copy();
	}

	@Override
	public boolean isValidIngredient(IngredientSource ingredient) {
		return ingredient.getElementType() != ElementType.NONE;
	}

	@Nonnull
    @Override
	public String getErrorInfo(IngredientSource ingredient) {
		return ingredient != null && ingredient.getElementType() == ElementType.NONE ? "Element shouldn't be none" : "";
	}

	@Nonnull
    @Override
	public IIngredientType<@NotNull IngredientSource> getIngredientType() {
		return ECIngredientTypes.SOURCE;
	}

	@Nonnull
	@Override
	public ItemStack getCheatItemStack(IngredientSource ingredient) {
		return ReceptacleHelper.create(ingredient.getElementType());
	}

}
