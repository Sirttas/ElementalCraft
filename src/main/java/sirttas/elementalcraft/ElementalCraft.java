package sirttas.elementalcraft;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.imc.DataManagerIMC;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.pureore.PureOreManager;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.world.feature.ECFeatures;

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

	public ElementalCraft() {
		var modBus = FMLJavaModLoadingContext.get().getModEventBus();

		Spells.register(modBus);
		Jewels.register(modBus);

		modBus.addListener(this::setup);
		modBus.addListener(this::registerCapabilities);
		modBus.addListener(this::enqueueIMC);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ECFeatures::onBiomeLoad);
		MinecraftForge.EVENT_BUS.addListener(PURE_ORE_MANAGER::reload);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ECConfig.CLIENT_SPEC);
	}

	public static ResourceLocation createRL(String name) {
		if (name.contains(":")) {
			return new ResourceLocation(name);
		}
		return new ResourceLocation(ElementalCraftApi.MODID, name);
	}

	private void setup(FMLCommonSetupEvent event) {
		MessageHandler.setup();
		ECLootFunctions.setup();
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IElementStorage.class);
		event.register(IRuneHandler.class);
		event.register(IElementTransferer.class);
	}
	
	private void enqueueIMC(InterModEnqueueEvent event) {
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_UPGRADE_MANAGER_KEY, SHRINE_UPGRADE_MANAGER).withCodec(ShrineUpgrade.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SHRINE_PROPERTIES_MANAGER_KEY, SHRINE_PROPERTIES_MANAGER).withCodec(ShrineProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(SPELL_PROPERTIES_MANAGER_KEY, SPELL_PROPERTIES_MANAGER).withCodec(SpellProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.RUNE_MANAGER_KEY, ElementalCraftApi.RUNE_MANAGER).withCodec(Rune.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.TOOL_INFUSION_MANAGER_KEY, ElementalCraftApi.TOOL_INFUSION_MANAGER).withCodec(ToolInfusion.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(ElementalCraftApi.SOURCE_TRAIT_MANAGER_KEY, ElementalCraftApi.SOURCE_TRAIT_MANAGER).withCodec(SourceTrait.CODEC));
	}
}
