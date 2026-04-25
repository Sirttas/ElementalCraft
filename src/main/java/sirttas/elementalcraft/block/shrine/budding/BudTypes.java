package sirttas.elementalcraft.block.shrine.budding;

import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;

public class BudTypes {

    public static final ResourceKey<BuddingShrineBudType> AMETHYST = createKey("amethyst");
    public static final ResourceKey<BuddingShrineBudType> SPRINGALINE = createKey("springaline");
    public static final ResourceKey<BuddingShrineBudType> CERTUS_QUARTZ = createKey("certus_quartz");

    private BudTypes() {}

    private static ResourceKey<@NotNull BuddingShrineBudType> createKey(String name) {
        return IDataManager.createKey(ElementalCraftApi.BUD_TYPE_MANAGER_KEY, ElementalCraftApi.createRL(name));
    }
}
