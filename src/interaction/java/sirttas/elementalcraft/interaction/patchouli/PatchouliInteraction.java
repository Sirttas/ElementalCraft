package sirttas.elementalcraft.interaction.patchouli;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliInteraction {

    private PatchouliInteraction() {}

    @NotNull
    public static ItemStack createElementopedia() {
        return PatchouliAPI.get().getBookStack(ElementalCraftApi.createRL("element_book"));
    }
}
