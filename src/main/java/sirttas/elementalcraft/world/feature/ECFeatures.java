package sirttas.elementalcraft.world.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.placement.SourcePlacement;

public class ECFeatures {

	private static final int RADIUS = 400;

	private static final DeferredRegister<Feature<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.FEATURE, ElementalCraftApi.MODID);

	public static final DeferredHolder<Feature<?>, Feature<IElementTypeFeatureConfig>> SOURCE = DEFERRED_REGISTER.register(SourceFeature.NAME, SourceFeature::new);

	private ECFeatures() {}


	public static void addSpawnSources(ServerLevel level) {
		if (Boolean.TRUE.equals(ECConfig.COMMON.disableSourceSpawn.get())) {
			return;
		}

		BlockPos pos = level.getSharedSpawnPos().offset(-RADIUS / 2, 0, -RADIUS / 2);

		for (var type : ElementType.ALL_VALID) {
			for (int i = 0; i < ECConfig.COMMON.sourceSpawnCount.get(); i++) {
				addSpawnSource(level, pos.offset(level.random.nextInt(RADIUS), 0, level.random.nextInt(RADIUS)), type);
			}
		}
	}

	private static void addSpawnSource(ServerLevel level, BlockPos pos, ElementType type) {
		var x = pos.getX();
		var z = pos.getZ();
		var newPos = new BlockPos(x, SourcePlacement.getHeight(level, x, z), z);
		ChunkPos chunkPos = new ChunkPos(pos);
		ServerChunkCache chunkProvider = level.getChunkSource();

		chunkProvider.getChunk(chunkPos.x, chunkPos.z, true);
		SourceFeature.placeSource(level, newPos, type, 0);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}

}
