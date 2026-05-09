package sirttas.elementalcraft.network.payload;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class PayloadHelper {

	private PayloadHelper() {}

	public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> createType(String name) {
		return new CustomPacketPayload.Type<>(ElementalCraftApi.identifier(name));
	}
}
