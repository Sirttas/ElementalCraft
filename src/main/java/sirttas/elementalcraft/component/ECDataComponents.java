package sirttas.elementalcraft.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.source.trait.holder.ItemSourceTraitHolder;
import sirttas.elementalcraft.element.ElementAmounts;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellList;
import sirttas.elementalcraft.spell.Spells;

import java.util.function.UnaryOperator;

public class ECDataComponents {

    private static final DeferredRegister<@NotNull DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ElementalCraftApi.MODID);

    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull BlockPos>> TARGET_ANCHOR = register(ECNames.TARGET_ANCHOR, b -> b
            .persistent(BlockPos.CODEC)
            .networkSynchronized(BlockPos.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull BlockPos>> TARGET_POS = register(ECNames.TARGET_POS, b -> b
            .persistent(BlockPos.CODEC)
            .networkSynchronized(BlockPos.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Holder<@NotNull ToolInfusion>>> TOOL_INFUSION = ElementalCraftApi.TOOL_INFUSION_MANAGER.registerComponentType(DEFERRED_REGISTER);
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Holder<@NotNull Rune>>> RUNE = ElementalCraftApi.RUNE_MANAGER.registerComponentType(DEFERRED_REGISTER);
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull ElementType>> ELEMENT_TYPE = register(ECNames.ELEMENT_TYPE, b -> b
            .persistent(ElementType.CODEC)
            .networkSynchronized(ElementType.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Integer>> ELEMENT_AMOUNT = register(ECNames.ELEMENT_AMOUNT, b -> b
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull ElementAmounts>> ELEMENT_AMOUNTS = register("element_amounts", b -> b
            .persistent(ElementAmounts.CODEC)
            .networkSynchronized(ElementAmounts.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull CustomData>> PIPE_UPGRADE_DATA = register("pipe_upgrade_data", b -> b
            .persistent(CustomData.CODEC)
            .networkSynchronized(CustomData.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Identifier>> PURE_ORE = register("pure_ore", b -> b
            .persistent(Identifier.CODEC)
            .networkSynchronized(Identifier.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Jewel>> JEWEL = register(ECNames.JEWEL, b -> b
            .persistent(Jewels.REGISTRY.byNameCodec())
            .networkSynchronized(ByteBufCodecs.registry(Jewels.REGISTRY_KEY)));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Holder<@NotNull Spell>>> SPELL = register(ECNames.SPELL, b -> b
            .persistent(Spells.REGISTRY.holderByNameCodec())
            .networkSynchronized(ByteBufCodecs.holderRegistry(Spells.REGISTRY_KEY)));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull SpellList>> SPELL_LIST = register(ECNames.SPELL_LIST, b -> b
            .persistent(SpellList.CODEC)
            .networkSynchronized(SpellList.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull ItemSourceTraitHolder>> SOURCE_TRAITS_HOLDER = register(ECNames.SOURCE_TRAITS_HOLDER, b -> b
            .persistent(ItemSourceTraitHolder.CODEC)
            .networkSynchronized(ItemSourceTraitHolder.STREAM_CODEC));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Boolean>> SOURCE_ANALYZED = register("source_analyzed", b -> b
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL));
    public static final DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull Integer>> AIR_MILL_DAMAGE = register("air_mill_damage", b -> b
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT));

    private ECDataComponents() { }

    private static <T> DeferredHolder<@NotNull DataComponentType<?>, @NotNull DataComponentType<@NotNull T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> factory) {
        return DEFERRED_REGISTER.register(name, () -> factory.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
