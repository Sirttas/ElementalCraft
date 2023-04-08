package sirttas.elementalcraft.block.pipe;

import net.minecraft.network.chat.Component;

public enum ConnectionType {
    NONE(0, "none", false),
    CONNECT(1, "connect", true),
    INSERT(2, "insert", true),
    EXTRACT(3, "extract", true),
    DISCONNECT(4, "disconnect", false);

    private final int value;
    private final String translationKey;
    private final boolean connected;

    ConnectionType(int value, String key, boolean connected) {
        this.value = value;
        this.translationKey = "message.elementalcraft.pipe." + key;
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public int getValue() {
        return value;
    }

    public static ConnectionType fromInteger(int x) {
        for (ConnectionType type : values()) {
            if (type.getValue() == x) {
                return type;
            }
        }
        return NONE;
    }

    public Component getDisplayName() {
        return Component.translatable(translationKey);
    }
}
