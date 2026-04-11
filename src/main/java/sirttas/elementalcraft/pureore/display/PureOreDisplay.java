package sirttas.elementalcraft.pureore.display;

import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.pureore.PureOre;

import java.util.Comparator;

@OnlyIn(Dist.CLIENT)
public record PureOreDisplay(
        Component name,
        int[] colors
) {

    private static final String MINECRAFT = "minecraft";
    private static final String DEEPSLATE = "deepslate";

    private static final Comparator<Identifier> MINECRAFT_NAMESPACE_COMPARATOR = (name1, name2) -> {
        if (MINECRAFT.equals(name1.getNamespace()) && !MINECRAFT.equals(name2.getNamespace())) {
            return -1;
        } else if (!MINECRAFT.equals(name1.getNamespace()) && MINECRAFT.equals(name2.getNamespace())) {
            return 1;
        }
        return 0;
    };

    private static final Comparator<Identifier> DEEPSLATE_COMPARATOR = (name1, name2) -> {
        if (name1.getPath().contains(DEEPSLATE) && !name2.getPath().contains(DEEPSLATE)) {
            return 1;
        } else if (!name1.getPath().contains(DEEPSLATE) && name2.getPath().contains(DEEPSLATE)) {
            return -1;
        }
        return 0;
    };

    private static final Comparator<Holder<@NotNull Item>> DESCRIPTION_COMPARATOR = Comparator.comparing(h -> h.getKey().identifier(), MINECRAFT_NAMESPACE_COMPARATOR.thenComparing(DEEPSLATE_COMPARATOR).thenComparing(Identifier::compareTo));

    public PureOreDisplay(Identifier id, PureOre pureOre) {
        this(loadPureOreName(id, pureOre), loadPureOreColors(pureOre));
    }

    private static Component loadPureOreName(Identifier id, PureOre pureOre) {
        var translationKey = "tooltip.elementalcraft.pure_ore." + id.getNamespace() + "." + id.getPath();

        if (Language.getInstance().has(translationKey)) {
            return Component.translatable(translationKey);
        }

        return pureOre.items().stream()
                .sorted(DESCRIPTION_COMPARATOR)
                .map(holder -> Component.translatable("tooltip.elementalcraft.pure_ore", new ItemStack(holder).getItemName()))
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
