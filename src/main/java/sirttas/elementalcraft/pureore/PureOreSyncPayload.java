package sirttas.elementalcraft.pureore;

import com.google.common.collect.Maps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.network.payload.PayloadHelper;
import sirttas.elementalcraft.pureore.display.PureOreDisplayManager;

import java.util.Map;

public record PureOreSyncPayload(Map<Identifier, PureOre> pureOres) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<@NotNull PureOreSyncPayload> TYPE = PayloadHelper.createType("pure_ore_sync");
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull PureOreSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    Maps::newHashMapWithExpectedSize,
                    StreamCodec.of(RegistryFriendlyByteBuf::writeIdentifier, RegistryFriendlyByteBuf::readIdentifier),
                    PureOre.STREAM_CODEC
            ),
            PureOreSyncPayload::pureOres,
            PureOreSyncPayload::new);

    @Override
    public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext payloadContext) {
        payloadContext.enqueueWork(() -> {
            var player = payloadContext.player();
            var server = player.getServer();

            if (server == null || !server.isSingleplayerOwner(player.getGameProfile())) { // don't replace pure ores for local player
                PureOreManager.getInstance().replacePureOres(pureOres);
            }
            PureOreDisplayManager.getInstance().regenerate(pureOres);
            ElementalCraftApi.LOGGER.info("Pure ores synced to client.");
        });
    }
}
