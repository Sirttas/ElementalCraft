package sirttas.elementalcraft.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.source.receptacle.NaturalSourceIngredient;
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

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRecipeSerializers {

	private static final DeferredRegister<RecipeSerializer<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElementalCraftApi.MODID);

	public static final RegistryObject<InfusionRecipe.Serializer> INFUSION = register(InfusionRecipe.Serializer::new, IInfusionRecipe.NAME);
	public static final RegistryObject<ToolInfusionRecipe.Serializer> TOOL_INFUSION = register(ToolInfusionRecipe.Serializer::new, ToolInfusionRecipe.NAME);
	public static final RegistryObject<BindingRecipe.Serializer> BINDING = register(BindingRecipe.Serializer::new, AbstractBindingRecipe.NAME);
	public static final RegistryObject<CrystallizationRecipe.Serializer> CRYSTALLIZATION = register(CrystallizationRecipe.Serializer::new, CrystallizationRecipe.NAME);
	public static final RegistryObject<InscriptionRecipe.Serializer> INSCRIPTION = register(InscriptionRecipe.Serializer::new, InscriptionRecipe.NAME);
	public static final RegistryObject<GrindingRecipe.Serializer> GRINDING = register(GrindingRecipe.Serializer::new, IGrindingRecipe.NAME);
	public static final RegistryObject<SawingRecipe.Serializer> SAWING = register(SawingRecipe.Serializer::new, SawingRecipe.NAME);
	public static final RegistryObject<PureInfusionRecipe.Serializer> PURE_INFUSION = register(PureInfusionRecipe.Serializer::new, PureInfusionRecipe.NAME);
	public static final RegistryObject<SpellCraftRecipe.Serializer> SPELL_CRAFT = register(SpellCraftRecipe.Serializer::new, SpellCraftRecipe.NAME);
	public static final RegistryObject<ShapedRecipe.Serializer> STAFF = register(StaffRecipe.Serializer::new, StaffItem.NAME);
	

	private ECRecipeSerializers() {}

	private static <R extends Recipe<?>, T extends RecipeSerializer<R>> RegistryObject<T> register(Supplier<T> serializer, String name) {
		return DEFERRED_REGISTER.register(name, serializer);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}

	@SubscribeEvent
	public static void registerRecipeSerializers(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(new ResourceLocation("elementalcraft", "natural_source"), NaturalSourceIngredient.Serializer.INSTANCE);
		}
	}
}
