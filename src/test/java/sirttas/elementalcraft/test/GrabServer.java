package sirttas.elementalcraft.test;

import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GrabServer implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        EphemeralTestServerProvider.grabServer();
    }
}
