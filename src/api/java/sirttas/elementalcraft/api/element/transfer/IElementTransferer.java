package sirttas.elementalcraft.api.element.transfer;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.stream.Stream;

public interface IElementTransferer {

	default ConnectionType getConnection(Direction face) {
		return getConnections().getOrDefault(face, ConnectionType.NONE);
	}

	Map<Direction, ConnectionType> getConnections();

	default Stream<Map.Entry<Direction, ConnectionType>> getConnectionStream() {
		return getConnections().entrySet().stream();
	}

	int getRemainingTransferAmount();

	void transfer(int amount);

	boolean isValid();

	enum ConnectionType {
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
			this.translationKey = "message.elementalcraft." + key;
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
}
