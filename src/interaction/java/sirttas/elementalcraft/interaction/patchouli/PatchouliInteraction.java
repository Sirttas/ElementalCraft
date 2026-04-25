package sirttas.elementalcraft.interaction.patchouli;

import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliInteraction {

    private PatchouliInteraction() {}

    @NotNull
    public static ItemStackTemplate createElementopedia() {
        return PatchouliAPI.get().getBookStackTemplate(ElementalCraftApi.createRL("element_book"));
    }
}
