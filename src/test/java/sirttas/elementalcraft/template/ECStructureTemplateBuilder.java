package sirttas.elementalcraft.template;

import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import net.neoforged.testframework.gametest.TemplateBuilderHelper;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ECStructureTemplateBuilder implements TemplateBuilderHelper<ECStructureTemplateBuilder> {

    private final StructureTemplateBuilder delegate;

    public ECStructureTemplateBuilder(int length, int height, int width)  {
        delegate = StructureTemplateBuilder.withSize(length, height, width);
    }

    public static ECStructureTemplateBuilder withSize(int length, int height, int width) {
        return new ECStructureTemplateBuilder(length, height, width);
    }

    public static Supplier<StructureTemplate> lazy(int length, int height, int width, UnaryOperator<ECStructureTemplateBuilder> consumer) {
        return Suppliers.memoize(() -> consumer.apply(withSize(length, height, width)).build());
    }

    public static StructureTemplate empty(int length, int height, int width) {
        return withSize(length, height, width).build();
    }

    @Override
    public ECStructureTemplateBuilder placeFloorLever(int x, int y, int z, boolean powered) {
        return TemplateBuilderHelper.super.placeFloorLever(x, y, z, powered)
                .set(x, y - 1, z, Blocks.REDSTONE_LAMP.defaultBlockState().setValue(RedstoneLampBlock.LIT, powered));
    }

    public ECStructureTemplateBuilder pipeline(int x, int y, int z, UnaryOperator<StructureTemplatePipeLine.Builder> consumer) {
        consumer.apply(StructureTemplatePipeLine.builder()).build()
                .place(this, new BlockPos(x, y, z));
        return this;
    }

    @Override
    public ECStructureTemplateBuilder set(int x, int y, int z, BlockState state, @Nullable CompoundTag nbt) {
        try {
            delegate.set(x, y, z, state, nbt);
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.error("Error setting structure template block", e);
        }
        return this;
    }

    public StructureTemplate build() {
        return delegate.build();
    }

    public StructureTemplateBuilder unpack() {
        return delegate;
    }
}
