package sirttas.elementalcraft;

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

}
