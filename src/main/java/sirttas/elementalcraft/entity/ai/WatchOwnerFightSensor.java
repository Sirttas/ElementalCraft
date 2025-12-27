package sirttas.elementalcraft.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.entity.spectral.SpectralTool;

import java.util.Set;

public class WatchOwnerFightSensor extends Sensor<SpectralTool> {

    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return Set.of(MemoryModuleType.ATTACK_TARGET);
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull SpectralTool entity) {
        var owner = entity.getOwner();

        if (owner == null) {
            return;
        }

        var lastHurtBy = owner.getLastHurtByMob();

        if (lastHurtBy != null && entity.canAttack(lastHurtBy)) {
            entity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, lastHurtBy);
        }
    }
}
