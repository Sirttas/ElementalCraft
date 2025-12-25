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
                        builder.present(ECMemoryModuleTypes.DIG_TARGET_BLOCK.get()),
                        builder.present(ECMemoryModuleTypes.DIG_TARGET.get())
                ).apply(builder, (digTargetBlock, digTarget) -> (level, entity, time) -> {
                    var state = level.getBlockState(builder.get(digTarget));

                    if (!state.is(builder.get(digTargetBlock))) {
                        digTarget.erase();
                        return true;
                    }
                    return false;
                }));
    }
}
