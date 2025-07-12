package sirttas.elementalcraft.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;

public class ECRecipeTypes {
	private static final DeferredRegister<RecipeType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<RecipeType<?>, RecipeType<IInfusionRecipe>> INFUSION = register(IInfusionRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<AbstractBindingRecipe>> BINDING = register(AbstractBindingRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<CrystallizationRecipe>> CRYSTALLIZATION = register(CrystallizationRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<InscriptionRecipe>> INSCRIPTION = register(InscriptionRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<OrePurificationRecipe>> ORE_PURIFICATION = register(OrePurificationRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<IGrindingRecipe>> GRINDING = register(IGrindingRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<SawingRecipe>> SAWING = register(SawingRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<PureInfusionRecipe>> PURE_INFUSION = register(PureInfusionRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<SpellCraftRecipe>> SPELL_CRAFT = register(SpellCraftRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<CrackingRecipe>> CRACKING = register(CrackingRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<SculkCrackingRecipe>> SCULK_CRACKING = register(SculkCrackingRecipe.NAME);
	public static final DeferredHolder<RecipeType<?>, RecipeType<MeltingRecipe>> MELTING = register(MeltingRecipe.NAME);


	private ECRecipeTypes() {}

	private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> register(String name) {
		return DEFERRED_REGISTER.register(name, () -> new RecipeType<>() {
			@Override
			public String toString() {
				return name;
			}
		});
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
