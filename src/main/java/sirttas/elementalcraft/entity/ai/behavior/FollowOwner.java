package sirttas.elementalcraft.entity.ai.behavior;

import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;

public class FollowOwner {

    private FollowOwner() {}

    public static <T extends PathfinderMob & OwnableEntity> BehaviorControl<T> create(double range) {
        return BehaviorBuilder.create(builder ->
            builder.group(
                    builder.absent(MemoryModuleType.WALK_TARGET),
                    builder.registered(MemoryModuleType.LOOK_TARGET)
            ).apply(builder, (walkTarget, lookTarget) -> (world, entity, time) -> {
                var owner = entity.getOwner();

                if (owner == null) {
                    return false;
                }

                var ownerPos = owner.position();

                if (entity.distanceToSqr(owner) < range * range) {
                    return false;
                }

                var target = LandRandomPos.getPosTowards(entity, 10, 7, ownerPos);

                if (target == null) {
                    return false;
                }

                lookTarget.set(new BlockPosTracker(ownerPos));
                walkTarget.set(new WalkTarget(target, 0.6F, 0));
                return true;
            }));
    }
}
