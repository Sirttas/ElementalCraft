package sirttas.elementalcraft;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.imc.DataManagerIMC;
import sirttas.elementalcraft.advancements.ECCriteriaTriggers;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderTypes;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.infusion.tool.effect.ToolInfusionEffectTypes;
import sirttas.elementalcraft.interaction.ECInteractions;
import sirttas.elementalcraft.item.ECCreativeModeTabs;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.loot.ECLootModifiers;
import sirttas.elementalcraft.loot.entry.ECLootPoolEntries;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.particle.ECParticles;
import sirttas.elementalcraft.pureore.factory.PureOreRecipeFactoryTypes;
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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Mod(ElementalCraftApi.MODID)
public class ElementalCraft {

	public static final ResourceKey<IDataManager<SpellProperties>> SPELL_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SPELL_PROPERTIES));
	public static final IDataManager<SpellProperties> SPELL_PROPERTIES_MANAGER = IDataManager.builder(SpellProperties.class, SPELL_PROPERTIES_MANAGER_KEY)
			.withDefault(SpellProperties.NONE)
			.build();

	public static final ResourceKey<IDataManager<IPureOreLoader>> PURE_ORE_LOADERS_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.PURE_ORE_LOADER));
	public static final IDataManager<IPureOreLoader> PURE_ORE_LOADERS_MANAGER = IDataManager.builder(IPureOreLoader.class, PURE_ORE_LOADERS_MANAGER_KEY)
			.build();

	public static final ResourceKey<IDataManager<IConfigurableBlockEntityProperties>> CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL("configurable_block_entity_properties"));
	public static final IDataManager<IConfigurableBlockEntityProperties> CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER = IDataManager.builder(IConfigurableBlockEntityProperties.class, CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER_KEY)
			.build();

	public ElementalCraft(IEventBus modBus, ModContainer container) {
		container.registerConfig(ModConfig.Type.SERVER, ECConfig.SERVER_SPEC);
		container.registerConfig(ModConfig.Type.CLIENT, ECConfig.CLIENT_SPEC);

		ECBlocks.register(modBus);
		ECBlockEntityTypes.register(modBus);
		ECItems.register(modBus);
		ECEntities.register(modBus);
		ECDataComponents.register(modBus);
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
		ECCriteriaTriggers.register(modBus);
		ConfigurableBlockEntityPropertiesType.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::enqueueIMC);

		tryRegisterTestFramework(modBus, container);
	}

	private void tryRegisterTestFramework(IEventBus modBus, ModContainer container) {
		if (!ECInteractions.isTestFrameworkActive()) {
			return;
		}
		try {
			Class.forName("sirttas.elementalcraft.ElementalCraftTests")
					.getMethod("registerTestFramework", IEventBus.class, ModContainer.class)
					.invoke(null, modBus, container);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			ElementalCraftApi.LOGGER.error("The test Framework is present but the tests sourceset was not found.", e);
		}
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
		Jewels.setup();
	}
	
	private void enqueueIMC(InterModEnqueueEvent event) {
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.RANGE_MANAGER).withCodec(Range.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.RUNE_MANAGER).withCodec(Rune.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.SHRINE_UPGRADE_MANAGER).withCodec(ShrineUpgrade.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.TOOL_INFUSION_MANAGER).withCodec(ToolInfusion.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.SOURCE_TRAIT_MANAGER).withCodec(SourceTrait.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SPELL_PROPERTIES_MANAGER).withCodec(SpellProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(PURE_ORE_LOADERS_MANAGER).withCodec(IPureOreLoader.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER).withCodec(IConfigurableBlockEntityProperties.CODEC));
	}
}
