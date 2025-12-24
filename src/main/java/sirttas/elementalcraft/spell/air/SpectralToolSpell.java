package sirttas.elementalcraft.spell.air;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
}