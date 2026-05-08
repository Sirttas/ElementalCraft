package sirttas.elementalcraft.recipe.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

import java.util.stream.Stream;

public class ToolInfusionSlotDisplay implements SlotDisplay {

    public static final MapCodec<ToolInfusionSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            SlotDisplay.CODEC.fieldOf("delegate").forGetter(d -> d.delegate),
            ToolInfusion.HOLDER_CODEC.fieldOf(ECNames.TOOL_INFUSION).forGetter(d -> d.toolInfusion)
    ).apply(builder, ToolInfusionSlotDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToolInfusionSlotDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC, d -> d.delegate,
            ToolInfusion.STREAM_CODEC, d -> d.toolInfusion,
            ToolInfusionSlotDisplay::new);
    public static final Type<ToolInfusionSlotDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

    private final SlotDisplay delegate;
    private final Holder<ToolInfusion> toolInfusion;

    public ToolInfusionSlotDisplay(SlotDisplay delegate, Holder<ToolInfusion> toolInfusion) {
        this.delegate = delegate;
        this.toolInfusion = toolInfusion;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> factory) {
        return delegate.resolve(context, factory).map(s -> {
            if (s instanceof ItemStack stack) {
                var copy = stack.copy();

                ToolInfusionHelper.setInfusion(copy, toolInfusion);
                return (T) copy;
            }
            return s;
        });
    }

    @Override
    public Type<ToolInfusionSlotDisplay> type() {
        return TYPE;
    }
}
