package sirttas.elementalcraft.spell;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Set;

public class ItemAbilitySpell extends Spell {

    private final Set<ItemAbility> itemAbilities;

    protected ItemAbilitySpell(ResourceKey<Spell> key, Set<ItemAbility> itemAbilities) {
        super(key);
        this.itemAbilities = itemAbilities;
    }

    public Set<ItemAbility> getItemAbilities() {
        return itemAbilities;
    }
}
