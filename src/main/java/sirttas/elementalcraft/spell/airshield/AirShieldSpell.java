package sirttas.elementalcraft.spell.airshield;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.DataComponentSpell;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class AirShieldSpell extends DataComponentSpell {

    public static final String NAME = "air_shield";

    public AirShieldSpell(ResourceKey<@NotNull Spell> key) {
        super(key, DataComponentMap.builder()
                .set(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(
                        0.25F,
                        1.0F,
                        List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                        new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                        Optional.empty(), // TODO Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                        Optional.of(SoundEvents.SHIELD_BLOCK),
                        Optional.of(SoundEvents.SHIELD_BREAK)
                )).build());
    }

    @Override
    public @Nonnull SpellCastResult castOnSelf(@Nonnull Level level, @Nonnull Entity caster) {
        return SpellCastResult.CHANNEL;
    }
}
