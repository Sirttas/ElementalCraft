package sirttas.elementalcraft.loot.parameter;

import net.minecraft.core.Direction;
import net.minecraft.util.context.ContextKey;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECLootContextParams {

    public static final ContextKey<@NotNull Direction> DIRECTION = create("direction");

    private ECLootContextParams() {}

    private static <T> ContextKey<@NotNull T> create(String name) {
        return new ContextKey<>(ElementalCraftApi.identifier(name));
    }
}
