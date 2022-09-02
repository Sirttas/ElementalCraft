package sirttas.elementalcraft;

import java.util.HashSet;
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

    public static <T, U> Predicate<T> distinctBy(Function<T, U> getter) {
        var seen = new HashSet<U>();

        return t -> seen.add(getter.apply(t));
    }

}
