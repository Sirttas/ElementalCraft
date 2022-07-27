package sirttas.elementalcraft.api.source.trait.holder;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilitySourceTraitHolder {

    public static final Capability<ISourceTraitHolder> SOURCE_TRAIT_HOLDER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    private CapabilitySourceTraitHolder() {}

    @Nonnull
    public static LazyOptional<ISourceTraitHolder> get(ICapabilityProvider provider) {
        return get(provider, null);
    }

    @Nonnull
    public static LazyOptional<ISourceTraitHolder> get(ICapabilityProvider provider, Direction side) {
        return SOURCE_TRAIT_HOLDER_CAPABILITY != null && provider != null ? provider.getCapability(SOURCE_TRAIT_HOLDER_CAPABILITY, side) : LazyOptional.empty();
    }

    @Nullable
    public static <T extends Tag, S extends ISourceTraitHolder & INBTSerializable<T>> ICapabilityProvider createProvider(S holder) {
        return SOURCE_TRAIT_HOLDER_CAPABILITY != null ? new CapabilityProvider<>(holder) : null;
    }

    private record CapabilityProvider<T extends Tag, S extends ISourceTraitHolder & INBTSerializable<T>>(S holder) implements ICapabilitySerializable<T> {

        @Override
        public <U> @NotNull LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
            return SOURCE_TRAIT_HOLDER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> holder));
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
