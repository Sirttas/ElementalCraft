package sirttas.elementalcraft.spell.airshield;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.common.ToolActions;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.ToolActionSpell;

import javax.annotation.Nonnull;

public class AirShieldSpell extends ToolActionSpell {

    public static final String NAME = "air_shield";

    public AirShieldSpell(ResourceKey<Spell> key) {
        super(key, ToolActions.DEFAULT_SHIELD_ACTIONS);
    }

    @Override
    public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
        return InteractionResult.CONSUME;
    }
    @Override
    public UseAnim getUseAnimation() {
        return UseAnim.BLOCK;
    }
}
