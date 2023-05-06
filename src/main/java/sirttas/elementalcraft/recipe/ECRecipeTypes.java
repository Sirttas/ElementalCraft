package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class ECRecipeTypes {
	private static final DeferredRegister<RecipeType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ElementalCraftApi.MODID);

	public static final RegistryObject<RecipeType<IInfusionRecipe>> INFUSION = register(IInfusionRecipe.NAME);
	public static final RegistryObject<RecipeType<AbstractBindingRecipe>> BINDING = register(AbstractBindingRecipe.NAME);
	public static final RegistryObject<RecipeType<CrystallizationRecipe>> CRYSTALLIZATION = register(CrystallizationRecipe.NAME);
	public static final RegistryObject<RecipeType<InscriptionRecipe>> INSCRIPTION = register(InscriptionRecipe.NAME);
	public static final RegistryObject<RecipeType<IGrindingRecipe>> AIR_MILL_GRINDING = register(IGrindingRecipe.NAME);
	public static final RegistryObject<RecipeType<SawingRecipe>> SAWING = register(SawingRecipe.NAME);
	public static final RegistryObject<RecipeType<PureInfusionRecipe>> PURE_INFUSION = register(PureInfusionRecipe.NAME);
	public static final RegistryObject<RecipeType<SpellCraftRecipe>> SPELL_CRAFT = register(SpellCraftRecipe.NAME);


	private ECRecipeTypes() {}

	private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
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
