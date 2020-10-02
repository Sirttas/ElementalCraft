package sirttas.elementalcraft.network;

import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import sirttas.elementalcraft.block.tile.TileEntityHelper;

/**
 * This class come from Botania code
 * 
 * 
 * https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/api/internal/VanillaPacketDispatcher.java
 */
public class NetworkHelper {

	@SuppressWarnings("resource")
	public static void dispatchTEToNearbyPlayers(TileEntity tile) {
		SUpdateTileEntityPacket packet = tile.getUpdatePacket();
		BlockPos pos = tile.getPos();

		if (packet != null && tile.hasWorld() && tile.getWorld() instanceof ServerWorld) {
			((ServerChunkProvider) tile.getWorld().getChunkProvider()).chunkManager.getTrackingPlayers(new ChunkPos(pos), false).forEach(e -> e.connection.sendPacket(packet));
		}
	}

	public static void dispatchTEToNearbyPlayers(World world, BlockPos pos) {
		TileEntityHelper.getTileEntity(world, pos).ifPresent(NetworkHelper::dispatchTEToNearbyPlayers);
	}
}
