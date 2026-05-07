package sirttas.elementalcraft.api.capability;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;

public class ElementalCraftCapabilities {

    private ElementalCraftCapabilities() {
    }

    public static class ElementStorages {
        private ElementStorages() {}

        private static final Identifier ID = ElementalCraftApi.createRL(ECNames.ELEMENT_STORAGE);
        private static final Identifier FOR_ELEMENT_ID = ElementalCraftApi.createRL("element_storage_for_element");

        public static final BlockCapability<@NotNull IElementStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, IElementStorage.class);
        public static final BlockCapability<@NotNull ISingleElementStorage, @Nullable BlockForElementContext> BLOCK_FOR_ELEMENT = BlockCapability.create(FOR_ELEMENT_ID, ISingleElementStorage.class, BlockForElementContext.class);
        public static final EntityCapability<@NotNull IElementStorage, Void> ENTITY = EntityCapability.createVoid(ID, IElementStorage.class);
        public static final EntityCapability<@NotNull ISingleElementStorage, @Nullable ElementType> ENTITY_FOR_ELEMENT = EntityCapability.create(FOR_ELEMENT_ID, ISingleElementStorage.class, ElementType.class);

        public static final ItemCapability<@NotNull IElementStorage, Void> ITEM = ItemCapability.createVoid(ID, IElementStorage.class);

        public record BlockForElementContext(
                ElementType elementType,
                Direction direction
        ) { }
    }

    public static class ElementTransferers {
        private ElementTransferers() {
        }

        public static final BlockCapability<@NotNull IElementTransferer, @Nullable Direction> BLOCK = BlockCapability.createSided(ElementalCraftApi.createRL("element_transferer"), IElementTransferer.class);
    }

    public static class SourceTraits {
        private SourceTraits() {}

        private static final Identifier ID = ElementalCraftApi.createRL("source_trait_holder");

        public static final BlockCapability<@NotNull ISourceTraitHolder, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, ISourceTraitHolder.class);
        public static final EntityCapability<@NotNull ISourceTraitHolder, Void> ENTITY = EntityCapability.createVoid(ID, ISourceTraitHolder.class);
        public static final ItemCapability<@NotNull ISourceTraitHolder, Void> ITEM = ItemCapability.createVoid(ID, ISourceTraitHolder.class);
    }

    public static class RuneHandlers {
        private RuneHandlers() {}

        private static final Identifier ID = ElementalCraftApi.createRL("rune_handler");

        public static final BlockCapability<@NotNull IRuneHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, IRuneHandler.class);
        public static final EntityCapability<@NotNull IRuneHandler, Void> ENTITY = EntityCapability.createVoid(ID, IRuneHandler.class);
        public static final ItemCapability<@NotNull IRuneHandler, Void> ITEM = ItemCapability.createVoid(ID, IRuneHandler.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static class ShrineUpgrades {
        private ShrineUpgrades() {}

        private static final Identifier ID = ElementalCraftApi.createRL("shrine_upgrade");

        public static final BlockCapability<@NotNull Holder<@NotNull ShrineUpgrade>, @Nullable Direction> BLOCK = (BlockCapability) BlockCapability.createSided(ID, Holder.class);
        public static final ItemCapability<@NotNull Holder<@NotNull ShrineUpgrade>, Void> ITEM =  (ItemCapability) ItemCapability.createVoid(ID, Holder.class);

    }
}
