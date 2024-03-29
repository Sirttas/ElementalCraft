package sirttas.elementalcraft.block.pipe;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgradeHelper;
import sirttas.elementalcraft.block.pipe.upgrade.priority.PipePriorityRingsPipeUpgrade;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ElementPipeTransferer implements IElementTransferer {

    private static final Collection<ElementPipeTransferer> TRANSFERERS = new ReferenceOpenHashSet<>();

    final ElementPipeBlockEntity pipe;
    final Map<Direction, ConnectionType> connections;
    final Map<Direction, PipeUpgrade> upgrades;
    final Comparator<Map.Entry<Direction, ConnectionType>> comparator;
    final int maxTransferAmount;
    private boolean initialized;
    int transferedAmount;

    ElementPipeTransferer(ElementPipeBlockEntity pipe) {
        this.pipe = pipe;
        this.initialized = pipe.getBlockState().is(ECBlocks.PIPE_CREATIVE.get());
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

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            var it = TRANSFERERS.iterator();

            while (it.hasNext()) {
                var transferer = it.next();

                if (transferer.pipe.isRemoved()) {
                    it.remove();
                } else {
                    transferer.transferedAmount = 0;
                }
            }
        }
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

        return level.getCapability(ElementalCraftCapabilities.ElementTransferer.BLOCK, pipe.getBlockPos().relative(face), opposite) instanceof ElementPipeTransferer elementPipeTransferer && elementPipeTransferer.getUpgrade(opposite) instanceof PipePriorityRingsPipeUpgrade;
    }

    public ConnectionType getConnection(Direction face) {
        return connections.getOrDefault(face, ConnectionType.NONE);
    }

    public Map<Direction, ConnectionType> getConnections() {
        return connections;
    }

    void init() {
        if (initialized) {
            return;
        }

        var level = pipe.getLevel();

        if (level == null || level.isClientSide()) {
            return;
        }
        TRANSFERERS.add(this);
        initialized = true;
    }

    @Override
    public List<IElementTransferPathNode> getConnectedNodes(@Nonnull ElementType type) {
        var level = pipe.getLevel();

        if (level == null) {
            return Collections.emptyList();
        }

        var pipePos = pipe.getBlockPos();

        return this.connections.entrySet().stream()
                .sorted(this.comparator)
                .<IElementTransferPathNode>mapMulti((entry, downstream) -> {
                    var side = entry.getKey();
                    var opposite = side.getOpposite();
                    var connection = entry.getValue();
                    var upgrade = this.getUpgrade(side);
                    var foundConnections = upgrade != null ? upgrade.getConnections(type, connection) : getDefaultPos(pipePos, side, connection);

                    if (foundConnections.isEmpty()) {
                        return;
                    }
                    foundConnections.forEach(p -> downstream.accept(createNode(level, p,  type, opposite, connection)));
                }).toList();
    }


    public IElementTransferPathNode createNode(Level level, BlockPos pos, ElementType type, Direction side, ConnectionType connection) {
        var transferer = level.getCapability(ElementalCraftCapabilities.ElementTransferer.BLOCK, pos, side);

        if (transferer instanceof ElementPipeTransferer elementPipeTransferer) {
            var upgrade = elementPipeTransferer.getUpgrade(side);

            if (upgrade != null && !upgrade.canTransfer(type, connection)) {
                transferer = null;
            }
        }

        var storage = level.getCapability(ElementalCraftCapabilities.ElementStorage.BLOCK, pos, side);

        if (storage != null && !storage.canPipeInsert(type, side)) {
            storage = null;
        }
        return new Node(pos, transferer, storage);
    }

    public static List<BlockPos> getDefaultPos(BlockPos pos, Direction face, ConnectionType connection) {
        if (connection == ConnectionType.CONNECT || connection == ConnectionType.INSERT) {
            return List.of(pos.relative(face));
        }
        return Collections.emptyList();
    }

    @Override
    public int getRemainingTransferAmount() {
        if (!this.initialized) {
            return 0;
        }

        return this.maxTransferAmount - this.transferedAmount;
    }

    @Override
    public void onTransfer(@Nonnull ElementType type, int amount, @Nullable BlockPos from, @Nullable BlockPos to) {
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
        return this.initialized && this.transferedAmount < this.maxTransferAmount && !pipe.isRemoved();
    }

    void setConnection(Direction face, ConnectionType type) {
        connections.put(face, type);
    }

    public Map<Direction, PipeUpgrade> getUpgrades() {
        return Map.copyOf(upgrades);
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
