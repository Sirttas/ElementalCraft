package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
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
import sirttas.elementalcraft.block.shrine.ShrineRenderer;
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
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlockEntity;
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

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	private ECRenderers() {}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {
		register(ElementPipeBlockEntity.TYPE, ElementPipeRenderer::new);
		register(InfuserBlockEntity.TYPE, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5)));
		register(ExtractorBlockEntity.TYPE, RuneRenderer::create);
		register(EvaporatorBlockEntity.TYPE, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5), 0.5F));
		register(SolarSynthesizerBlockEntity.TYPE, SolarSynthesizerRenderer::new);
		register(DiffuserBlockEntity.TYPE, DiffuserRenderer::new);
		register(BinderBlockEntity.TYPE, BinderRenderer::new);
		register(ImprovedBinderBlockEntity.TYPE, BinderRenderer::new);
		register(CrystallizerBlockEntity.TYPE, CrystallizerRenderer::new);
		register(InscriberBlockEntity.TYPE, InscriberRenderer::new);
		register(AirMillBlockEntity.TYPE, AirMillRenderer::new);
		register(PedestalBlockEntity.TYPE, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.9, 0.5)));
		register(PureInfuserBlockEntity.TYPE, PureInfuserRenderer::new);
		register(FireFurnaceBlockEntity.TYPE, FireFurnaceRenderer::new);
		register(FireBlastFurnaceBlockEntity.TYPE, FireFurnaceRenderer::new);
		register(PurifierBlockEntity.TYPE, PurifierRenderer::new);
		register(AccelerationShrineUpgradeBlockEntity.TYPE, AccelerationShrineUpgradeRenderer::new);
		register(SorterBlockEntity.TYPE, SorterRenderer::new);
		register(SourceBlockEntity.TYPE, SourceRenderer::new);

		register(FirePylonBlockEntity.TYPE, ShrineRenderer::new);
		register(VacuumShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(GrowthShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(HarvestShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(LavaShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(OreShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(OverloadShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(SweetShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(EnderLockShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(BreedingShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(GroveShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(BuddingShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(SpringShrineBlockEntity.TYPE, ShrineRenderer::new);
		register(SpawningShrineBlockEntity.TYPE, ShrineRenderer::new);
	}

	public static void initRenderLayouts() {
		setRenderLayer(ECBlocks.SMALL_CONTAINER, RenderType.cutout());
		setRenderLayer(ECBlocks.CONTAINER, RenderType.cutout());
		setRenderLayer(ECBlocks.FIRE_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.EARTH_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.WATER_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.AIR_RESERVOIR, RenderType.cutout());
		setRenderLayer(ECBlocks.CREATIVE_CONTAINER, RenderType.cutout());
		setRenderLayer(ECBlocks.EVAPORATOR, RenderType.cutout());
		setRenderLayer(ECBlocks.SMALL_SPRINGALINE_BUD, RenderType.cutout());
		setRenderLayer(ECBlocks.MEDIUM_SPRINGALINE_BUD, RenderType.cutout());
		setRenderLayer(ECBlocks.LARGE_SPRINGALINE_BUD, RenderType.cutout());
		setRenderLayer(ECBlocks.SPRINGALINE_CLUSTER, RenderType.cutout());
		setRenderLayer(ECBlocks.SPAWNING_SHRINE, RenderType.cutout());
		
		setRenderLayer(ECBlocks.FIRE_BLAST_FURNACE, RenderType.translucent());
		setRenderLayer(ECBlocks.BURNT_GLASS, RenderType.translucent());
		setRenderLayer(ECBlocks.BURNT_GLASS_PANE, RenderType.translucent());
		setRenderLayer(ECBlocks.SPRINGALINE_GLASS, RenderType.translucent());
		setRenderLayer(ECBlocks.SPRINGALINE_GLASS_PANE, RenderType.translucent());
		setRenderLayer(ECBlocks.SOURCE, RenderType.translucent());
		setRenderLayer(ECBlocks.CAPACITY_SHRINE_UPGRADE, RenderType.translucent());
		setRenderLayer(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE, RenderType.translucent());
	}

	public static <T extends BlockEntity> void register(RegistryObject<BlockEntityType<T>> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		register(type.get(), renderProvider);
	}

	public static <T extends BlockEntity> void register(BlockEntityType<T> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		BlockEntityRenderers.register(type, d -> renderProvider.get());
	}

	public static void setRenderLayer(RegistryObject<Block> block, RenderType type) {
		setRenderLayer(block.get(), type);
	}

	public static void setRenderLayer(Block block, RenderType type) {
		ItemBlockRenderTypes.setRenderLayer(block, type);
	}
}
