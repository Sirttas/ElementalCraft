package sirttas.elementalcraft.block.pipe.upgrade.capability;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.CapabilityRegistry;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.ApiStatus;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class PipeUpgradeCapability<T, C> extends BaseCapability<T, C> {

    private static final CapabilityRegistry<PipeUpgradeCapability<?, ?>> registry = new CapabilityRegistry<>(PipeUpgradeCapability::new);

    final Map<PipeUpgradeType<?>, List<ICapabilityProvider<PipeUpgrade, C, T>>> providers = new IdentityHashMap<>();

    private PipeUpgradeCapability(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        super(name, typeClass, contextClass);
    }

    @SuppressWarnings("unchecked")
    public static <T, C> PipeUpgradeCapability<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return (PipeUpgradeCapability<T, C>) registry.create(name, typeClass, contextClass);
    }

    public static <T> PipeUpgradeCapability<T, Void> createVoid(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, void.class);
    }

    public static <T> PipeUpgradeCapability<T, Direction> createSided(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Direction.class);
    }

    public static synchronized List<PipeUpgradeCapability<?, ?>> getAll() {
        return registry.getAll();
    }

    public <B extends ElementPipeBlockEntity> ICapabilityProvider<? super B, Direction, T> getBlockCapabilityProvider() {
        return (pipe, direction) -> {
            var upgrade = pipe.getUpgrade(direction);

            if (upgrade != null) {
                return getCapability(upgrade, null);
            }
            return null;
        };

    }

    @ApiStatus.Internal
    @Nullable
    public T getCapability(PipeUpgrade pipeUpgrade, C context) {
        for (var provider : providers.getOrDefault(pipeUpgrade.getType(), List.of())) {
            var ret = provider.getCapability(pipeUpgrade, context);
            if (ret != null)
                return ret;
        }
        return null;
    }
}
