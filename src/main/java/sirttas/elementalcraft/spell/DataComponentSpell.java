package sirttas.elementalcraft.spell;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;

public class DataComponentSpell extends Spell {

    private final DataComponentMap dataComponents;

    protected DataComponentSpell(ResourceKey<@NotNull Spell> key, DataComponentMap dataComponents) {
        super(key);
        this.dataComponents = dataComponents;
    }

    public void patch(MutableDataComponentHolder holder) {
        holder.applyComponents(dataComponents);
    }

    public void unpatch(MutableDataComponentHolder holder) {
        for (var component : dataComponents) {
            holder.remove(component.type());
        }
    }
}
