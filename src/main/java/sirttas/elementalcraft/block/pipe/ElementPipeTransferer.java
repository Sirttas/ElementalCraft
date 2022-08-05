package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.config.ECConfig;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class ElementPipeTransferer implements IElementTransferer {

    final BlockEntity pipe;
    final Map<Direction, ConnectionType> connections;
    final Map<Direction, Boolean> priorities;
    final Comparator<Map.Entry<Direction, ConnectionType>> comparator;
    final int maxTransferAmount;

    int transferedAmount;

    ElementPipeTransferer(BlockEntity pipe) {
        this.pipe = pipe;
        this.connections = new EnumMap<>(Direction.class);
        this.priorities = new EnumMap<>(Direction.class);
        this.comparator = creatComparator();
        this.maxTransferAmount = switch (((ElementPipeBlock) pipe.getBlockState().getBlock()).getType()) {
            case IMPAIRED -> ECConfig.COMMON.impairedPipeTransferAmount.get();
            case STANDARD -> ECConfig.COMMON.pipeTransferAmount.get();
            case IMPROVED -> ECConfig.COMMON.improvedPipeTransferAmount.get();
        };
    }

    private Comparator<Map.Entry<Direction, ConnectionType>> creatComparator() {
        Comparator<Map.Entry<Direction, ConnectionType>> cmp = (c1, c2) -> Boolean.compare(isPriority(c2.getKey()), isPriority(c1.getKey()));

        return cmp.thenComparing((c1, c2) -> c2.getValue().getValue() - c1.getValue().getValue());
    }

    public boolean isPriority(Direction face) {
        return Boolean.TRUE.equals(priorities.get(face));
    }

    @Override
    public ConnectionType getConnection(Direction face) {
        return connections.getOrDefault(face, ConnectionType.NONE);
    }

    @Override
    public Map<Direction, ConnectionType> getConnections() {
        return connections;
    }

    @Override
    public Stream<Map.Entry<Direction, ConnectionType>> getConnectionStream() {
        return this.connections.entrySet().stream().filter(entry -> {
            ConnectionType connection = entry.getValue();

            return connection == ConnectionType.CONNECT || connection == ConnectionType.INSERT;
        }).sorted(this.comparator);
    }

    @Override
    public int getRemainingTransferAmount() {
        return this.maxTransferAmount - this.transferedAmount;
    }

    @Override
    public void transfer(int amount) {
        this.transferedAmount += amount;
    }

    @Override
    public boolean isValid() {
        return this.transferedAmount < this.maxTransferAmount && !pipe.isRemoved();
    }

    void setConnection(Direction face, ConnectionType type) {
        connections.put(face, type);
        if (!type.isConnected() && isPriority(face)) {
            this.setPriority(face, false);
        }
    }

    void setPriority(Direction face, boolean value) {
        priorities.put(face, value);
    }

    void resetTransferedAmount() {
        this.transferedAmount = 0;
    }

    void load(CompoundTag compound) {
        for (Direction face : Direction.values()) {
            this.setConnection(face, ConnectionType.fromInteger(compound.getInt(face.getSerializedName())));
            this.setPriority(face, compound.getBoolean(face.getSerializedName() + "_priority"));
        }
    }

    CompoundTag save(CompoundTag compound) {
        connections.forEach((k, v) -> compound.putInt(k.getSerializedName(), v.getValue()));
        priorities.forEach((k, v) -> compound.putBoolean(k.getSerializedName() + "_priority", v));
        return compound;
    }
}
