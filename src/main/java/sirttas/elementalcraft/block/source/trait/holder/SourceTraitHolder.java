package sirttas.elementalcraft.block.source.trait.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraits;

import java.util.Map;

public class SourceTraitHolder implements ISourceTraitHolder, ValueIOSerializable {

    private final Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits;

    public SourceTraitHolder() {
        traits = SourceTraits.createTraitMap();
    }

    @Override
    public Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> getTraits() {
        return traits;
    }

    public void setTraits(Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits) {
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

    @Override
    public void serialize(@NotNull ValueOutput output) {
        traits.forEach((holder, value) -> {
            if (holder.isBound()) {
                output.store(holder.getKey().identifier().toString(), holder.value().valueCodec(), value);
            }
        });
    }

    @Override
    public void deserialize(@NotNull ValueInput input) {
        traits.clear();
        for (var holder : ElementalCraftApi.SOURCE_TRAIT_MANAGER.holders().toList()) {
            if (holder.isBound()) {
                input.read(holder.getKey().identifier().toString(), holder.value().valueCodec()).ifPresent(v -> traits.put(holder, v));
            }
        }
    }
}
