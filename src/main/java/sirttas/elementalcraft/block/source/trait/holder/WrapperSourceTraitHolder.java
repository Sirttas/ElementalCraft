package sirttas.elementalcraft.block.source.trait.holder;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

import java.util.Map;

public class WrapperSourceTraitHolder implements ISourceTraitHolder {

    private final SourceTraitHolder delegate;
    private final ItemStack stack;

    public WrapperSourceTraitHolder(ItemStack stack) {
        delegate = new SourceTraitHolder();
        this.stack = stack;
    }

    @Override
    public Map<ResourceKey<SourceTrait>, ISourceTraitValue> getTraits() {
        load();
        return delegate.getTraits();
    }

    @Override
    public void setTraits(Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
        delegate.setTraits(traits);
        save();
    }

    @Override
    public boolean isArtificial() {
        load();
        return delegate.isArtificial();
    }

    private void load() {
        var tag = stack.getTag();

        if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
            var blockEntityTag = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

            if (blockEntityTag.contains(ECNames.TRAITS_HOLDER)) {
                delegate.deserializeNBT(blockEntityTag.getCompound(ECNames.TRAITS_HOLDER));
            }
        }
    }

    public void save() {
        var tag = stack.getOrCreateTag();
        var blockEntityTag = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);

        blockEntityTag.put(ECNames.TRAITS_HOLDER, delegate.serializeNBT());
        tag.put(ECNames.BLOCK_ENTITY_TAG, blockEntityTag);
    }
}
