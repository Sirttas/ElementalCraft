package sirttas.elementalcraft.test.annotation;

import org.junit.jupiter.params.provider.ArgumentsSource;
import sirttas.elementalcraft.test.provider.TagArgumentsProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(TagArgumentsProvider.class)
public @interface TagSource {

    Tag[] value();

    String registry();

    String registryNamespace() default "";

    RegistrySource.Exclude[] exclude() default {};

    @interface Tag {
        String value();

        String namespace() default "";
    }
}
