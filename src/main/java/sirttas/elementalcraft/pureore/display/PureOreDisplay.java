package sirttas.elementalcraft.pureore.display;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.pureore.PureOre;

@OnlyIn(Dist.CLIENT)
public record PureOreDisplay(
        Component name,
        int[] colors
) {

    public PureOreDisplay(Identifier id, PureOre pureOre) {
        this(loadPureOreName(id, pureOre), loadPureOreColors(pureOre));
    }

    private static Component loadPureOreName(Identifier id, PureOre pureOre) {
        var translationKey = "tooltip.elementalcraft.pure_ore." + id.getNamespace() + "." + id.getPath();

        if (Language.getInstance().has(translationKey)) {
            return Component.translatable(translationKey);
        }

        return pureOre.items().stream()
                .map(holder -> Component.translatable("tooltip.elementalcraft.pure_ore", holder.value().getDescription()))
                .findFirst()
                .orElseGet(() -> Component.literal("ERROR no name"));
    }

    private static int[] loadPureOreColors(PureOre pureOre) {
        return pureOre.resultsForColor().stream()
                .map(ElementalCraft.interactions()::lookupColors)
                .findFirst()
                .orElse(null);
    }
}
