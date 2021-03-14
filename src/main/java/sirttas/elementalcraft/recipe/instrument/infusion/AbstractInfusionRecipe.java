package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

public abstract class AbstractInfusionRecipe extends AbstractInstrumentRecipe<IInfuser> {

	public static final String NAME = "infusion";
	public static final IRecipeType<AbstractInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<AbstractInfusionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@Override
	public boolean matches(IInfuser inv) {
		ItemStack stack = inv.getItem();
		ISingleElementStorage tank = inv.getTank();
		
		return !stack.isEmpty() && tank != null;
	}

	protected AbstractInfusionRecipe(ResourceLocation id, ElementType type) {
		super(id, type);
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}
}