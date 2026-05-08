package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.range.Ranges;

import java.util.concurrent.CompletableFuture;

public class RangesProvider extends AbstractManagedDataBuilderProvider<Range, Range.Builder> {

    public RangesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, ElementalCraftApi.RANGE_MANAGER, Range.Builder.CODEC);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        add(Ranges.BOX_RADIUS_10, Range.builder().box(10));

        add(Ranges.BELOW, Range.builder().box(0, -1, 0, 1, 0, 1));
        add(Ranges.ABOVE, Range.builder().box(0, 1, 0, 1, 2, 1));
        add(Ranges.NORTH, Range.builder().box(0, 0, -1, 1, 1, 0));
        add(Ranges.SOUTH, Range.builder().box(0, 0, 1, 1, 1, 2));
        add(Ranges.WEST, Range.builder().box(-1, 0, 0, 0, 1, 1));
        add(Ranges.EAST, Range.builder().box(1, 0, 0, 2, 1, 1));

        add(Ranges.DIFFUSER, Range.withParent(Ranges.BOX_RADIUS_10));
    }

    @Override
    public String getName() {
        return "ElementalCraft Ranges";
    }
}
