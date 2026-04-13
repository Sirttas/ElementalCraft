package sirttas.elementalcraft.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipe;

public class ECRecipeTypes {
	private static final DeferredRegister<@NotNull RecipeType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull InfusionRecipe>> INFUSION = register(InfusionRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull AbstractBindingRecipe>> BINDING = register(AbstractBindingRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull CrystallizationRecipe>> CRYSTALLIZATION = register(CrystallizationRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull InscriptionRecipe>> INSCRIPTION = register(InscriptionRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull OrePurificationRecipe>> ORE_PURIFICATION = register(OrePurificationRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull GrindingRecipe>> GRINDING = register(GrindingRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull SawingRecipe>> SAWING = register(SawingRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull PureInfusionRecipe>> PURE_INFUSION = register(PureInfusionRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull SpellCraftRecipe>> SPELL_CRAFT = register(SpellCraftRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull CrackingRecipe>> CRACKING = register(CrackingRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull SculkCrackingRecipe>> SCULK_CRACKING = register(SculkCrackingRecipe.NAME);
	public static final DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull MeltingRecipe>> MELTING = register(MeltingRecipe.NAME);

	private ECRecipeTypes() {}

	private static <T extends Recipe<?>> DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull T>> register(String name) {
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
