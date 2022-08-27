package sirttas.elementalcraft.spell.air;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import sirttas.elementalcraft.entity.projectile.FeatherSpike;
import sirttas.elementalcraft.spell.AbstractSpellInstance;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;

public class FeatherSpikesSpell extends Spell {

    public static final String NAME = FeatherSpike.NAME +'s';

    private final int castCount;

    public FeatherSpikesSpell(ResourceKey<Spell> key, int castCount) {
        super(key);
        this.castCount = castCount;
    }

    @Override
    public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
        if (caster instanceof LivingEntity livingEntity) {
            this.addSpellInstance(new Instance(livingEntity));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private class Instance extends AbstractSpellInstance {

        private static final int INTERVAL = 10;

        private final LivingEntity livingEntity;
        private int remainingCasts;

        protected Instance(LivingEntity sender) {
            super(sender, FeatherSpikesSpell.this, castCount * INTERVAL);
            this.livingEntity = sender;
            this.remainingCasts = castCount;
        }

        @Override
        public void tick() {
            if (this.remainingCasts <= 0) {
                stop();
            } else if (this.getTicks() % INTERVAL == 0) {
                var level = livingEntity.level;
                var spike = new FeatherSpike(level, livingEntity);

                spike.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, 4.0F, 1.0F);
                level.addFreshEntity(spike);
                remainingCasts--;
            }
        }
    }

}
