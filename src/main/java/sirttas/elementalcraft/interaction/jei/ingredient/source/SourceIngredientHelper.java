package sirttas.elementalcraft.interaction.jei.ingredient.source;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;

public class SourceIngredientHelper implements IIngredientHelper<IngredientSource> {

	@Nonnull
    @Override
	public String getDisplayName(IngredientSource ingredient) {
		return I18n.get(ingredient.getTranslationKey());
	}

	@Nonnull
    @Override
	public String getUniqueId(@Nonnull IngredientSource ingredient, @Nonnull UidContext context) {
		return getName(ingredient);
	}

	@Nonnull
	private String getName(IngredientSource ingredient) {
		return ingredient.getElementType().getSerializedName() + "_source";
	}

	@Nonnull
	@Override
	public ResourceLocation getResourceLocation(@Nonnull IngredientSource ingredient) {
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
	public IIngredientType<IngredientSource> getIngredientType() {
		return IngredientSource.TYPE;
	}

	@Nonnull
	@Override
	public ItemStack getCheatItemStack(IngredientSource ingredient) {
		return ReceptacleHelper.create(ingredient.getElementType());
	}

}
