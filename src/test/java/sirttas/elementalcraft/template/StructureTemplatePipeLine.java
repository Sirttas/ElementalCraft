package sirttas.elementalcraft.template;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.cover.CoverType;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static sirttas.elementalcraft.template.StructureTemplateHelper.withValue;

public class StructureTemplatePipeLine {

    private final Map<BlockPos, State> pipes;

    private StructureTemplatePipeLine(Map<BlockPos, State> pipes) {
        this.pipes = Map.copyOf(pipes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void place(StructureTemplateBuilder builder, BlockPos pos) {
        pipes.forEach((blockPos, state) -> {
            var at = pos.offset(blockPos);

            try {
                builder.set(at.getX(), at.getY(), at.getZ(), ECBlocks.PIPE_IMPROVED.get().defaultBlockState(), withValue(output -> output.putChild(ECNames.TRANSFERER, state)));
            } catch (Exception e) {
                ElementalCraftApi.LOGGER.error("Failed to place StructureTemplatePipeLine at position {}", at, e);
                throw e;
            }
        });
    }

    public void place(StructureTemplateBuilder builder) {
        place(builder, new BlockPos(0, 0, 0));
    }

    public static class Builder {

        private final Map<BlockPos, State> pipes;
        private BlockPos.MutableBlockPos pointer;

        private Builder() {
            pipes = new HashMap<>();
            pointer = BlockPos.ZERO.mutable();
        }

        public Builder lay(BlockPos target) {
            return lay(target.getX() > pointer.getX() ? Direction.EAST : Direction.WEST, Math.abs(target.getX() - pointer.getX()))
                    .lay(target.getY() > pointer.getY() ? Direction.UP : Direction.DOWN, Math.abs(target.getY() - pointer.getY()))
                    .lay(target.getZ() > pointer.getZ() ? Direction.SOUTH : Direction.NORTH, Math.abs(target.getZ() - pointer.getZ()));
        }

        public Builder lay(Direction direction) {
            return lay(direction, 1);
        }

        public Builder lay(Direction direction, int length) {
            var lastState = getLastState();

            while (length-- > 0) {
                lastState.connect(direction, ConnectionType.CONNECT);
                pointer.move(direction);
                lastState = getLastState();
                lastState.connect(direction.getOpposite(), ConnectionType.CONNECT);
            }
            return this;
        }

        public Builder branch(BlockPos to) {
            return branch(to, to);
        }

        public Builder branch(BlockPos from, BlockPos to) {
            pointer.move(from);
            return lay(to);
        }

        public Builder extract(Direction direction) {
            getLastState().connect(direction, ConnectionType.EXTRACT);
            return this;
        }

        public Builder insert(Direction direction) {
            getLastState().connect(direction, ConnectionType.INSERT);
            return this;
        }

        public StructureTemplatePipeLine build() {
            return new StructureTemplatePipeLine(pipes);
        }

        private @NonNull State getLastState() {
            return pipes.computeIfAbsent(pointer.immutable(), _ -> new State());
        }

        public Builder upgrade(Direction direction, Supplier<? extends PipeUpgradeType<?>> upgrade, Consumer<ValueOutput> output) {
            getLastState().upgrades.put(direction, new PipeUpgrade(upgrade, output));
            return this;
        }

        public Builder upgrade(Direction direction, Supplier<? extends PipeUpgradeType<?>> upgrade) {
            return upgrade(direction, upgrade, _ -> {});
        }
    }

    private static class State implements ValueIOSerializable {
        CoverType type;
        final Map<Direction, ConnectionType> connections;
        final Map<Direction, PipeUpgrade> upgrades;

        State() {
            connections  = new EnumMap<>(Direction.class);

            for (var direction : Direction.values()) {
                this.connections.put(direction, ConnectionType.NONE);
            }
            upgrades  = new EnumMap<>(Direction.class);
        }

        void connect(Direction direction, ConnectionType connectionType) {
            connections.put(direction, connectionType);
        }

        @Override
        public void serialize(@NonNull ValueOutput output) {
            connections.forEach((k, v) -> output.putString(k.getSerializedName(), v.getName()));
            upgrades.forEach((k, v) -> output.putChild(k.getSerializedName() + "_upgrade", v));
        }

        @Override
        public void deserialize(@NonNull ValueInput input) {
            throw new UnsupportedOperationException("Deserialization is not supported for StructureTemplatePipeLine.State");
        }
    }

    private record PipeUpgrade(Supplier<? extends PipeUpgradeType<?>> upgrade, Consumer<ValueOutput> output) implements ValueIOSerializable {

        @Override
        public void serialize(@NonNull ValueOutput output) {
            output.putString("id", upgrade.get().getKey().toString());
            this.output.accept(output);
        }

        @Override
        public void deserialize(@NonNull ValueInput input) {
            throw new UnsupportedOperationException("Deserialization is not supported for StructureTemplatePipeLine.PipeUpgrade");
        }
    }
}
