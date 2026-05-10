package sirttas.elementalcraft.spell;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import sirttas.elementalcraft.spell.properties.SpellProperties;

@Deprecated
public class DataComponentSpell extends Spell {

    private final DataComponentMap dataComponents;

    protected DataComponentSpell(Holder<SpellProperties> properties, DataComponentMap dataComponents) {
        super(properties);
        this.dataComponents = dataComponents;
    }

    protected DataComponentMap getDataComponents() {
        return dataComponents;
    }
}
