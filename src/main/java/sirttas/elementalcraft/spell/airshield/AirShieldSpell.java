package sirttas.elementalcraft.spell.airshield;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.spell.DataComponentSpell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import java.util.List;
import java.util.Optional;

public class AirShieldSpell extends DataComponentSpell {

    public static final String NAME = "air_shield";

    public AirShieldSpell(Holder<SpellProperties> properties) {
        super(properties, DataComponentMap.builder()
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
    public SpellCastResult castOnSelf(Level level, Entity caster) {
        return SpellCastResult.CHANNEL;
    }
}
