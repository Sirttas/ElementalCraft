package sirttas.elementalcraft.block.source.flux;

import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import org.jetbrains.annotations.VisibleForTesting;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;

import java.util.List;

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
        var list = getSourceFlux(chunkSource);

        handleSourceFluxList(list);
        profiler.pop();
    }

    @VisibleForTesting
    public static void handleSourceFluxList(List<SourceFlux> list) {
        for (var i = 0; i < list.size() - 1; i++) {
            var sourceFlux = list.get(i);

            sourceFlux.recover();
            for (var j = i + 1; j < list.size(); j++) {
                var targetFlux = list.get(j);

                if (sourceFlux.isNeighbor(targetFlux)) {
                    sourceFlux.transfer(targetFlux);
                }
            }
            sourceFlux.afterTransfers();
        }
    }

    private static List<SourceFlux> getSourceFlux(ServerChunkCache chunkSource) {
        List<SourceFlux> list = Lists.newArrayListWithCapacity(chunkSource.chunkMap.size());

        for (var chunkHolder : chunkSource.chunkMap.getChunks()) {
            var levelChunk = chunkHolder.getTickingChunk();

            if (levelChunk != null) {
                list.add(levelChunk.getData(ECDataAttachments.SOURCE_FLUX));
            }
        }
        list.sort(SourceFlux.COMPARATOR);
        return List.copyOf(list);
    }
}
