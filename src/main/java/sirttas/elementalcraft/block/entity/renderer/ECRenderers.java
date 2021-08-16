package sirttas.elementalcraft.block.entity.renderer;

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
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.diffuser.DiffuserBlockEntity;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.evaporator.EvaporatorBlockEntity;
import sirttas.elementalcraft.block.extractor.ExtractorBlockEntity;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.binder.BinderRenderer;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlockEntity;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerRenderer;
import sirttas.elementalcraft.block.instrument.firefurnace.FireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.firefurnace.FireFurnaceRenderer;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.FireBlastFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberRenderer;
import sirttas.elementalcraft.block.instrument.mill.AirMillBlockEntity;
import sirttas.elementalcraft.block.instrument.mill.AirMillRenderer;
import sirttas.elementalcraft.block.instrument.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.block.instrument.purifier.PurifierRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserRenderer;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import sirttas.elementalcraft.block.shrine.RendererShrine;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlockEntity;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerRenderer;
import sirttas.elementalcraft.block.sorter.SorterBlockEntity;
import sirttas.elementalcraft.block.sorter.SorterRenderer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	private static final Function<TileEntityRendererDispatcher, SingleItemRenderer<PedestalBlockEntity>> PEDESTAL_RENDERER_FACTORY = d -> new SingleItemRenderer<>(d, new Vector3d(0.5, 0.9, 0.5));

	private ECRenderers() {}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {
		ClientRegistry.bindTileEntityRenderer(ElementPipeBlockEntity.TYPE, ElementPipeRenderer::new);
		ClientRegistry.bindTileEntityRenderer(InfuserBlockEntity.TYPE, d -> new SingleItemRenderer<>(d, new Vector3d(0.5, 0.2, 0.5)));
		ClientRegistry.bindTileEntityRenderer(ExtractorBlockEntity.TYPE, RuneRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EvaporatorBlockEntity.TYPE, d -> new SingleItemRenderer<>(d, new Vector3d(0.5, 0.2, 0.5), 0.5F));
		ClientRegistry.bindTileEntityRenderer(SolarSynthesizerBlockEntity.TYPE, SolarSynthesizerRenderer::new);
		ClientRegistry.bindTileEntityRenderer(DiffuserBlockEntity.TYPE, DiffuserRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BinderBlockEntity.TYPE, BinderRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ImprovedBinderBlockEntity.TYPE, BinderRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CrystallizerBlockEntity.TYPE, CrystallizerRenderer::new);
		ClientRegistry.bindTileEntityRenderer(InscriberBlockEntity.TYPE, InscriberRenderer::new);
		ClientRegistry.bindTileEntityRenderer(AirMillBlockEntity.TYPE, AirMillRenderer::new);
		ClientRegistry.bindTileEntityRenderer(PedestalBlockEntity.TYPE_FIRE, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(PedestalBlockEntity.TYPE_WATER, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(PedestalBlockEntity.TYPE_EARTH, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(PedestalBlockEntity.TYPE_AIR, PEDESTAL_RENDERER_FACTORY);
		ClientRegistry.bindTileEntityRenderer(PureInfuserBlockEntity.TYPE, PureInfuserRenderer::new);
		ClientRegistry.bindTileEntityRenderer(FireFurnaceBlockEntity.TYPE, FireFurnaceRenderer::new);
		ClientRegistry.bindTileEntityRenderer(FireBlastFurnaceBlockEntity.TYPE, FireFurnaceRenderer::new);
		ClientRegistry.bindTileEntityRenderer(PurifierBlockEntity.TYPE, PurifierRenderer::new);
		ClientRegistry.bindTileEntityRenderer(AccelerationShrineUpgradeBlockEntity.TYPE, AccelerationShrineUpgradeRenderer::new);
		ClientRegistry.bindTileEntityRenderer(SorterBlockEntity.TYPE, SorterRenderer::new);

		ClientRegistry.bindTileEntityRenderer(FirePylonBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(VacuumShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(GrowthShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(HarvestShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(LavaShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(OreShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(OverloadShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(SweetShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(EnderLockShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(BreedingShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(GroveShrineBlockEntity.TYPE, RendererShrine::new);
		ClientRegistry.bindTileEntityRenderer(SpringShrineBlockEntity.TYPE, RendererShrine::new);
	}

	public static void initRenderLayouts() {
		RenderTypeLookup.setRenderLayer(ECBlocks.TANK_SMALL, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.TANK, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.FIRE_RESERVOIR, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.EARTH_RESERVOIR, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.WATER_RESERVOIR, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.AIR_RESERVOIR, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.TANK_CREATIVE, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.EVAPORATOR, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.FIRE_BLAST_FURNACE, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.BURNT_GLASS, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.BURNT_GLASS_PANE, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.SOURCE, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.CAPACITY_SHRINE_UPGRADE, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE, RenderType.translucent());
	}
}
