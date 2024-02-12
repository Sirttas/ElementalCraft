package sirttas.elementalcraft.api.capability;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;

public class ElementalCraftCapabilities {

    private ElementalCraftCapabilities() {}

    public static class ElementStorage {
        private ElementStorage() {}

        private static final ResourceLocation ID = ElementalCraftApi.createRL(ECNames.ELEMENT_STORAGE);

        public static final BlockCapability<IElementStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, IElementStorage.class);
        public static final EntityCapability<IElementStorage, Void> ENTITY = EntityCapability.createVoid(ID, IElementStorage.class);
        public static final EntityCapability<ISingleElementStorage, @Nullable ElementType> ENTITY_FOR_ELEMENT = EntityCapability.create(ElementalCraftApi.createRL("element_storage_for_element"), ISingleElementStorage.class, ElementType.class);

        public static final ItemCapability<IElementStorage, Void> ITEM = ItemCapability.createVoid(ID, IElementStorage.class);
        public static final ItemCapability<ISingleElementStorage, Integer> ITEM_LENS = ItemCapability.create(ElementalCraftApi.createRL("lens_element_storage"), ISingleElementStorage.class, Integer.class);
    }

    public static class ElementTransferer {
        private ElementTransferer() {}

        public static final BlockCapability<IElementTransferer, @Nullable Direction> BLOCK = BlockCapability.createSided(ElementalCraftApi.createRL("element_transferer"), IElementTransferer.class);
        public static final EntityCapability<IElementTransferer, Void> ENTITY = EntityCapability.createVoid(ElementalCraftApi.createRL("element_transferer"), IElementTransferer.class);
    }

    public static class SourceTrait {
        private SourceTrait() {}

        private static final ResourceLocation ID = ElementalCraftApi.createRL("source_trait_holder");

        public static final BlockCapability<ISourceTraitHolder, @Nullable Direction> BLOCK = BlockCapability.createSided(ID, ISourceTraitHolder.class);
        public static final EntityCapability<ISourceTraitHolder, Void> ENTITY = EntityCapability.createVoid(ID, ISourceTraitHolder.class);
        public static final ItemCapability<ISourceTraitHolder, Void> ITEM = ItemCapability.createVoid(ID, ISourceTraitHolder.class);
    }

    public static class RuneHandler {
        private RuneHandler() {}

        public static final BlockCapability<IRuneHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(ElementalCraftApi.createRL("rune_handler"), IRuneHandler.class);
        public static final EntityCapability<IRuneHandler, Void> ENTITY = EntityCapability.createVoid(ElementalCraftApi.createRL("rune_handler"), IRuneHandler.class);
    }

}
