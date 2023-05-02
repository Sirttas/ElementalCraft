package sirttas.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.elemental.ElementalItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;

public class SourceBreedingRecipeCategory extends AbstractECRecipeCategory<ElementalItem> {

	public static final String NAME = "source_breeding";

	private final Map<ResourceKey<SourceTrait>, ISourceTraitValue> artificialTraitsMap;

	public SourceBreedingRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.source_breeding", createDrawableStack(guiHelper, new ItemStack(ECBlocks.SOURCE_BREEDER.get())), guiHelper.createBlankDrawable(67, 80));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/source_breeding.png"), 0, 0, 47, 33), 10, 10);

		artificialTraitsMap = getArtificialTraitsMap();
	}

	@Nonnull
	private static Map<ResourceKey<SourceTrait>, ISourceTraitValue> getArtificialTraitsMap() {
		var artificial = ElementalCraftApi.SOURCE_TRAIT_MANAGER.get(SourceTraits.ARTIFICIAL);

		if (artificial != null) {
			var value = artificial.load(new CompoundTag());

			if (value != null) {
				return Collections.singletonMap(SourceTraits.ARTIFICIAL, value);
			}
		}
		return Collections.emptyMap();
	}

	@Nonnull
	@Override
	public RecipeType<ElementalItem> getRecipeType() {
		return ECJEIRecipeTypes.SOURCE_BREEDING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ElementalItem seed, @Nonnull IFocusGroup focuses) {
		var type = seed.getElementType();
		var sourceReceptacle = ReceptacleHelper.create(type);
		var natural = new ItemStack(seed).is(ECTags.Items.NATURAL_SOURCE_SEEDS);

		builder.addSlot(RecipeIngredientRole.INPUT, 25, 46).addItemStack(new ItemStack(seed));
		builder.addSlot(RecipeIngredientRole.INPUT, 25, 62).addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(type, 4));

		builder.addSlot(RecipeIngredientRole.CATALYST, 4, 38).addItemStack(sourceReceptacle);
		builder.addSlot(RecipeIngredientRole.CATALYST, 48, 38).addItemStack(sourceReceptacle);

		builder.addSlot(RecipeIngredientRole.OUTPUT, 25, 2).addItemStack(ReceptacleHelper.create(type, natural ? Collections.emptyMap() : artificialTraitsMap));
	}
}
