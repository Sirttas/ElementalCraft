package sirttas.elementalcraft;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.NeoForge;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.imc.DataManagerIMC;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderTypes;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.infusion.tool.effect.ToolInfusionEffectTypes;
import sirttas.elementalcraft.item.ECCreativeModeTabs;
import sirttas.elementalcraft.item.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.loot.ECLootModifiers;
import sirttas.elementalcraft.loot.entry.ECLootPoolEntries;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.particle.ECParticles;
import sirttas.elementalcraft.pureore.PureOreManager;
import sirttas.elementalcraft.pureore.injector.PureOreRecipeFactoryTypes;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;
import sirttas.elementalcraft.pureore.loader.PureOreLoaderTypes;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.sound.ECSounds;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.world.feature.ECFeatures;
import sirttas.elementalcraft.world.feature.placement.ECPlacements;
import sirttas.elementalcraft.world.feature.structure.ECStructureTypes;

import java.util.Map;

@Mod(ElementalCraftApi.MODID)
public class ElementalCraft {
	
	public static final PureOreManager PURE_ORE_MANAGER = new PureOreManager();

	public static final ResourceKey<IDataManager<ShrineUpgrade>> SHRINE_UPGRADE_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SHRINE_UPGRADE));
	public static final IDataManager<ShrineUpgrade> SHRINE_UPGRADE_MANAGER = IDataManager.builder(ShrineUpgrade.class, SHRINE_UPGRADE_MANAGER_KEY)
			.withIdSetter(ShrineUpgrade::setId)
			.merged(ShrineUpgrade::merge)
			.build();

	public static final ResourceKey<IDataManager<SpellProperties>> SPELL_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SPELL_PROPERTIES));
	public static final IDataManager<SpellProperties> SPELL_PROPERTIES_MANAGER = IDataManager.builder(SpellProperties.class, SPELL_PROPERTIES_MANAGER_KEY)
			.withDefault(SpellProperties.NONE)
			.build();

	public static final ResourceKey<IDataManager<ShrineProperties>> SHRINE_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SHRINE_PROPERTIES));
	public static final IDataManager<ShrineProperties> SHRINE_PROPERTIES_MANAGER = IDataManager.builder(ShrineProperties.class, SHRINE_PROPERTIES_MANAGER_KEY)
			.withDefault(ShrineProperties.DEFAULT)
			.build();

	public static final ResourceKey<IDataManager<IPureOreLoader>> PURE_ORE_LOADERS_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.PURE_ORE_LOADER));
	public static final IDataManager<IPureOreLoader> PURE_ORE_LOADERS_MANAGER = IDataManager.builder(IPureOreLoader.class, PURE_ORE_LOADERS_MANAGER_KEY)
			.build();

	public ElementalCraft(IEventBus modBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ECConfig.CLIENT_SPEC);

		ECBlocks.register(modBus);
		ECBlockEntityTypes.register(modBus);
		ECItems.register(modBus);
		ECEntities.register(modBus);
		ECDataAttachments.register(modBus);
		Spells.register(modBus);
		Jewels.register(modBus);
		ECMenus.register(modBus);
		ECParticles.register(modBus);
		ECRecipeTypes.register(modBus);
		ECRecipeSerializers.register(modBus);
		ECFeatures.register(modBus);
		ECStructureTypes.register(modBus);
		ECPlacements.register(modBus);
		ECLootPoolEntries.register(modBus);
		ECLootFunctions.register(modBus);
		ECLootModifiers.register(modBus);
		ECBlockPosPredicateTypes.register(modBus);
		ToolInfusionEffectTypes.register(modBus);
		SourceTraitValueProviderTypes.register(modBus);
		PureOreRecipeFactoryTypes.register(modBus);
		PipeUpgradeTypes.register(modBus);
		ECSounds.register(modBus);
		ECCreativeModeTabs.register(modBus);
		PureOreLoaderTypes.register(modBus);
		ECIngredientTypes.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::enqueueIMC);
		NeoForge.EVENT_BUS.addListener(PURE_ORE_MANAGER::reload);
	}

	public static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
		return ResourceKey.createRegistryKey(ElementalCraftApi.createRL(name));
	}

	public static <T> boolean owns(Map.Entry<ResourceKey<T>, T> entry) {
		return owns(entry.getKey());
	}

	public static boolean owns(ResourceKey<?> key) {
		return owns(key.location());
	}

	public static boolean owns(ResourceLocation location) {
		return ElementalCraftApi.MODID.equals(location.getNamespace());
	}

    private void setup(FMLCommonSetupEvent event) {
		PipeUpgradeTypes.setup();
	}
	
	private void enqueueIMC(InterModEnqueueEvent event) {
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_UPGRADE_MANAGER).withCodec(ShrineUpgrade.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_PROPERTIES_MANAGER).withCodec(ShrineProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SPELL_PROPERTIES_MANAGER).withCodec(SpellProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(PURE_ORE_LOADERS_MANAGER).withCodec(IPureOreLoader.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.RUNE_MANAGER).withCodec(Rune.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.TOOL_INFUSION_MANAGER).withCodec(ToolInfusion.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.SOURCE_TRAIT_MANAGER).withCodec(SourceTrait.CODEC));
	}
}
