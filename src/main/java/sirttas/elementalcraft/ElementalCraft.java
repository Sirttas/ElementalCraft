package sirttas.elementalcraft;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.imc.DataManagerIMC;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.source.trait.value.SourceTraitValueProviderTypes;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.infusion.tool.effect.ToolInfusionEffectTypes;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.curios.CuriosInteractions;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.loot.ECLootModifiers;
import sirttas.elementalcraft.loot.entry.ECLootPoolEntries;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.particle.ECParticles;
import sirttas.elementalcraft.pureore.PureOreLoader;
import sirttas.elementalcraft.pureore.PureOreManager;
import sirttas.elementalcraft.pureore.injector.PureOreRecipeInjectors;
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

	public static final ResourceKey<IDataManager<ShrineUpgrade>> SHRINE_UPGRADE_MANAGER_KEY = IDataManager.createManagerKey(createRL(ShrineUpgrades.NAME));
	public static final IDataManager<ShrineUpgrade> SHRINE_UPGRADE_MANAGER = IDataManager.builder(ShrineUpgrade.class, ShrineUpgrades.FOLDER)
			.withIdSetter(ShrineUpgrade::setId)
			.merged(ShrineUpgrade::merge)
			.build();

	public static final ResourceKey<IDataManager<SpellProperties>> SPELL_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(createRL(SpellProperties.NAME));
	public static final IDataManager<SpellProperties> SPELL_PROPERTIES_MANAGER = IDataManager.builder(SpellProperties.class, SpellProperties.FOLDER)
			.withDefault(SpellProperties.NONE)
			.build();

	public static final ResourceKey<IDataManager<ShrineProperties>> SHRINE_PROPERTIES_MANAGER_KEY = IDataManager.createManagerKey(createRL(ShrineProperties.NAME));
	public static final IDataManager<ShrineProperties> SHRINE_PROPERTIES_MANAGER = IDataManager.builder(ShrineProperties.class, ShrineProperties.FOLDER)
			.withDefault(ShrineProperties.DEFAULT)
			.build();

	public static final ResourceKey<IDataManager<PureOreLoader>> PURE_ORE_LOADERS_MANAGER_KEY = IDataManager.createManagerKey(createRL(PureOreLoader.NAME));
	public static final IDataManager<PureOreLoader> PURE_ORE_LOADERS_MANAGER = IDataManager.builder(PureOreLoader.class, PureOreLoader.FOLDER)
			.build();

	public ElementalCraft() {
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ECConfig.CLIENT_SPEC);

		ECBlocks.register(modBus);
		ECBlockEntityTypes.register(modBus);
		ECItems.register(modBus);
		ECEntities.register(modBus);
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
		PureOreRecipeInjectors.register(modBus);
		PipeUpgradeTypes.register(modBus);
		ECSounds.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::enqueueIMC);
		MinecraftForge.EVENT_BUS.addListener(PURE_ORE_MANAGER::reload);
	}

	public static ResourceLocation createRL(String name) {
		if (name.contains(":")) {
			return new ResourceLocation(name);
		}
		return new ResourceLocation(ElementalCraftApi.MODID, name);
	}

	public static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
		return ResourceKey.createRegistryKey(createRL(name));
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
		MessageHandler.setup();
		PipeUpgradeTypes.setup();
	}
	
	private void enqueueIMC(InterModEnqueueEvent event) {
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_UPGRADE_MANAGER_KEY, SHRINE_UPGRADE_MANAGER).withCodec(ShrineUpgrade.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_PROPERTIES_MANAGER_KEY, SHRINE_PROPERTIES_MANAGER).withCodec(ShrineProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SPELL_PROPERTIES_MANAGER_KEY, SPELL_PROPERTIES_MANAGER).withCodec(SpellProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(PURE_ORE_LOADERS_MANAGER_KEY, PURE_ORE_LOADERS_MANAGER).withCodec(PureOreLoader.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.RUNE_MANAGER_KEY, ElementalCraftApi.RUNE_MANAGER).withCodec(Rune.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.TOOL_INFUSION_MANAGER_KEY, ElementalCraftApi.TOOL_INFUSION_MANAGER).withCodec(ToolInfusion.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, ElementalCraftApi.SOURCE_TRAIT_MANAGER).withCodec(SourceTrait.CODEC));
		if (ECinteractions.isCuriosActive()) {
			CuriosInteractions.registerSlots();
		}
	}
}
