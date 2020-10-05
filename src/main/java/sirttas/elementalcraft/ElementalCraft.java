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
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.pureore.PureOreHelper;
import sirttas.elementalcraft.loot.function.ECLootFunctions;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.network.proxy.ClientProxy;
import sirttas.elementalcraft.network.proxy.IProxy;
import sirttas.elementalcraft.spell.SpellTickManager;
import sirttas.elementalcraft.world.feature.ECFeatures;

@Mod(ElementalCraft.MODID)
public class ElementalCraft {
	public static final String MODID = "elementalcraft";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	private IProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> () -> new IProxy() {});

	public ElementalCraft() {
		proxy.registerHandlers();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.addListener(this::setupServer);
		MinecraftForge.EVENT_BUS.addListener(SpellTickManager::serverTick);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ECFeatures::onBiomeLoad);
	}

	private void setup(FMLCommonSetupEvent event) {
		MessageHandler.setup();
		ECLootFunctions.setup();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ECConfig.SPEC);
	}

	private void setupServer(FMLServerStartedEvent event) {
		PureOreHelper.generatePureOres(event.getServer().getRecipeManager());
	}

	public static ResourceLocation createRL(String name) {
		return new ResourceLocation(MODID, name);
	}
}

