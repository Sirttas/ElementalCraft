package sirttas.elementalcraft.spell;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.ToolAction;

import java.util.Set;

public class ToolActionSpell extends Spell {

    private final Set<ToolAction> actions;

    protected ToolActionSpell(ResourceKey<Spell> key, Set<ToolAction> actions) {
        super(key);
        this.actions = actions;
    }

    public Set<ToolAction> getActions() {
        return actions;
    }
}
