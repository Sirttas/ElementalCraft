package sirttas.elementalcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.api.pureore.PureOreInjectorIMCMessage;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeManager;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.item.pureore.PureOreManager;
import sirttas.elementalcraft.item.pureore.PureOreRecipeInjector;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.network.proxy.ClientProxy;
import sirttas.elementalcraft.network.proxy.IProxy;
import sirttas.elementalcraft.spell.SpellTickManager;
import sirttas.elementalcraft.spell.properties.SpellPropertiesManager;
import sirttas.elementalcraft.world.feature.ECFeatures;

@Mod(ElementalCraft.MODID)
public class ElementalCraft {
	public static final String MODID = "elementalcraft";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final PureOreManager PURE_ORE_MANAGER = new PureOreManager();
	public static final ShrineUpgradeManager SHRINE_UPGRADE_MANAGER = new ShrineUpgradeManager();
	public static final SpellPropertiesManager SPELL_PROPERTIES_MANAGER = new SpellPropertiesManager();

	private IProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> () -> new IProxy() {});

	public ElementalCraft() {
		proxy.registerHandlers();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		MinecraftForge.EVENT_BUS.addListener(this::setupServer);
		MinecraftForge.EVENT_BUS.addListener(SpellTickManager::serverTick);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ECFeatures::onBiomeLoad);
		MinecraftForge.EVENT_BUS.addListener(this::addReloadListeners);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ECConfig.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ECConfig.CLIENT_SPEC);
	}

	public static ResourceLocation createRL(String name) {
		return new ResourceLocation(MODID, name);
	}

	private void setup(FMLCommonSetupEvent event) {
		MessageHandler.setup();
		ECLootFunctions.setup();
		ECinteractions.setup();
	}

	private void setupServer(FMLServerStartedEvent event) {
		PURE_ORE_MANAGER.generatePureOres(event.getServer().getRecipeManager());
	}

	private void processIMC(InterModProcessEvent event) {
		InterModComms.getMessages(MODID, PureOreInjectorIMCMessage.METHOD::equals).forEach(imc -> PURE_ORE_MANAGER.addInjector(PureOreRecipeInjector.create(imc.getMessageSupplier())));
	}
	private void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(SHRINE_UPGRADE_MANAGER);
		event.addListener(SPELL_PROPERTIES_MANAGER);
	}
}

