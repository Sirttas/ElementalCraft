package sirttas.elementalcraft.pureore.display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.SessionSearchTrees;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.pureore.PureOre;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class PureOreDisplayManager {

    private static final SessionSearchTrees.Key SEARCH_TREE_RELOAD_KEY = new SessionSearchTrees.Key();
    private static final PureOreDisplayManager INSTANCE = new PureOreDisplayManager();

    private final Map<ResourceLocation, PureOreDisplay> pureOreDisplays = new HashMap<>();

    private PureOreDisplayManager() { }

    public static PureOreDisplayManager getInstance() {
        return INSTANCE;
    }

    public void regenerate(Map<ResourceLocation, PureOre> pureOres) {
        ClientPacketListener clientpacketlistener = Minecraft.getInstance().getConnection();

        if (clientpacketlistener == null) {
            doRegenerate(pureOres);
            return;
        }
        clientpacketlistener.searchTrees().register(SEARCH_TREE_RELOAD_KEY, () -> doRegenerate(pureOres));
    }

    private void doRegenerate(Map<ResourceLocation, PureOre> pureOres) {
        this.pureOreDisplays.clear();
        for (var entry : pureOres.entrySet()) {
            var id = entry.getKey();
            var pureOre = entry.getValue();

            this.pureOreDisplays.put(id, new PureOreDisplay(id, pureOre));
        }
    }

    @Nullable
    public Component getPureOreName(@Nonnull ItemStack stack) {
        var display = getPureOreDisplay(stack);

        return display != null ? display.name() : null;
    }

    @Nullable
    public int[] getColors(@Nonnull ItemStack stack) {
        var display = getPureOreDisplay(stack);

        return display != null ? display.colors() : null;
    }

    private @Nullable PureOreDisplay getPureOreDisplay(@NotNull ItemStack stack) {
        var id = PureOre.getId(stack);

        if (id == null) {
            return null;
        }
        return this.pureOreDisplays.get(id);
    }
}
