package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.ElementTransfererHelper;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgradeHelper;
import sirttas.elementalcraft.block.pipe.upgrade.priority.PipePriorityRingsPipeUpgrade;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ElementPipeTransferer implements IElementTransferer {

    final ElementPipeBlockEntity pipe;
    final Map<Direction, ConnectionType> connections;
    final Map<Direction, PipeUpgrade> upgrades;
    final Comparator<Map.Entry<Direction, ConnectionType>> comparator;
    final int maxTransferAmount;

    int transferedAmount;

    ElementPipeTransferer(ElementPipeBlockEntity pipe) {
        this.pipe = pipe;
        this.connections = new EnumMap<>(Direction.class);

        for (var direction : Direction.values()) {
            this.connections.put(direction, ConnectionType.NONE);
        }

        this.upgrades = new EnumMap<>(Direction.class);
        this.comparator = creatComparator();
        this.maxTransferAmount = switch (((ElementPipeBlock) pipe.getBlockState().getBlock()).getType()) {
            case IMPAIRED -> ECConfig.COMMON.impairedPipeTransferAmount.get();
            case STANDARD -> ECConfig.COMMON.pipeTransferAmount.get();
            case IMPROVED -> ECConfig.COMMON.improvedPipeTransferAmount.get();
            case CREATIVE -> Integer.MAX_VALUE;
        };
    }

    private Comparator<Map.Entry<Direction, ConnectionType>> creatComparator() {
        Comparator<Map.Entry<Direction, ConnectionType>> cmp = (c1, c2) -> Boolean.compare(isPriority(c1.getKey()), isPriority(c2.getKey()));

        return cmp.thenComparingInt(c -> c.getValue().getValue());
    }

    public boolean isPriority(Direction face) { // TODO priority cache ?
        if (this.getUpgrade(face) instanceof PipePriorityRingsPipeUpgrade) {
            return true;
        }

        var level = pipe.getLevel();

        if (level == null) {
            return false;
        }

        var opposite = face.getOpposite();

        return BlockEntityHelper.getBlockEntity(level, this.pipe.getBlockPos().relative(face))
                .flatMap(p -> ElementTransfererHelper.get(p, opposite).resolve())
                .filter(ElementPipeTransferer.class::isInstance)
                .map(ElementPipeTransferer.class::cast)
                .map(t -> t.getUpgrade(opposite))
                .filter(PipePriorityRingsPipeUpgrade.class::isInstance)
                .isPresent();

    }

    public ConnectionType getConnection(Direction face) {
        return connections.getOrDefault(face, ConnectionType.NONE);
    }

    public Map<Direction, ConnectionType> getConnections() {
        return connections;
    }

    @Override
    public List<IElementTransferPathNode> getConnectedNodes(ElementType type) {
        var pipePos = pipe.getBlockPos();
        var level = pipe.getLevel();

        return this.connections.entrySet().stream()
                .sorted(this.comparator)
                .<IElementTransferPathNode>mapMulti((entry, downstream) -> {
                    var side = entry.getKey();
                    var connection = entry.getValue();
                    var upgrade = this.getUpgrade(side);

                    addNodes(level, upgrade != null ? upgrade.getConnections(type, connection) : getDefaultPos(pipePos, side, connection), type, side.getOpposite(), connection, downstream);
                }).toList();
    }

    private void addNodes(Level level, List<BlockPos> pos, ElementType type, Direction side, ConnectionType connection, Consumer<IElementTransferPathNode> downstream) {
        pos.forEach(p -> createNode(level, p,  type, side, connection).ifPresent(downstream));
    }

    public Optional<IElementTransferPathNode> createNode(Level level, BlockPos pos, ElementType type, Direction side, ConnectionType connection) {
        return BlockEntityHelper.getBlockEntity(level, pos).map(be -> {
            var transferer = ElementTransfererHelper.get(be, side)
                    .filter(t -> {
                        if (t instanceof ElementPipeTransferer elementPipeTransferer) {
                            var upgrade = elementPipeTransferer.getUpgrade(side);

                            return upgrade == null || upgrade.canTransfer(type, connection);
                        }
                        return true;
                    })
                    .orElse(null);
            var storage = ElementStorageHelper.get(be, side)
                    .filter(s -> s.canPipeInsert(type, side))
                    .orElse(null);

            return new Node(pos, transferer, storage);
        });
    }

    public static List<BlockPos> getDefaultPos(BlockPos pos, Direction face, ConnectionType connection) {
        if (connection == ConnectionType.CONNECT || connection == ConnectionType.INSERT) {
            return List.of(pos.relative(face));
        }
        return Collections.emptyList();
    }

    @Override
    public int getRemainingTransferAmount() {
        return this.maxTransferAmount - this.transferedAmount;
    }

    @Override
    public void transfer(ElementType type, int amount, @Nullable BlockPos from, @Nullable BlockPos to) {
        if (to != null) {
            this.connections.forEach((side, connection) -> {
                var upgrade = this.getUpgrade(side);

                if (upgrade == null || !upgrade.getConnections(type, connection).contains(to)) {
                    return;
                }
                upgrade.onTransfer(type, amount, from, to);
            });
        }
        this.transferedAmount += amount;
    }

    @Override
    public boolean isValid() {
        return this.transferedAmount < this.maxTransferAmount && !pipe.isRemoved();
    }

    void setConnection(Direction face, ConnectionType type) {
        connections.put(face, type);
    }

    public PipeUpgrade getUpgrade(Direction face) {
        return upgrades.get(face);
    }

    void setUpgrade(Direction face, PipeUpgrade upgrade) {
        if (upgrade != null) {
            upgrades.put(face, upgrade);
        }
    }

    public void removeUpgrade(Direction side) {
        this.upgrades.remove(side);
    }

    void resetTransferedAmount() {
        this.transferedAmount = 0;
    }

    void load(CompoundTag compound) {
        for (Direction face : Direction.values()) {
            this.setConnection(face, ConnectionType.fromInteger(compound.getInt(face.getSerializedName())));
            if (compound.getBoolean(face.getSerializedName() + "_priority")) { // TODO 1.20 remove
                this.setUpgrade(face, new PipePriorityRingsPipeUpgrade(pipe, face));
            }
            this.setUpgrade(face, PipeUpgradeHelper.load(pipe, face, compound.getCompound(face.getSerializedName() + "_upgrades")));
        }
    }

    CompoundTag save(CompoundTag compound) {
        connections.forEach((k, v) -> compound.putInt(k.getSerializedName(), v.getValue()));
        upgrades.forEach((k, v) -> compound.put(k.getSerializedName() + "_upgrades", v.save()));
        return compound;
    }

    public record Node(
            BlockPos pos,
            IElementTransferer transferer,
            IElementStorage storage
    ) implements IElementTransferPathNode {

        @Override
        public BlockPos getPos() {
            return pos;
        }

        @Override
        public IElementTransferer getTransferer() {
            return transferer;
        }

        @Override
        public IElementStorage getStorage() {
            return storage;
        }
    }

}
