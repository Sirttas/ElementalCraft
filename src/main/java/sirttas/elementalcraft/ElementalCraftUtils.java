package sirttas.elementalcraft;

import java.util.HashSet;
import net.minecraft.world.phys.AABB;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static <T, U> Predicate<T> distinctBy(Function<T, U> getter) {
        var seen = new HashSet<U>();

        return t -> seen.add(getter.apply(t));
    }

}
