package sirttas.elementalcraft.test.annotation;

import org.junit.jupiter.params.provider.ArgumentsSource;
import sirttas.elementalcraft.test.provider.RegistryArgumentsProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(RegistryArgumentsProvider.class)
public @interface RegistrySource {

    String value();

    String namespace() default "";

    Exclude[] exclude() default {};

    Class<?> ofType() default Object.class;

    @interface Exclude {
        String value();

        String namespace() default "";
    }

}
