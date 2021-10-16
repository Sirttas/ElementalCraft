package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberRenderer;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceRenderer;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillRenderer;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserRenderer;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import sirttas.elementalcraft.block.shrine.RendererShrine;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlockEntity;
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
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.block.source.SourceRenderer;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	private ECRenderers() {}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {
		BlockEntityRenderers.register(ElementPipeBlockEntity.TYPE, ElementPipeRenderer::new);
		BlockEntityRenderers.register(InfuserBlockEntity.TYPE, c -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5)));
		BlockEntityRenderers.register(ExtractorBlockEntity.TYPE, c -> new RuneRenderer<>() {});
		BlockEntityRenderers.register(EvaporatorBlockEntity.TYPE, c -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5), 0.5F));
		BlockEntityRenderers.register(SolarSynthesizerBlockEntity.TYPE, SolarSynthesizerRenderer::new);
		BlockEntityRenderers.register(DiffuserBlockEntity.TYPE, DiffuserRenderer::new);
		BlockEntityRenderers.register(BinderBlockEntity.TYPE, BinderRenderer::new);
		BlockEntityRenderers.register(ImprovedBinderBlockEntity.TYPE, BinderRenderer::new);
		BlockEntityRenderers.register(CrystallizerBlockEntity.TYPE, CrystallizerRenderer::new);
		BlockEntityRenderers.register(InscriberBlockEntity.TYPE, InscriberRenderer::new);
		BlockEntityRenderers.register(AirMillBlockEntity.TYPE, AirMillRenderer::new);
		BlockEntityRenderers.register(PedestalBlockEntity.TYPE, c -> new SingleItemRenderer<>(new Vec3(0.5, 0.9, 0.5)));
		BlockEntityRenderers.register(PureInfuserBlockEntity.TYPE, PureInfuserRenderer::new);
		BlockEntityRenderers.register(FireFurnaceBlockEntity.TYPE, FireFurnaceRenderer::new);
		BlockEntityRenderers.register(FireBlastFurnaceBlockEntity.TYPE, FireFurnaceRenderer::new);
		BlockEntityRenderers.register(PurifierBlockEntity.TYPE, PurifierRenderer::new);
		BlockEntityRenderers.register(AccelerationShrineUpgradeBlockEntity.TYPE, AccelerationShrineUpgradeRenderer::new);
		BlockEntityRenderers.register(SorterBlockEntity.TYPE, SorterRenderer::new);
		BlockEntityRenderers.register(SourceBlockEntity.TYPE, SourceRenderer::new);

		BlockEntityRenderers.register(FirePylonBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(VacuumShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(GrowthShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(HarvestShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(LavaShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(OreShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(OverloadShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(SweetShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(EnderLockShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(BreedingShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(GroveShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(BuddingShrineBlockEntity.TYPE, RendererShrine::new);
		BlockEntityRenderers.register(SpringShrineBlockEntity.TYPE, RendererShrine::new);
	}

	public static void initRenderLayouts() {
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.SMALL_CONTAINER, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.CONTAINER, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.FIRE_RESERVOIR, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.EARTH_RESERVOIR, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.WATER_RESERVOIR, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.AIR_RESERVOIR, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.CREATIVE_CONTAINER, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.EVAPORATOR, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.SMALL_SPRINGALINE_BUD, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.MEDIUM_SPRINGALINE_BUD, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.LARGE_SPRINGALINE_BUD, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.SPRINGALINE_CLUSTER, RenderType.cutout());
		
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.FIRE_BLAST_FURNACE, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.BURNT_GLASS, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.BURNT_GLASS_PANE, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.SPRINGALINE_GLASS, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.SPRINGALINE_GLASS_PANE, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.SOURCE, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.CAPACITY_SHRINE_UPGRADE, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE, RenderType.translucent());
	}
}
