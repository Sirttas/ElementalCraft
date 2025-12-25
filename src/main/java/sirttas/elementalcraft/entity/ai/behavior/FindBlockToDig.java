package sirttas.elementalcraft.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.apache.commons.lang3.mutable.MutableLong;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;

public class FindBlockToDig {

    private static final long TRY_INTERVAL = 22L;

    private FindBlockToDig() {}

    public static <T extends PathfinderMob> BehaviorControl<T> create(int range) {
        return create(range, range);
    }

    public static <T extends PathfinderMob> BehaviorControl<T> create(int range, int verticalRange) {
        MutableLong nextTry = new MutableLong(0L);

        return BehaviorBuilder.create(builder ->
                builder.group(
                        builder.present(ECMemoryModuleTypes.DIG_TARGET_STATE.get()),
                        builder.absent(ECMemoryModuleTypes.DIG_TARGET.get()),
                        builder.registered(MemoryModuleType.LOOK_TARGET)
                ).apply(builder, (digTargetState, digTarget, lookTarget) -> (level, entity, time) -> {
                    if (time < nextTry.getValue()) {
                        nextTry.setValue(time + TRY_INTERVAL);
                        return true;
                    }

                    for (BlockPos pos : BlockPos.withinManhattan(entity.blockPosition(), range, verticalRange, range)) {
                        var state = level.getBlockState(pos);

                        if (builder.get(digTargetState).equals(state)) {
                            lookTarget.set(new BlockPosTracker(pos));
                            digTarget.set(pos);
                            return true;
                        }
                    }
                    return false;
                }));
    }
}
