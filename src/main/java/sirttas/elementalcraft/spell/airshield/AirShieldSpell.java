package sirttas.elementalcraft.spell.airshield;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.ItemAbilitySpell;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;

public class AirShieldSpell extends ItemAbilitySpell {

    public static final String NAME = "air_shield";

    public AirShieldSpell(ResourceKey<@NotNull Spell> key) {
        super(key, ItemAbilities.DEFAULT_SHIELD_ACTIONS);
    }

    @Override
    public @Nonnull SpellCastResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
        return SpellCastResult.CHANNEL;
    }
}
