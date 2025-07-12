package sirttas.elementalcraft.test.annotation;

import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import sirttas.elementalcraft.test.GrabServer;
import sirttas.elementalcraft.test.interaction.mekanism.MekanismPreload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
        EphemeralTestServerProvider.class,
        MekanismPreload.class,
        GrabServer.class
})
public @interface ElementalCraftTest {
}
