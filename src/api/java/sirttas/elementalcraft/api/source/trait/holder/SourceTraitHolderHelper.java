package sirttas.elementalcraft.api.source.trait.holder;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceTraitHolderHelper {

    private SourceTraitHolderHelper() {}

    @Nonnull
    public static LazyOptional<ISourceTraitHolder> get(ICapabilityProvider provider) {
        return get(provider, null);
    }

    @Nonnull
    public static LazyOptional<ISourceTraitHolder> get(ICapabilityProvider provider, Direction side) {
        return ElementalCraftCapabilities.SOURCE_TRAIT_HOLDER != null && provider != null ? provider.getCapability(ElementalCraftCapabilities.SOURCE_TRAIT_HOLDER, side) : LazyOptional.empty();
    }

    @Nullable
    public static <T extends Tag, S extends ISourceTraitHolder & INBTSerializable<T>> ICapabilityProvider createProvider(S holder) {
        return ElementalCraftCapabilities.SOURCE_TRAIT_HOLDER != null ? new CapabilityProvider<>(holder) : null;
    }

    private record CapabilityProvider<T extends Tag, S extends ISourceTraitHolder & INBTSerializable<T>>(S holder) implements ICapabilitySerializable<T> {

        @Override
        public <U> @NotNull LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
            return ElementalCraftCapabilities.SOURCE_TRAIT_HOLDER.orEmpty(cap, LazyOptional.of(() -> holder));
        }

        @Override
        public T serializeNBT() {
            return holder.serializeNBT();
        }

        @Override
        public void deserializeNBT(T nbt) {
            holder.deserializeNBT(nbt);
        }
    }
}
