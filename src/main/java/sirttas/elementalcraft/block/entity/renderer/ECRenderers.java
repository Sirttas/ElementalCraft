package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.ContainerRenderer;
import sirttas.elementalcraft.block.diffuser.DiffuserRenderer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.binder.BinderRenderer;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerRenderer;
import sirttas.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierRenderer;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberRenderer;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceRenderer;
import sirttas.elementalcraft.block.instrument.io.mill.MillRenderer;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierRenderer;
import sirttas.elementalcraft.block.pipe.ElementPipeRenderer;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserRenderer;
import sirttas.elementalcraft.block.shrine.ShrineRenderer;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.AccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked.OverclockedAccelerationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeRenderer;
import sirttas.elementalcraft.block.shrine.upgrade.vortex.VortexShrineUpgradeRenderer;
import sirttas.elementalcraft.block.sorter.ordered.OrderedSorterRenderer;
import sirttas.elementalcraft.block.source.SourceRenderer;
import sirttas.elementalcraft.block.source.breeder.SourceBreederRenderer;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalRenderer;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerRenderer;

import java.util.function.Supplier;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, value = Dist.CLIENT)
public final class ECRenderers {

	private ECRenderers() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		register(ECBlockEntityTypes.PIPE, ElementPipeRenderer::new);
		register(ECBlockEntityTypes.INFUSER, context -> SingleItemBlockEntityRenderer.create(context, new Vec3(0.5, 0.2, 0.5)));
		register(ECBlockEntityTypes.EXTRACTOR, RuneBlockEntityRenderer::create);
		register(ECBlockEntityTypes.CRACKING_SYNTHESIZER, CrackingSynthesizerRenderer::new);
		register(ECBlockEntityTypes.COMBUSTION_SYNTHESIZER, context -> SingleItemBlockEntityRenderer.create(context, new Vec3(0.5, 0.5, 0.5), 0.7F));
		register(ECBlockEntityTypes.DRAINING_SYNTHESIZER, RuneBlockEntityRenderer::create);
		register(ECBlockEntityTypes.VIBRATION_SYNTHESIZER, VibrationSynthesizerRenderer::new);
		register(ECBlockEntityTypes.SOLAR_SYNTHESIZER, SolarSynthesizerRenderer::new);
		register(ECBlockEntityTypes.SCULK_CRACKING_SYNTHESIZER, CrackingSynthesizerRenderer::new);
		register(ECBlockEntityTypes.AIR_MILL_SYNTHESIZER, AirMillSynthesizerRenderer::new);
		register(ECBlockEntityTypes.DIFFUSER, DiffuserRenderer::new);
		register(ECBlockEntityTypes.BINDER, BinderRenderer::new);
		register(ECBlockEntityTypes.BINDER_IMPROVED, BinderRenderer::new);
		register(ECBlockEntityTypes.CRYSTALLIZER, CrystallizerRenderer::new);
		register(ECBlockEntityTypes.INSCRIBER, InscriberRenderer::new);
		register(ECBlockEntityTypes.WATER_MILL_GRINDSTONE, context -> new MillRenderer<>(context, MillRenderer.WATER_MILL_GRINDSTONE_SHAFT_LOCATION));
		register(ECBlockEntityTypes.AIR_MILL_GRINDSTONE, context -> new MillRenderer<>(context, MillRenderer.AIR_MILL_GRINDSTONE_SHAFT_LOCATION));
		register(ECBlockEntityTypes.WATER_MILL_WOOD_SAW, context -> new MillRenderer<>(context, MillRenderer.WATER_MILL_WOOD_SAW_SHAFT_LOCATION));
		register(ECBlockEntityTypes.AIR_MILL_WOOD_SAW, context -> new MillRenderer<>(context, MillRenderer.AIR_MILL_WOOD_SAW_SHAFT_LOCATION));
		register(ECBlockEntityTypes.ENCHANTMENT_LIQUEFIER, EnchantmentLiquefierRenderer::new);
		register(ECBlockEntityTypes.PEDESTAL, context -> SingleItemBlockEntityRenderer.create(context, new Vec3(0.5, 0.9, 0.5)));
		register(ECBlockEntityTypes.PURE_INFUSER, PureInfuserRenderer::new);
		register(ECBlockEntityTypes.FIRE_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.FIRE_BLAST_FURNACE, FireFurnaceRenderer::new);
		register(ECBlockEntityTypes.PURIFIER, PurifierRenderer::new);
		register(ECBlockEntityTypes.ACCELERATION_SHRINE_UPGRADE, AccelerationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.VORTEX_SHRINE_UPGRADE, VortexShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.TRANSLOCATION_SHRINE_UPGRADE, TranslocationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE, OverclockedAccelerationShrineUpgradeRenderer::new);
		register(ECBlockEntityTypes.GREATER_FORTUNE_SHRINE_UPGRADE, RuneBlockEntityRenderer::create);
		register(ECBlockEntityTypes.SORTER, OrderedSorterRenderer::new);
		register(ECBlockEntityTypes.SOURCE, SourceRenderer::new);

		register(ECBlockEntityTypes.FIRE_PYLON, ShrineRenderer::create);
		register(ECBlockEntityTypes.GROVE_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.BUDDING_SHRINE, BuddingShrineRenderer::new);
		register(ECBlockEntityTypes.BREEDING_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.SPAWNING_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.LAVA_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.ORE_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.OVERLOAD_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.SWEET_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.GROWTH_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.HARVEST_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.LUMBER_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.ENDER_LOCK_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.SPRING_SHRINE, ShrineRenderer::create);
		register(ECBlockEntityTypes.VACUUM_SHRINE, ShrineRenderer::create);

		register(ECBlockEntityTypes.CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.CREATIVE_CONTAINER, ContainerRenderer::new);
		register(ECBlockEntityTypes.RESERVOIR, ContainerRenderer::new);

		register(ECBlockEntityTypes.SOURCE_BREEDER, SourceBreederRenderer::new);
		register(ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL, SourceBreederPedestalRenderer::new);
	}

	public static <T extends BlockEntity, S extends BlockEntityRenderState> void register(DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull T>> type, Supplier<BlockEntityRenderer<@NotNull T, @NotNull S>> renderProvider) {
		register(type, d -> renderProvider.get());
	}

	public static <T extends BlockEntity, S extends BlockEntityRenderState> void register(DeferredHolder<@NotNull BlockEntityType<?>, @NotNull BlockEntityType<@NotNull T>> type, BlockEntityRendererProvider<@NotNull T, @NotNull S> renderProvider) {
		BlockEntityRenderers.register(type.get(), renderProvider);
	}
}
