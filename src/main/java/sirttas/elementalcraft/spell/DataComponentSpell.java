package sirttas.elementalcraft.spell;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class DataComponentSpell extends Spell {

    private final DataComponentMap dataComponents;

    protected DataComponentSpell(ResourceKey<@NotNull Spell> key, DataComponentMap dataComponents) {
        super(key);
        this.dataComponents = dataComponents;
    }

    protected DataComponentMap getDataComponents() {
        return dataComponents;
    }
}
