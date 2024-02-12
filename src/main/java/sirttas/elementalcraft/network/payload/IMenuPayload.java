package sirttas.elementalcraft.network.payload;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.container.menu.screen.IRefreshedScreen;

public interface IMenuPayload<T extends AbstractContainerMenu> extends CustomPacketPayload {

    Class<? extends T> getMenuType();

    void handleOnMenu(PlayPayloadContext ctx, T menu);

    default void handle(PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            var minecraft = Minecraft.getInstance();
            var menu = minecraft.player.containerMenu;
            var type = getMenuType();

            if (type.isInstance(menu)) {
                handleOnMenu(ctx, type.cast(menu));
            }
            if (minecraft.screen instanceof IRefreshedScreen refreshedScreen) {
                refreshedScreen.refresh();
            }
        });
    }
}
