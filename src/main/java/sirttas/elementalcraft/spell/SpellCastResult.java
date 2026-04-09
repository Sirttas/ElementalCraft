package sirttas.elementalcraft.spell;

import net.minecraft.world.InteractionResult;

public record SpellCastResult(InteractionResult interactionResult, boolean consume, boolean startCooldown) {

    public static final SpellCastResult PASS = new SpellCastResult(InteractionResult.PASS, false, false);
    public static final SpellCastResult SUCCESS = new SpellCastResult(InteractionResult.SUCCESS, true, true);
    public static final SpellCastResult CHANNEL = new SpellCastResult(InteractionResult.CONSUME, true, false);
    public static final SpellCastResult FAIL = new SpellCastResult(InteractionResult.FAIL, true, true);

    public boolean success() {
        return interactionResult.consumesAction();
    }
}
