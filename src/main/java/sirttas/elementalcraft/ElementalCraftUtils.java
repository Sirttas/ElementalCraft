package sirttas.elementalcraft;

import net.minecraft.world.phys.AABB;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ElementalCraftUtils {

    private ElementalCraftUtils() {}

    public static <S, T> BiConsumer<S, Consumer<T>> cast(Class<T> clazz) {
        return (s, c) -> {
            if (clazz.isInstance(s)) {
                c.accept(clazz.cast(s));
            }
        };
    }

    public static AABB stitchAABB(AABB source) {
        return new AABB(Math.floor(source.minX), Math.floor(source.minY), Math.floor(source.minZ), Math.ceil(source.maxX), Math.ceil(source.maxY), Math.ceil(source.maxZ));
    }

}
