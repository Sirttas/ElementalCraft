package sirttas.elementalcraft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.world.ECFeatures;
import sirttas.elementalcraft.world.dimension.boss.BossDimension;

@Mod(ElementalCraft.MODID)
public class ElementalCraft {
	public static final String MODID = "elementalcraft";
	public static final String VERSION = "1.0.0";

	public static final Logger T = LogManager.getLogger(MODID);

	public ElementalCraft() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
	}

	private void setup(FMLCommonSetupEvent event) {
		MessageHandler.setup();
		ECFeatures.addToWorldgen();
		BossDimension.getDimensionType();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ECConfig.SPEC);
	}

	@OnlyIn(Dist.CLIENT)
	private void setupClient(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(ECBlocks.tank, RenderType.getCutout());
		
		ECEntities.registerRenderers();
	}
}

