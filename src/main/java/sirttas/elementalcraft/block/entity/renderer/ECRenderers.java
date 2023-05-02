package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ContainerRenderer;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.binder.BinderRenderer;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerRenderer;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberRenderer;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceRenderer;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneRenderer;
import sirttas.elementalcraft.block.instrument.io.mill.saw.WaterMillWoodSawRenderer;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserRenderer;
import sirttas.elementalcraft.block.shrine.ShrineRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeRenderer;
import sirttas.elementalcraft.block.sorter.SorterRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.block.source.breeder.SourceBreederRenderer;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalRenderer;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateRenderer;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	private ECRenderers() {}
	
	@SubscribeEvent
	public static void registerModels(RegisterGeometryLoaders evt) {
		register(ECBlockEntityTypes.PIPE, ElementPipeRenderer::new);
		register(ECBlockEntityTypes.INFUSER, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5)));
		register(ECBlockEntityTypes.EXTRACTOR, IRuneRenderer::create);
		register(ECBlockEntityTypes.EVAPORATOR, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.2, 0.5), 0.5F));
		register(ECBlockEntityTypes.SOLAR_SYNTHESIZER, SolarSynthesizerRenderer::new);
		register(ECBlockEntityTypes.MANA_SYNTHESIZER, ManaSynthesizerRenderer::new);
		register(ECBlockEntityTypes.DIFFUSER, DiffuserRenderer::new);
		register(ECBlockEntityTypes.BINDER, BinderRenderer::new);
		register(ECBlockEntityTypes.BINDER_IMPROVED, BinderRenderer::new);
		register(ECBlockEntityTypes.CRYSTALLIZER, CrystallizerRenderer::new);
		register(ECBlockEntityTypes.INSCRIBER, InscriberRenderer::new);
		register(ECBlockEntityTypes.AIR_MILL_GRINDSTONE, AirMillGrindstoneRenderer::new);
		register(ECBlockEntityTypes.WATER_MILL_WOOD_SAW, WaterMillWoodSawRenderer::new);
		register(ECBlockEntityTypes.PEDESTAL, () -> new SingleItemRenderer<>(new Vec3(0.5, 0.9, 0.5)));
		register(ECBlockEntityTypes.PURE_INFUSER, PureInfuserRenderer::new);
		register(ECBlockEntityTypes.FIRE_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.FIRE_BLAST_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.PURIFIER, PurifierRenderer::new);
		register(ECBlockEntityTypes.ACCELERATION_SHRINE_UPGRADE, AccelerationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.VORTEX_SHRINE_UPGRADE, VortexShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.TRANSLOCATION_SHRINE_UPGRADE, TranslocationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE, OverclockedAccelerationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.SORTER, SorterRenderer::new);
		register(ECBlockEntityTypes.SOURCE, SourceRenderer::new);

		register(ECBlockEntityTypes.FIRE_PYLON, ShrineRenderer::new);
		register(ECBlockEntityTypes.GROVE_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.BUDDING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.BREEDING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SPAWNING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.LAVA_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.ORE_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.OVERLOAD_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SWEET_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.GROWTH_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.HARVEST_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.LUMBER_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.ENDER_LOCK_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.SPRING_SHRINE, ShrineRenderer::new);
		register(ECBlockEntityTypes.VACUUM_SHRINE, ShrineRenderer::new);

		register(ECBlockEntityTypes.CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.CREATIVE_CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.RESERVOIR, ContainerRenderer::new);

		register(ECBlockEntityTypes.SOURCE_DISPLACEMENT_PLATE, SourceDisplacementPlateRenderer::new);
		register(ECBlockEntityTypes.SOURCE_BREEDER, SourceBreederRenderer::new);
		register(ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL, SourceBreederPedestalRenderer::new);
	}

	public static <T extends BlockEntity> void register(RegistryObject<BlockEntityType<T>> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		register(type.get(), renderProvider);
	}

	public static <T extends BlockEntity> void register(RegistryObject<BlockEntityType<T>> type, BlockEntityRendererProvider<T> renderProvider) {
		BlockEntityRenderers.register(type.get(), renderProvider);
	}

	public static <T extends BlockEntity> void register(BlockEntityType<T> type, Supplier<BlockEntityRenderer<? super T>> renderProvider) {
		BlockEntityRenderers.register(type, d -> renderProvider.get());
	}
}
