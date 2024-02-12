package sirttas.elementalcraft.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.dpanvil.api.codec.recipe.CodecRecipeSerializer;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.spell.StaffItem;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

import java.util.function.Supplier;

public class ECRecipeSerializers {

	private static final DeferredRegister<RecipeSerializer<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ElementalCraftApi.MODID);

	public static final DeferredHolder<RecipeSerializer<?>, InfusionRecipe.Serializer> INFUSION = register(IInfusionRecipe.NAME, InfusionRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, ToolInfusionRecipe.Serializer> TOOL_INFUSION = register(ToolInfusionRecipe.NAME, ToolInfusionRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, BindingRecipe.Serializer> BINDING = register(AbstractBindingRecipe.NAME, BindingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, CrystallizationRecipe.Serializer> CRYSTALLIZATION = register(CrystallizationRecipe.NAME, CrystallizationRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, InscriptionRecipe.Serializer> INSCRIPTION = register(InscriptionRecipe.NAME, InscriptionRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, GrindingRecipe.Serializer> GRINDING = register(IGrindingRecipe.NAME, GrindingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, SawingRecipe.Serializer> SAWING = register(SawingRecipe.NAME, SawingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, CodecRecipeSerializer<PureInfusionRecipe>> PURE_INFUSION = register(PureInfusionRecipe.NAME, () -> new CodecRecipeSerializer<>(PureInfusionRecipe.CODEC));
	public static final DeferredHolder<RecipeSerializer<?>, SpellCraftRecipe.Serializer> SPELL_CRAFT = register(SpellCraftRecipe.NAME, SpellCraftRecipe.Serializer::new);
	public static final DeferredHolder<RecipeSerializer<?>, ShapedRecipe.Serializer> STAFF = register(StaffItem.NAME, StaffRecipe.Serializer::new);


	private ECRecipeSerializers() {}

	private static <R extends Recipe<?>, T extends RecipeSerializer<R>> DeferredHolder<RecipeSerializer<?>, T> register(String name, Supplier<T> serializer) {
		return DEFERRED_REGISTER.register(name, serializer);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
