package sirttas.elementalcraft.api.capability;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;
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
        private ElementStorages() {
        }

        private static final ResourceLocation ID = ElementalCraftApi.createRL(ECNames.ELEMENT_STORAGE);
        private static final ResourceLocation FOR_ELEMENT_ID = ElementalCraftApi.createRL("element_storage_for_element");

        public static final BlockCapability<IElementStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, IElementStorage.class);
        public static final BlockCapability<ISingleElementStorage, @Nullable BlockForElementContext> BLOCK_FOR_ELEMENT = BlockCapability.create(FOR_ELEMENT_ID, ISingleElementStorage.class, BlockForElementContext.class);
        public static final EntityCapability<IElementStorage, Void> ENTITY = EntityCapability.createVoid(ID, IElementStorage.class);
        public static final EntityCapability<ISingleElementStorage, @Nullable ElementType> ENTITY_FOR_ELEMENT = EntityCapability.create(FOR_ELEMENT_ID, ISingleElementStorage.class, ElementType.class);

        public static final ItemCapability<IElementStorage, Void> ITEM = ItemCapability.createVoid(ID, IElementStorage.class);

        public record BlockForElementContext(
                ElementType elementType,
                Direction direction
        ) {
        }
    }

    public static class ElementTransferers {
        private ElementTransferers() {
        }

        public static final BlockCapability<IElementTransferer, @Nullable Direction> BLOCK = BlockCapability.createSided(ElementalCraftApi.createRL("element_transferer"), IElementTransferer.class);
    }

    public static class SourceTraits {
        private SourceTraits() {
        }

        private static final ResourceLocation ID = ElementalCraftApi.createRL("source_trait_holder");

        public static final BlockCapability<ISourceTraitHolder, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, ISourceTraitHolder.class);
        public static final EntityCapability<ISourceTraitHolder, Void> ENTITY = EntityCapability.createVoid(ID, ISourceTraitHolder.class);
        public static final ItemCapability<ISourceTraitHolder, Void> ITEM = ItemCapability.createVoid(ID, ISourceTraitHolder.class);
    }

    public static class RuneHandlers {
        private RuneHandlers() {
        }

        private static final ResourceLocation ID = ElementalCraftApi.createRL("rune_handler");

        public static final BlockCapability<IRuneHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, IRuneHandler.class);
        public static final EntityCapability<IRuneHandler, Void> ENTITY = EntityCapability.createVoid(ID, IRuneHandler.class);
        public static final ItemCapability<IRuneHandler, Void> ITEM = ItemCapability.createVoid(ID, IRuneHandler.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static class ShrineUpgrades {
        private ShrineUpgrades() {
        }

        private static final ResourceLocation ID = ElementalCraftApi.createRL("shrine_upgrade");

        public static final BlockCapability<Holder<ShrineUpgrade>, @Nullable Direction> BLOCK = (BlockCapability) BlockCapability.createSided(ID, Holder.class);
        public static final ItemCapability<Holder<ShrineUpgrade>, Void> ITEM =  (ItemCapability) ItemCapability.createVoid(ID, Holder.class);

    }
}
