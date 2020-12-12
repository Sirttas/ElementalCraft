package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;

public abstract class AbstractInfusionRecipe extends AbstractInstrumentRecipe<TileInfuser> {

	private static final String NAME = "infusion";
	public static final IRecipeType<AbstractInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<AbstractInfusionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@Override
	public boolean matches(TileInfuser inv) {
		ItemStack stack = inv.getItem();
		TileTank tank = inv.getTank();
		
		return !stack.isEmpty() && tank != null && tank.getElementAmount() >= this.getElementPerTick();
	}

	public AbstractInfusionRecipe(ResourceLocation id, ElementType type) {
		super(id, type);
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}
}