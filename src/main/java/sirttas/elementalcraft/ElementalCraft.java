package sirttas.elementalcraft;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.imc.DataManagerIMC;
import sirttas.elementalcraft.advancements.ECCriteriaTriggers;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
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
import sirttas.elementalcraft.gameevent.ECGameEvents;
import sirttas.elementalcraft.infusion.tool.effect.ToolInfusionEffectTypes;
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
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.display.ECRecipeDisplayTypes;
import sirttas.elementalcraft.recipe.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.sound.ECSounds;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.world.feature.ECFeatures;
import sirttas.elementalcraft.world.feature.placement.ECPlacements;
import sirttas.elementalcraft.world.feature.structure.ECStructureTypes;

import java.util.Map;

@Mod(ElementalCraftApi.MODID)
public class ElementalCraft {

	public static final ResourceKey<@NotNull IDataManager<SpellProperties>> SPELL_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.SPELL_PROPERTIES));
	public static final IDataManager<SpellProperties> SPELL_PROPERTIES_MANAGER = IDataManager.builder(SpellProperties.class, SPELL_PROPERTIES_MANAGER_KEY)
			.withDefault(SpellProperties.NONE)
			.build();

	public static final ResourceKey<@NotNull IDataManager<IPureOreLoader>> PURE_ORE_LOADERS_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL(ECNames.PURE_ORE_LOADER));
	public static final IDataManager<IPureOreLoader> PURE_ORE_LOADERS_MANAGER = IDataManager.builder(IPureOreLoader.class, PURE_ORE_LOADERS_MANAGER_KEY)
			.build();

	public static final ResourceKey<@NotNull IDataManager<IConfigurableBlockEntityProperties>> CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(ElementalCraftApi.createRL("configurable_block_entity_properties"));
	public static final IDataManager<IConfigurableBlockEntityProperties> CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER = IDataManager.builder(IConfigurableBlockEntityProperties.class, CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER_KEY)
			.build();

    private static final ElementalCraftInteractionWrapper INTERACTIONS = new ElementalCraftInteractionWrapper();

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
        ECRecipeBookCategories.register(modBus);
        ECRecipeDisplayTypes.register(modBus);
        ECIngredientTypes.register(modBus);
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
        ECGameEvents.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::enqueueIMC);
		modBus.addListener(this::processIMC);

        interactions().registerTestFramework(modBus, container);
	}

    public static synchronized sirttas.elementalcraft.api.ElementalCraftInteraction interactions() {
        return INTERACTIONS;
    }

	public static <T> ResourceKey<@NotNull Registry<@NotNull T>> createRegistryKey(String name) {
		return ResourceKey.createRegistryKey(ElementalCraftApi.createRL(name));
	}

	public static <T> boolean owns(Map.Entry<ResourceKey<@NotNull T>, T> entry) {
		return owns(entry.getKey());
	}

	public static boolean owns(ResourceKey<?> key) {
		return owns(key.identifier());
	}

	public static boolean owns(Identifier location) {
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
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.BUD_TYPE_MANAGER).withCodec(BuddingShrineBudType.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SPELL_PROPERTIES_MANAGER).withCodec(SpellProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(PURE_ORE_LOADERS_MANAGER).withCodec(IPureOreLoader.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER).withCodec(IConfigurableBlockEntityProperties.CODEC));
	}

    private void processIMC(InterModProcessEvent event) {
        event.getIMCStream(sirttas.elementalcraft.api.ElementalCraftInteraction.IMC_METHOD::equals).forEach(message -> INTERACTIONS.addInteractionFromIMC(message.messageSupplier()));
    }
}
