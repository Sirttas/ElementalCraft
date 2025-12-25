package sirttas.elementalcraft.entity.ai;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.Optional;

public class ECMemoryModuleTypes {

    private static final DeferredRegister<MemoryModuleType<?>> DEFERRED_REGISTRY = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> DIG_TARGET = register("dig_target", BlockPos.CODEC);
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Float>> DIG_PROGRESS = register("dig_progress", Codec.FLOAT);
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Block>> DIG_TARGET_BLOCK = register("dig_target_state", BuiltInRegistries.BLOCK.byNameCodec());

    private ECMemoryModuleTypes() {}

    private static <U> DeferredHolder<MemoryModuleType<?>, MemoryModuleType<U>> register(String key, Codec<U> codec) {
        return DEFERRED_REGISTRY.register(key, () -> new MemoryModuleType<>(Optional.of(codec)));
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTRY.register(bus);
    }
}
