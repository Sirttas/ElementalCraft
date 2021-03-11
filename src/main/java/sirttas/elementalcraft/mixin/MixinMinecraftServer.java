package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.command.ICommandSource;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.concurrent.RecursiveEventLoop;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.server.ServerWorld;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.world.feature.ECFeatures;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer extends RecursiveEventLoop<TickDelayedTask> implements ISnooperInfo, ICommandSource, AutoCloseable {

	@Shadow
	public abstract ServerWorld func_241755_D_();
	
	protected MixinMinecraftServer(String name) {
		super(name);
	}
	
	@Inject(method = "func_240787_a_", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer; func_240786_a_(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/storage/IServerWorldInfo;ZZZ)V"))
	private void addSpawnSources(IChunkStatusListener listener, CallbackInfo ci) {
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableWorldGen.get())) {
			ECFeatures.addSpawnSources(this.func_241755_D_());
		}
	}
}
