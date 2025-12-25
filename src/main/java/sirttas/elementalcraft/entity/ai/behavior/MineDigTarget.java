package sirttas.elementalcraft.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.entity.ai.ECMemoryModuleTypes;

import java.util.Map;

public class MineDigTarget extends Behavior<LivingEntity> {

    public MineDigTarget() {
        super(Map.of(
                ECMemoryModuleTypes.DIG_TARGET.get(), MemoryStatus.VALUE_PRESENT,
                ECMemoryModuleTypes.DIG_PROGRESS.get(), MemoryStatus.REGISTERED,
                ECMemoryModuleTypes.DIG_TARGET_BLOCK.get(), MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull LivingEntity entity) {
        var target = entity.getBrain().getMemory(ECMemoryModuleTypes.DIG_TARGET.get()).orElse(null);

        if (target == null) {
            return false;
        }

        var block = entity.getBrain().getMemory(ECMemoryModuleTypes.DIG_TARGET_BLOCK.get()).orElse(Blocks.AIR);

        if (block.defaultBlockState().isAir()) {
            return false;
        }

        var state = level.getBlockState(target);

        return isInRange(entity, target) && state.is(block);
    }

    private static boolean isInRange(@NotNull LivingEntity entity, BlockPos target) {
        var range = entity.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);

        return entity.position().distanceToSqr(Vec3.atCenterOf(target)) <= range * range;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, @NotNull LivingEntity entity, long gameTime) {
        return true;
    }

    @Override
    protected void start(@NotNull ServerLevel level, @NotNull LivingEntity entity, long gameTime) {
        var pos = entity.getBrain().getMemory(ECMemoryModuleTypes.DIG_TARGET.get()).orElse(null);

        if (pos == null) {
            return;
        }

        var state = level.getBlockState(pos);

        EnchantmentHelper.onHitBlock(
                level,
                entity.getMainHandItem(),
                entity,
                entity,
                EquipmentSlot.MAINHAND,
                Vec3.atCenterOf(pos),
                state,
                item -> entity.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

        entity.getBrain().eraseMemory(ECMemoryModuleTypes.DIG_PROGRESS.get());
        if (progressDigging(level, entity, pos, state) >= 1) {
            this.doStop(level, entity, gameTime);
        }
    }

    private float getDestroyProgress(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull LivingEntity entity) {
        float destroySpeed = state.getDestroySpeed(level, pos);

        if (destroySpeed == -1.0F) {
            return  0.0F;
        }
        return entity.getMainHandItem().getDestroySpeed(state) / destroySpeed / 30;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull LivingEntity entity, long gameTime) {
        var pos = entity.getBrain().getMemory(ECMemoryModuleTypes.DIG_TARGET.get()).orElse(null);

        if (pos == null) {
            return;
        }

        var state = level.getBlockState(pos);

        if (progressDigging(level, entity, pos, state) >= 1) {
            this.doStop(level, entity, gameTime);
        }
    }

    private float progressDigging(@NotNull ServerLevel level, @NotNull LivingEntity entity, BlockPos pos, BlockState state) {
        float oldProgress = entity.getBrain().getMemory(ECMemoryModuleTypes.DIG_PROGRESS.get()).orElse(0F);
        float destroyProgress = oldProgress + getDestroyProgress(level, pos, state, entity);

        level.destroyBlockProgress(entity.getId(), pos, (int) (destroyProgress * 10F));

        entity.getBrain().setMemory(ECMemoryModuleTypes.DIG_PROGRESS.get(), destroyProgress);
        entity.swing(InteractionHand.MAIN_HAND, true);
        return destroyProgress;
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull LivingEntity entity, long gameTime) {
        entity.getBrain().getMemory(ECMemoryModuleTypes.DIG_TARGET.get()).ifPresent(pos -> level.destroyBlock(pos, true, entity));
        entity.getBrain().eraseMemory(ECMemoryModuleTypes.DIG_TARGET.get());
        entity.getBrain().eraseMemory(ECMemoryModuleTypes.DIG_PROGRESS.get());
    }
}
