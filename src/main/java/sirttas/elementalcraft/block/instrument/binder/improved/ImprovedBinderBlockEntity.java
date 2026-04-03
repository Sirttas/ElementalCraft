package sirttas.elementalcraft.block.instrument.binder.improved;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BinderInfusionRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

public class ImprovedBinderBlockEntity extends BinderBlockEntity implements IInfuser {

	public static final ResourceKey<@NotNull IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(ImprovedBinderBlock.NAME);
	private static final Holder<@NotNull IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public ImprovedBinderBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.BINDER_IMPROVED, PROPERTIES, pos, state);
	}

	@Override
	protected AbstractBindingRecipe lookupRecipe(@NotNull MultipleItemsSingleElementRecipeInput recipeInput) {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}

		var bindingRecipe = super.lookupRecipe(recipeInput);

		if (bindingRecipe == null) {
			InfusionRecipe infusionRecipe = this.lookupInfusionRecipe(level);

			return infusionRecipe != null ? new BinderInfusionRecipeWrapper(infusionRecipe) : null;
		}
		return bindingRecipe;
	}
}
