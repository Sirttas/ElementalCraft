package sirttas.elementalcraft.block.tile.renderer;

import java.util.function.Function;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.evaporator.TileEvaporator;
import sirttas.elementalcraft.block.extractor.TileExtractor;
import sirttas.elementalcraft.block.instrument.binder.RendererBinder;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.block.instrument.binder.improved.TileImprovedBinder;
import sirttas.elementalcraft.block.instrument.crystallizer.RendererCrystallizer;
import sirttas.elementalcraft.block.instrument.crystallizer.TileCrystallizer;
import sirttas.elementalcraft.block.instrument.firefurnace.RendererFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.TileFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.TileFireBlastFurnace;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.block.instrument.inscriber.RendererInscriber;
import sirttas.elementalcraft.block.instrument.inscriber.TileInscriber;
import sirttas.elementalcraft.block.instrument.purifier.RendererPurifier;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.block.pureinfuser.RendererPureInfuser;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;
import sirttas.elementalcraft.block.pureinfuser.pedestal.TilePedestal;
import sirttas.elementalcraft.block.shrine.RendererShrine;
import sirttas.elementalcraft.block.shrine.breeding.TileBreedingShrine;
import sirttas.elementalcraft.block.shrine.enderlock.TileEnderLockShrine;
import sirttas.elementalcraft.block.shrine.firepylon.TileFirePylon;
import sirttas.elementalcraft.block.shrine.growth.TileGrowthShrine;
import sirttas.elementalcraft.block.shrine.harvest.TileHarvestShrine;
import sirttas.elementalcraft.block.shrine.lava.TileLavaShrine;
import sirttas.elementalcraft.block.shrine.ore.TileOreShrine;
import sirttas.elementalcraft.block.shrine.overload.TileOverloadShrine;
import sirttas.elementalcraft.block.shrine.sweet.TileSweetShrine;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.RedererAccelerationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.TileAccelerationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.vacuum.TileVacuumShrine;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	private static final Function<TileEntityRendererDispatcher, SingleItemRenderer<TilePedestal>> PEDESTAL_RENDERER_FACTORY = d -> new SingleItemRenderer<>(d, new Vector3d(0.5, 0.9, 0.5));

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {
		ClientRegistry.bindTileEntityRenderer(TileInfuser.TYPE, d -> new SingleItemRenderer<>(d, new Vector3d(0.5, 0.2, 0.5)));
		ClientRegistry.bindTileEntityRenderer(TileExtractor.TYPE, RuneRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEvaporator.TYPE, d -> new SingleItemRenderer<>(d, new Vector3d(0.5, 0.2, 0.5), 0.5F));
		ClientRegistry.bindTileEntityRenderer(TileBinder.TYPE, RendererBinder::new);
		ClientRegistry.bindTileEntityRenderer(TileImprovedBinder.TYPE, RendererBinder::new);
		ClientRegistry.bindTileEntityRenderer(TileCrystallizer.TYPE, RendererCrystallizer::new);
		ClientRegistry.bindTileEntityRenderer(TileInscriber.TYPE, RendererInscriber::new);
		ClientRegistry.bindTileEntityRenderer(TilePedestal.TYPE_FIRE, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(TilePedestal.TYPE_WATER, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(TilePedestal.TYPE_EARTH, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(TilePedestal.TYPE_AIR, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(TilePureInfuser.TYPE, RendererPureInfuser::new);
		ClientRegistry.bindTileEntityRenderer(TileFireFurnace.TYPE, RendererFireFurnace::new);
		ClientRegistry.bindTileEntityRenderer(TileFireBlastFurnace.TYPE, RendererFireFurnace::new);
		ClientRegistry.bindTileEntityRenderer(TilePurifier.TYPE, RendererPurifier::new);
		ClientRegistry.bindTileEntityRenderer(TileAccelerationShrineUpgrade.TYPE, RedererAccelerationShrineUpgrade::new);

		ClientRegistry.bindTileEntityRenderer(TileFirePylon.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileVacuumShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileGrowthShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileHarvestShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileLavaShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileOreShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileOverloadShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileSweetShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileEnderLockShrine.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(TileBreedingShrine.TYPE, RendererShrine::new);
	}

	public static void initRenderLayouts() {
		RenderTypeLookup.setRenderLayer(ECBlocks.tankSmall, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.tank, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.tankCreative, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.evaporator, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.fireBlastFurnace, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.burntGlass, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.burntGlassPane, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.source, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.capacityShrineUpgrade, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.optimizationShrineUpgrade, RenderType.getTranslucent());
	}
}
