package sirttas.elementalcraft.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.apache.commons.lang3.mutable.MutableLong;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;

public class FindBlockToDig {

    private FindBlockToDig() {}

    public static <T extends LivingEntity> BehaviorControl<T> create(int range) {
        MutableLong nextTry = new MutableLong(0L);

        return BehaviorBuilder.create(builder ->
                builder.group(
                        builder.present(ECMemoryModuleTypes.DIG_TARGET_STATE.get()),
                        builder.absent(ECMemoryModuleTypes.DIG_TARGET.get()),
                        builder.registered(MemoryModuleType.LOOK_TARGET)
                ).apply(builder, (digTargetState, digTarget, lookTarget) -> (world, entity, time) -> {
                    if (time < nextTry.getValue()) {
                        nextTry.setValue(time + 22);
                        return true;
                    }

                    for (BlockPos pos : BlockPos.withinManhattan(entity.blockPosition(), range, range, range)) {
                        var state = world.getBlockState(pos);

                        if (state.equals(builder.get(digTargetState))) {
                            lookTarget.set(new BlockPosTracker(pos));
                            digTarget.set(pos);
                            return true;
                        }
                    }
                    return false;
                }));
    }
}
