package sirttas.elementalcraft.spell.air;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.entity.spectral.SpectralTool;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class SpectralToolSpell extends Spell {

    public SpectralToolSpell(ResourceKey<Spell> key) {
        super(key);
    }

    @Override
    public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
        var level = caster.level();

        if (level.isClientSide || !(caster instanceof LivingEntity livingEntity)) {
            return InteractionResult.PASS;
        }

        var tool = this.getItemInOtherHand(livingEntity);

        if (tool.isEmpty()) {
            return InteractionResult.PASS;
        }

        var entity = new SpectralTool(livingEntity, tool);

        entity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        level.addFreshEntity(entity);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult castOnBlock(@NotNull Entity caster, @NotNull BlockPos target, @NotNull BlockHitResult hitResult) {
        var level = caster.level();

        level.getEntities(caster, caster.getBoundingBox().inflate(this.getRange(caster)), e -> e instanceof SpectralTool)
                .forEach(e -> ((SpectralTool) e).digBlock(target));
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    public @NotNull InteractionResult castOnEntity(@NotNull Entity caster, @NotNull Entity target) {
        if (caster.level().isClientSide || !(target instanceof SpectralTool spectralTool)) {
            return InteractionResult.PASS;
        }

        spectralTool.discard();
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }
}