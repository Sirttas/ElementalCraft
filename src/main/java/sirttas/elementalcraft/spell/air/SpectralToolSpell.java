package sirttas.elementalcraft.spell.air;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.entity.spectral.SpectralTool;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class SpectralToolSpell extends Spell {

    public SpectralToolSpell(ResourceKey<Spell> key) {
        super(key);
    }

    @Override
    public @Nonnull InteractionResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
        if (level.isClientSide || !(caster instanceof LivingEntity livingEntity)) {
            return InteractionResult.PASS;
        }

        var tool = this.getItemInOtherHand(livingEntity);

        if (tool.isEmpty() || !tool.is(ECTags.Items.SPECTRAL_TOOLS)) {
            return InteractionResult.PASS;
        }

        var entity = new SpectralTool(livingEntity, tool.copy());

        entity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        level.addFreshEntity(entity);

        if (livingEntity instanceof Player player && !player.getAbilities().instabuild) {
            tool.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult castOnBlock(@Nonnull Level level, @NotNull Entity caster, @NotNull BlockPos target, @NotNull BlockHitResult hitResult) {
        var state = level.getBlockState(target);

        if (state.isAir()) {
            return InteractionResult.PASS;
        }

        var hasGivenOrder = new MutableBoolean(false);

        level.getEntitiesOfClass(SpectralTool.class, caster.getBoundingBox().inflate(this.getRange(caster))).forEach(spectralTool -> {
            if (spectralTool.getOwner() == caster && spectralTool.digBlock(target, state)) {
                hasGivenOrder.setTrue();
            }
        });
        return hasGivenOrder.booleanValue() ? InteractionResult.SUCCESS_NO_ITEM_USED : InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult castOnEntity(@Nonnull Level level, @NotNull Entity caster, @NotNull Entity target) {
        if (level.isClientSide || !(target instanceof SpectralTool spectralTool)) {
            return InteractionResult.PASS;
        }

        spectralTool.kill();
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }
}