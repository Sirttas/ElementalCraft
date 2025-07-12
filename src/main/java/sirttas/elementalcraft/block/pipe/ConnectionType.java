package sirttas.elementalcraft.block.pipe;

import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

public enum ConnectionType {
    NONE("none", false),
    CONNECT("connect", true),
    INSERT("insert", true),
    EXTRACT("extract", true),
    DISCONNECT("disconnect", false);

    private final String name;
    private final boolean connected;

    ConnectionType(String name, boolean connected) {
        this.name = name;
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }


    public String getName() {
        return name;
    }

    public static ConnectionType byName(String name) {
        for (ConnectionType type : values()) {
            if (StringUtils.endsWithIgnoreCase(type.getName(), name)) {
                return type;
            }
        }
        return NONE;
    }

    public Component getDisplayName() {
        return Component.translatable("message.elementalcraft.pipe." + name);
    }
}
