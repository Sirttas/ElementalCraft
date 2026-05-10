package sirttas.elementalcraft.spell;

import net.minecraft.core.Holder;
import net.neoforged.neoforge.common.ItemAbility;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import java.util.Set;

public class ItemAbilitySpell extends Spell {

    private final Set<ItemAbility> itemAbilities;

    protected ItemAbilitySpell(Holder<SpellProperties> properties, Set<ItemAbility> itemAbilities) {
        super(properties);
        this.itemAbilities = itemAbilities;
    }

    public Set<ItemAbility> getItemAbilities() {
        return itemAbilities;
    }
}
