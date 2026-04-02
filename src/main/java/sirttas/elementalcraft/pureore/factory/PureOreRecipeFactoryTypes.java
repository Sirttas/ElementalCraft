package sirttas.elementalcraft.pureore.factory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.PureOreException;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.registry.RegistryHelper;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class PureOreRecipeFactoryTypes {


	public static final ResourceKey<@NotNull Registry<@NotNull IPureOreRecipeFactoryType<?, ?>>> REGISTRY_KEY = ElementalCraft.createRegistryKey(ECNames.PURE_ORE_RECIPE_FACTORY_TYPE);
	private static final DeferredRegister<@NotNull IPureOreRecipeFactoryType<?, ?>> DEFERRED_REGISTER = DeferredRegister.create(REGISTRY_KEY, ElementalCraftApi.MODID);
	public static final Registry<@NotNull IPureOreRecipeFactoryType<?, ?>> REGISTRY = DEFERRED_REGISTER.makeRegistry(Consumers.nop());

	private PureOreRecipeFactoryTypes() {}

	@SubscribeEvent
	public static void registerPureOreRecipeInjectors(RegisterEvent event) {
		event.register(REGISTRY_KEY, helper -> {
			registerCooking(helper, RecipeType.SMELTING, SmeltingRecipe::new);
			registerCooking(helper, RecipeType.BLASTING, BlastingRecipe::new);
			registerCooking(helper, RecipeType.SMOKING, SmokingRecipe::new);
			registerCooking(helper, RecipeType.CAMPFIRE_COOKING, CampfireCookingRecipe::new);
			register(helper, IGrindingRecipe.NAME, PureOreGrindingRecipeFactory::new);

            ElementalCraft.interactions().registerPureOreRecipeInjectors(helper);
		});
	}

	public static <T extends AbstractCookingRecipe> void registerCooking(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, RecipeType<@NotNull T> recipeType, PureOreCookingRecipeFactory.Factory<T> factory) {
		register(registry, recipeType, m -> new PureOreCookingRecipeFactory<T>(m, recipeType, factory));
	}

	public static <C extends RecipeInput, T extends Recipe<@NotNull C>> void register(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, RecipeType<?> recipeType, IPureOreRecipeFactoryType<C, T> type) {
		var id = BuiltInRegistries.RECIPE_TYPE.getKey(recipeType);

		if (id == null) {
			throw new PureOreException("Cannot register factory as its RecipeType is absent in registry.");
		}
		register(registry, id, type);
	}

	public static <C extends RecipeInput, T extends Recipe<@NotNull C>> void register(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, Identifier id, IPureOreRecipeFactoryType<C, T> type) {
		var namespace = id.getNamespace();
		var path = id.getPath();

		register(registry, ElementalCraftApi.MODID.equals(namespace) ? path : namespace + '/' + path, type);
	}


	public static <C extends RecipeInput, T extends Recipe<@NotNull C>> void register(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, String name, IPureOreRecipeFactoryType<C, T> type) {
		RegistryHelper.register(registry, type, name);
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}
}
