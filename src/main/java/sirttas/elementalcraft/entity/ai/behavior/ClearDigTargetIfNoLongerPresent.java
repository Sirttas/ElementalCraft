package sirttas.elementalcraft.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;

public class ClearDigTargetIfNoLongerPresent {

    private ClearDigTargetIfNoLongerPresent() {}

    public static <T extends LivingEntity> BehaviorControl<T> create() {
        return BehaviorBuilder.create(builder ->
                builder.group(
                        builder.present(ECMemoryModuleTypes.DIG_TARGET_STATE.get()),
                        builder.present(ECMemoryModuleTypes.DIG_TARGET.get())
                ).apply(builder, (digTargetState, digTarget) -> (level, entity, time) -> {
                    var state = level.getBlockState(builder.get(digTarget));

                    if (!builder.get(digTargetState).equals(state)) {
                        digTarget.erase();
                        return true;
                    }
                    return false;
                }));
    }
}
