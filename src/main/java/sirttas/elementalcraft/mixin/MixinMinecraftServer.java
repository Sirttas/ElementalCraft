package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.commands.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.ServerLevel;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.world.feature.ECFeatures;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer extends ReentrantBlockableEventLoop<TickTask> implements CommandSource, AutoCloseable {

	@Shadow
	public abstract ServerLevel overworld();
	
	protected MixinMinecraftServer(String name) {
		super(name);
	}
	
	@Inject(method = "createLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V", 
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer; setInitialSpawn(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/storage/ServerLevelData;ZZ)V"))
	private void addSpawnSources(ChunkProgressListener listener, CallbackInfo ci) {
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableWorldGen.get())) {
			ECFeatures.addSpawnSources(this.overworld());
		}
	}
}
