package sirttas.elementalcraft.block.pipe;

import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public interface IElementPipe {

	ConnectionType getConection(Direction face);

	default boolean isPriority(Direction face) {
		return false;
	}
	
	public enum ConnectionType {
		NONE(0, "none", false),
		CONNECT(1, "connect", true),
		INSERT(2, "insert", true),
		EXTRACT(3, "extract", true),
		DISCONNECT(4, "disconnect", false);

		private final int value;
		private final String translationKey;
		private final boolean connected;

		private ConnectionType(int value, String key, boolean connected) {
			this.value = value;
			this.translationKey = "message.elementalcraft." + key;
			this.connected = connected;
		}

		boolean isConnected() {
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

		public ITextComponent getDisplayName() {
			return new TranslationTextComponent(translationKey);
		}
	}
}