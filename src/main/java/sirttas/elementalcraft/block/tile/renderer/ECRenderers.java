package sirttas.elementalcraft.block.tile.renderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.binder.RendererBinder;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.block.instrument.firefurnace.RendererFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.TileFireFurnace;
import sirttas.elementalcraft.block.instrument.infuser.RendererInfuser;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.block.pureinfuser.RendererPedestal;
import sirttas.elementalcraft.block.pureinfuser.RendererPureInfuser;
import sirttas.elementalcraft.block.pureinfuser.TilePedestal;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ECRenderers {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {

		ClientRegistry.bindTileEntityRenderer(TileInfuser.TYPE, RendererInfuser::new);
		ClientRegistry.bindTileEntityRenderer(TileBinder.TYPE, RendererBinder::new);
		ClientRegistry.bindTileEntityRenderer(TilePedestal.TYPE, RendererPedestal::new);
		ClientRegistry.bindTileEntityRenderer(TilePureInfuser.TYPE, RendererPureInfuser::new);
		ClientRegistry.bindTileEntityRenderer(TileFireFurnace.TYPE, RendererFireFurnace::new);
	}
}
