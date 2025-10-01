package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BudTypeProvider extends AbstractManagedDataBuilderProvider<BuddingShrineBudType, BuddingShrineBudType> {

    public BudTypeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, ElementalCraftApi.BUD_TYPE_MANAGER, BuddingShrineBudType.CODEC);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        add(IDataManager.createKey(ElementalCraftApi.BUD_TYPE_MANAGER_KEY, ElementalCraftApi.createRL("springaline")), new BuddingShrineBudType(
                List.of(ECBlocks.SMALL_SPRINGALINE_BUD.get(), ECBlocks.MEDIUM_SPRINGALINE_BUD.get(), ECBlocks.LARGE_SPRINGALINE_BUD.get(), ECBlocks.SPRINGALINE_CLUSTER.get()),
                ElementalCraftApi.SHRINE_UPGRADE_MANAGER.getOrCreateHolder(ShrineUpgrades.SPRINGALINE),
                ElementalCraftApi.createRL(BuddingShrineBudType.PLATE_MODEL_FOLDER + "/springaline")
        ));
    }

    @Override
    public @NotNull String getName() {
        return "ElementalCraft Budding Shrine Bud Types";
    }
}
