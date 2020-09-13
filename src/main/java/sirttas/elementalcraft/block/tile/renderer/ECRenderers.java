package sirttas.elementalcraft.block.tile.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.binder.RendererBinder;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.block.instrument.firefurnace.RendererFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.TileFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.TileFireBlastFurnace;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.block.instrument.purifier.RendererPurifier;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.block.pureinfuser.TilePedestal;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {

		ClientRegistry.bindTileEntityRenderer(TileInfuser.TYPE, d -> new SingleItemRenderer<TileInfuser>(d, new Vec3d(0.5, 0.2, 0.5)));
		ClientRegistry.bindTileEntityRenderer(TileBinder.TYPE, RendererBinder::new);
		ClientRegistry.bindTileEntityRenderer(TilePedestal.TYPE, d -> new SingleItemRenderer<TilePedestal>(d, new Vec3d(0.5, 0.9, 0.5)));
		ClientRegistry.bindTileEntityRenderer(TilePureInfuser.TYPE, d -> new SingleItemRenderer<TilePureInfuser>(d, new Vec3d(0.5, 0.9, 0.5)));
		ClientRegistry.bindTileEntityRenderer(TileFireFurnace.TYPE, RendererFireFurnace::new);
		ClientRegistry.bindTileEntityRenderer(TileFireBlastFurnace.TYPE, RendererFireFurnace::new);
		ClientRegistry.bindTileEntityRenderer(TilePurifier.TYPE, RendererPurifier::new);
	}

	public static void initRenderLayouts() {
		RenderTypeLookup.setRenderLayer(ECBlocks.tank, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.tankSmall, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ECBlocks.fireBlastFurnace, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.burntGlass, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.burntGlassPane, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ECBlocks.source, RenderType.getTranslucent());
	}
}
