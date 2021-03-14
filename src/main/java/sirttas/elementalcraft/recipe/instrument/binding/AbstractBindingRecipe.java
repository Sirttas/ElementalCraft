package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

public abstract class AbstractBindingRecipe extends AbstractInstrumentRecipe<IBinder> {

	public static final String NAME = "binding";
	public static final IRecipeType<AbstractBindingRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<AbstractBindingRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});
	
	protected AbstractBindingRecipe(ResourceLocation id, ElementType type) {
		super(id, type);
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}
}
