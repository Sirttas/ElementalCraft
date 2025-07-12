package sirttas.elementalcraft.block.pipe;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.api.element.transfer.path.IElementTransferPathNode;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgradeHelper;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ElementPipeTransferer implements IElementTransferer, INBTSerializable<CompoundTag> {

    private static final Collection<ElementPipeTransferer> TRANSFERERS = new ReferenceOpenHashSet<>();

    final ElementPipeBlockEntity pipe;
    final Map<Direction, ConnectionType> connections;
    final Map<Direction, PipeUpgrade> upgrades;
    final int maxTransferAmount;
    private boolean initialized;
    int transferedAmount;

    ElementPipeTransferer(ElementPipeBlockEntity pipe) {
        this.pipe = pipe;
        this.initialized = false;
        this.connections = new EnumMap<>(Direction.class);

        for (var direction : Direction.values()) {
            this.connections.put(direction, ConnectionType.NONE);
        }

        this.upgrades = new EnumMap<>(Direction.class);
        this.maxTransferAmount = switch (((ElementPipeBlock) pipe.getBlockState().getBlock()).getType()) {
            case RUDIMENTARY -> ECConfig.SERVER.rudimentaryPipeTransferAmount.get();
            case STANDARD -> ECConfig.SERVER.pipeTransferAmount.get();
            case IMPROVED -> ECConfig.SERVER.improvedPipeTransferAmount.get();
            case CREATIVE -> Integer.MAX_VALUE;
        };
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
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

    public int getUpgradeWeight(Direction face) {
        var upgrade = this.getUpgrade(face);

        if (upgrade == null) {
            return 0;
        }
        return upgrade.getWeight();
    }

    public ConnectionType getConnection(Direction face) {
        return connections.getOrDefault(face, ConnectionType.NONE);
    }

    public Map<Direction, ConnectionType> getConnections() {
        return connections;
    }

    synchronized void init() {
        if (initialized) {
            return;
        }

        var level = pipe.getLevel();

        if (level == null || level.isClientSide()) {
            return;
        }
        if (!pipe.getBlockState().is(ECBlocks.PIPE_CREATIVE.get())) { // Creative pipes don't need to be ticked
            TRANSFERERS.add(this);
        }
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
                .<IElementTransferPathNode>mapMulti((entry, downstream) -> {
                    var side = entry.getKey();
                    var opposite = side.getOpposite();
                    var connection = entry.getValue();
                    var upgrade = this.getUpgrade(side);
                    var foundConnections = upgrade != null ? upgrade.getConnections(type, connection) : getDefaultPos(pipePos, side, connection);

                    if (foundConnections.isEmpty()) {
                        return;
                    }
                    foundConnections.forEach(p -> downstream.accept(createNode(level, p, type, opposite, connection)));
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
    public void onTransfer(@Nonnull ElementType type, int amount, @Nullable IElementTransferPathNode prev, @Nullable IElementTransferPathNode next) {
        getInvolvedUpgrades(type, prev, next).forEach(upgrade -> upgrade.onTransfer(type, amount, prev, next));
        this.transferedAmount += amount;
    }

    private List<PipeUpgrade> getInvolvedUpgrades(@Nonnull ElementType type, @Nullable IElementTransferPathNode prev, @Nullable IElementTransferPathNode next) {
        var list = new ArrayList<PipeUpgrade>(this.connections.size());

        this.connections.forEach((side, connection) -> {
            var upgrade = this.getUpgrade(side);

            if (upgrade != null && (isUpgradeConnectedTo(upgrade, prev, type, connection) || isUpgradeConnectedTo(upgrade, next, type, connection))) {
                list.add(upgrade);
            }
        });
        return List.copyOf(list);
    }

    private static boolean isUpgradeConnectedTo(PipeUpgrade upgrade, @Nullable IElementTransferPathNode to, @NotNull ElementType type, ConnectionType connection) {
        if (to == null) {
            return false;
        }
        return upgrade.getConnections(type, connection).contains(to.getPos());
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

    @Override
    public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull CompoundTag compound) {
        for (Direction face : Direction.values()) {
            this.setConnection(face, ConnectionType.byName(compound.getString(face.getSerializedName())));
            this.setUpgrade(face, PipeUpgradeHelper.load(pipe, face, compound.getCompound(face.getSerializedName() + "_upgrade"), provider));
        }
    }

    @Override
    public CompoundTag serializeNBT(@NotNull HolderLookup.Provider provider) {
        var compound = new CompoundTag();

        connections.forEach((k, v) -> compound.putString(k.getSerializedName(), v.getName()));
        upgrades.forEach((k, v) -> compound.put(k.getSerializedName() + "_upgrade", v.save(provider)));
        return compound;
    }

    void copyTo(ElementPipeTransferer transferer) {
        for (Direction face : Direction.values()) {
            transferer.setConnection(face, this.getConnection(face));
            transferer.setUpgrade(face, this.getUpgrade(face));
        }
        transferer.transferedAmount = this.transferedAmount;
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

        @Override
        public int getWeight(@NotNull ElementType type, @Nullable IElementTransferPathNode prev, @Nullable IElementTransferPathNode next) {
            if (!(transferer instanceof ElementPipeTransferer pipeTransferer)) {
                return 1;
            }
            return 1 + pipeTransferer.getInvolvedUpgrades(type, prev, next).stream()
                    .mapToInt(PipeUpgrade::getWeight)
                    .sum();
        }
    }

}
