package sirttas.elementalcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.imc.DataManagerIMC;
import sirttas.dpanvil.api.imc.DataTagIMC;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.pureore.PureOreManager;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.network.proxy.ClientProxy;
import sirttas.elementalcraft.network.proxy.IProxy;
import sirttas.elementalcraft.network.proxy.ServerProxy;
import sirttas.elementalcraft.rune.Rune;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.spell.SpellTickManager;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.tag.ECTags;
import sirttas.elementalcraft.world.feature.ECFeatures;

@Mod(ElementalCraft.MODID)
public class ElementalCraft {
	public static final String MODID = ECNames.ELEMENTALCRAFT;
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final IProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	public static final PureOreManager PURE_ORE_MANAGER = new PureOreManager();
	public static final IDataManager<ShrineUpgrade> SHREINE_UPGRADE_MANAGER = IDataManager.builder(ShrineUpgrade.class, ShrineUpgrades.FOLDER).withIdSetter(ShrineUpgrade::setId).merged(ShrineUpgrade::merge).build();
	public static final IDataManager<SpellProperties> SPELL_PROPERTIES_MANAGER = IDataManager.builder(SpellProperties.class, SpellProperties.FOLDER).withDefault(SpellProperties.NONE).build();
	public static final IDataManager<Rune> RUNE_MANAGER = IDataManager.builder(Rune.class, Runes.FOLDER).withIdSetter(Rune::setId).merged(Rune::merge).build();

	public ElementalCraft() {
		PROXY.registerHandlers();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		MinecraftForge.EVENT_BUS.addListener(SpellTickManager::serverTick);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ECFeatures::onBiomeLoad);
		MinecraftForge.EVENT_BUS.addListener(PURE_ORE_MANAGER::reload);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ECConfig.CLIENT_SPEC);
	}

	public static ResourceLocation createRL(String name) {
		if (name.contains(":")) {
			return new ResourceLocation(name);
		}
		return new ResourceLocation(MODID, name);
	}

	private void setup(FMLCommonSetupEvent event) {
		MessageHandler.setup();
		ECLootFunctions.setup();
		CapabilityRuneHandler.register();
		CapabilityElementStorage.register();
	}


	private void enqueueIMC(InterModEnqueueEvent event) {
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(createRL(ShrineUpgrades.NAME), SHREINE_UPGRADE_MANAGER).withCodec(ShrineUpgrade.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(createRL(SpellProperties.NAME), SPELL_PROPERTIES_MANAGER).withCodec(SpellProperties.CODEC));
		DataManagerIMC.enqueue(() -> new DataManagerIMC<>(createRL(Runes.NAME), RUNE_MANAGER).withCodec(Rune.CODEC));
		DataTagIMC.enqueue(() -> new DataTagIMC<>(RUNE_MANAGER, ECTags.Runes.RUNE_TAGS));
	}
}

