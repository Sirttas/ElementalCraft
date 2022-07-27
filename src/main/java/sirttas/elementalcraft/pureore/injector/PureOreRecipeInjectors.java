package sirttas.elementalcraft.pureore.injector;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.PureOreException;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.registry.RegistryHelper;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PureOreRecipeInjectors {


	public static final ResourceKey<Registry<AbstractPureOreRecipeInjector<?, ?>>> REGISTRY_KEY = ElementalCraft.createRegistryKey(ECNames.PURE_ORE_RECIPE_INJECTOR);
	private static final DeferredRegister<AbstractPureOreRecipeInjector<?, ?>> DEFERRED_REGISTER = DeferredRegister.create(REGISTRY_KEY, ElementalCraftApi.MODID);
	public static final Supplier<IForgeRegistry<AbstractPureOreRecipeInjector<?, ?>>> REGISTRY = DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

	private PureOreRecipeInjectors() {}
	
	@SubscribeEvent
	public static void registerPureOreRecipeInjectors(RegisterEvent event) {
		if (!REGISTRY_KEY.equals(event.getRegistryKey())){
			return;
		}

		IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry = event.getForgeRegistry();

		register(registry, new PureOreCookingRecipeInjector<>(RecipeType.SMELTING, SmeltingRecipe::new));
		register(registry, new PureOreCookingRecipeInjector<>(RecipeType.BLASTING, BlastingRecipe::new));
		register(registry, new PureOreCookingRecipeInjector<>(RecipeType.CAMPFIRE_COOKING, CampfireCookingRecipe::new));
		register(registry, new PureOreGrindingRecipeInjector());

		if (ECinteractions.isMekanismActive()) {
			MekanismInteraction.registerPureOreRecipeInjectors(registry);
		}
	}

	public static <C extends Container, T extends Recipe<C>> void register(IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry, AbstractPureOreRecipeInjector<C, T> injector) {
		ResourceLocation id = injector.getRecipeTypeRegistryName();

		if (id == null) {
			throw new PureOreException("Cannot register injector as its RecipeType is absent in registry.");
		}
		RegistryHelper.register(registry, injector, ElementalCraft.createRL(id.getNamespace() + '_' + id.getPath()));
	}

	public static void register(IEventBus modBus) {
		DEFERRED_REGISTER.register(modBus);
	}

}
