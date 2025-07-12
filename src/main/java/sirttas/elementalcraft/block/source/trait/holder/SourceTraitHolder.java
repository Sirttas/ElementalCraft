package sirttas.elementalcraft.block.source.trait.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.block.source.trait.SourceTraits;

import javax.annotation.Nonnull;
import java.util.Map;

public class SourceTraitHolder implements ISourceTraitHolder, INBTSerializable<CompoundTag> {

    private final Map<Holder<SourceTrait>, ISourceTraitValue> traits;

    public SourceTraitHolder() {
        traits = SourceTraits.createTraitMap();
    }

    @Override
    public Map<Holder<SourceTrait>, ISourceTraitValue> getTraits() {
        return traits;
    }

    public void setTraits(Map<Holder<SourceTrait>, ISourceTraitValue> traits) {
        this.traits.clear();
        this.traits.putAll(traits);
    }

    public void initTraits(ServerLevelAccessor level, BlockPos pos, int luck) {
        if (!traits.isEmpty()) {
            return;
        }
        
        for (var holder : ElementalCraftApi.SOURCE_TRAIT_MANAGER.holders().toList()) {
            var value = holder.value().roll(level, pos, luck);

            if (value != null) {
                traits.put(holder, value);
            }
        }
    }

    public void clear() {
        traits.clear();
    }

    public void load(@Nonnull CompoundTag compound) {
        SourceTraitHelper.loadTraits(compound, traits);
    }

    public CompoundTag save() {
        return SourceTraitHelper.saveTraits(traits);
    }

    @Override
    public void deserializeNBT(@Nonnull HolderLookup.Provider provider, @Nonnull CompoundTag compound) {
        load(compound);
    }

    @Override
    public CompoundTag serializeNBT(@Nonnull HolderLookup.Provider provider) {
        return save();
    }
}
