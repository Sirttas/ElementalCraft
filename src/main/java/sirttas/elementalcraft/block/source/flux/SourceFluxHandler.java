package sirttas.elementalcraft.block.source.flux;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import org.jetbrains.annotations.VisibleForTesting;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class SourceFluxHandler {

    private SourceFluxHandler() {}

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        var level = event.level;

        if (level.isClientSide || event.phase != TickEvent.Phase.END || !(level instanceof ServerLevel serverLevel)) {
            return;
        }

        handleSourceFlux(serverLevel);
    }

    private static void handleSourceFlux(ServerLevel serverLevel) {
        var profiler = serverLevel.getProfiler();

        profiler.push("elementalcraft:source_flux_transfer");

        var chunkSource = serverLevel.getChunkSource();
        var map = getSourceFlux(chunkSource);

        handleSourceFluxMap(map);
        profiler.pop();
    }

    @VisibleForTesting
    public static void handleSourceFluxMap(Map<Long, SourceFlux> map) {
        var suppliers = SourceFlux.NeighborSupplier.of(map);

        for (var sourceFlux : map.values()) {
            sourceFlux.tick(suppliers);
        }
    }

    private static Map<Long, SourceFlux> getSourceFlux(ServerChunkCache chunkSource) {
        Map<Long, SourceFlux> map = new Long2ObjectLinkedOpenHashMap<>();

        for (var chunkHolder : chunkSource.chunkMap.getChunks()) {
            var levelChunk = chunkHolder.getTickingChunk();

            if (levelChunk != null) {
                map.put(levelChunk.getPos().toLong(), levelChunk.getData(ECDataAttachments.SOURCE_FLUX));
            }
        }
        return map;
    }
}
