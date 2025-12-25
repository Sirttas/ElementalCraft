package sirttas.elementalcraft.entity.spectral;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;
import sirttas.elementalcraft.entity.ai.behavior.ClearDigTargetIfNoLongerPresent;
import sirttas.elementalcraft.entity.ai.behavior.FindBlockToDig;
import sirttas.elementalcraft.entity.ai.behavior.FollowOwner;
import sirttas.elementalcraft.entity.ai.behavior.MineDigTarget;
import sirttas.elementalcraft.entity.ai.behavior.MoveToDigTarget;

import java.util.Set;

public class SpectralToolAi {

    private static final double MAX_RANGE_FROM_OWNER = 50;

    protected static Brain<?> makeBrain(SpectralTool spectralTool, Dynamic<?> dynamic) {
        var brain = spectralTool.brainProvider().makeBrain(dynamic);

        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                dropEveryThingIfOwnerIsTooFar(),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
        ));
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
                SetEntityLookTarget.create(8F),
                SetLookAndInteract.create(EntityType.PLAYER, 4),
                FollowOwner.create(8)
        ));
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.DIG, 10, ImmutableList.of(
                ClearDigTargetIfNoLongerPresent.create(),
                FindBlockToDig.create(16, 8),
                MoveToDigTarget.create(),
                new MineDigTarget()
        ), ECMemoryModuleTypes.DIG_TARGET_BLOCK.get());
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
                StopAttackingIfTargetInvalid.create(),
                SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                MeleeAttack.create(20)
        ), MemoryModuleType.ATTACK_TARGET);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static BehaviorControl<SpectralTool> dropEveryThingIfOwnerIsTooFar() {
        return BehaviorBuilder.create(builder ->
                builder.group(
                        builder.registered(ECMemoryModuleTypes.DIG_TARGET_BLOCK.get()),
                        builder.registered(MemoryModuleType.ATTACK_TARGET)
                ).apply(builder, (digTargetBlock, attackTarget) -> (level, entity, time) -> {
                    var owner = entity.getOwner();

                    if (owner == null) {
                        return false;
                    }
                    if (entity.distanceToSqr(owner) > MAX_RANGE_FROM_OWNER * MAX_RANGE_FROM_OWNER) {
                        digTargetBlock.erase();
                        attackTarget.erase();
                        return false;
                    }
                    return false;
                }));
    }
}
