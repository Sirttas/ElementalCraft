package sirttas.elementalcraft.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.entity.spectral.SpectralTool;

import java.util.Set;

public class WatchOwnerDigSensor extends Sensor<SpectralTool> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return Set.of(
                ECMemoryModuleTypes.DIG_TARGET_STATE.get()
        );
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull SpectralTool entity) {
        var owner = entity.getOwner();

        if (owner instanceof ServerPlayer player && player.gameMode.isDestroyingBlock) {
            entity.getBrain().setMemory(ECMemoryModuleTypes.DIG_TARGET_STATE.get(), level.getBlockState(player.gameMode.destroyPos));
        }
    }
}
