package sirttas.elementalcraft.entity.ai.behavior;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;

public class MoveToDigTarget {

    private MoveToDigTarget() {}

    public static <T extends PathfinderMob> BehaviorControl<T> create() {
        return BehaviorBuilder.create(builder ->
                builder.group(
                        builder.present(ECMemoryModuleTypes.DIG_TARGET.get()),
                        builder.absent(MemoryModuleType.WALK_TARGET)
                ).apply(builder, (digTarget, walkTarget) -> (level, entity, time) -> {
                    var pos = builder.get(digTarget);
                    var interactionRange = entity.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);

                    if (entity.position().distanceToSqr(Vec3.atBottomCenterOf(pos)) >= interactionRange * interactionRange) {
                        walkTarget.set(new WalkTarget(Vec3.atBottomCenterOf(pos), 0.6F, 1));
                        return true;
                    }
                    return false;
                }));
    }
}
