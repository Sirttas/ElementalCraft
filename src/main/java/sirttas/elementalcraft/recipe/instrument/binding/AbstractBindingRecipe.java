package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

import javax.annotation.Nonnull;

public abstract class AbstractBindingRecipe extends AbstractInstrumentRecipe<IBinder> {

	public static final String NAME = "binding";
	public static final RecipeType<AbstractBindingRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new RecipeType<AbstractBindingRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	
	protected AbstractBindingRecipe(ResourceLocation id, ElementType type) {
		super(id, type);
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}
}
