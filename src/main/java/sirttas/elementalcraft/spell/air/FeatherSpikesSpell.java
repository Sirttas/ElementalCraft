package sirttas.elementalcraft.spell.air;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.entity.projectile.FeatherSpike;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.spell.tick.SpellInstance;

public class FeatherSpikesSpell extends Spell {

    public static final String NAME = FeatherSpike.NAME + 's';

    private final int castCount;

    public FeatherSpikesSpell(Holder<SpellProperties> properties, int castCount) {
        super(properties);
        this.castCount = castCount;
    }

    @Override
    public SpellCastResult castOnSelf(Level level, Entity caster) {
        if (caster instanceof LivingEntity livingEntity) {
            this.addSpellInstance(new Instance(livingEntity));
            return SpellCastResult.SUCCESS;
        }
        return SpellCastResult.PASS;
    }

    private class Instance extends SpellInstance {

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
                var level = livingEntity.level();
                var spike = new FeatherSpike(level, livingEntity);

                spike.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, getStrength(), 1.0F);
                level.addFreshEntity(spike);
                remainingCasts--;
            }
        }
    }

}
