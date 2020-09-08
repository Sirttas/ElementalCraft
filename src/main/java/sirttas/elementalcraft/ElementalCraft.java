package sirttas.elementalcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.pureore.PureOreHelper;
import sirttas.elementalcraft.loot.function.RandomSpell;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.network.proxy.ClientProxy;
import sirttas.elementalcraft.network.proxy.IProxy;
import sirttas.elementalcraft.world.ECFeatures;
import sirttas.elementalcraft.world.dimension.boss.BossDimension;

@Mod(ElementalCraft.MODID)
public class ElementalCraft {
	public static final String MODID = "elementalcraft";

	public static final Logger T = LogManager.getLogger(MODID);

	private IProxy proxy = new IProxy() {};

	public ElementalCraft() {
		DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
		proxy.registerHandlers();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.addListener(this::setupServer);
	}

	private void setup(FMLCommonSetupEvent event) {
		MessageHandler.setup();
		ECFeatures.addToWorldgen();
		BossDimension.getDimensionType();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ECConfig.SPEC);
		LootFunctionManager.registerFunction(new RandomSpell.Serializer());
	}

	private void setupServer(FMLServerStartedEvent event) {
		PureOreHelper.generatePureOres(event.getServer().getRecipeManager());
	}
}

