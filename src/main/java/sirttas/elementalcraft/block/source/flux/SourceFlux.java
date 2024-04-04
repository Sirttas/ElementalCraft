package sirttas.elementalcraft.block.source.flux;

import net.minecraft.nbt.FloatTag;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SourceFlux implements INBTSerializable<FloatTag> {

    private final SourceFluxConfig config;
    private final int x;
    private final int z;
    private float flux;
    private float fluxReceived;


    public SourceFlux(SourceFluxConfig config, int x, int z) {
        this.config = config;
        this.x = x;
        this.z = z;
        this.flux = config.capacity();
        fluxReceived = 0;
    }

    public float getRatio() {
        return Math.max(0.1F, Math.min(this.flux / config.capacity(), 1F));
    }

    public void consume() {
        this.flux = Math.max(0, this.flux - config.consumption());
    }

    void tick(NeighborSupplier neighbors) {
        recover();
        neighbors.getNeighbor(x, z).forEach(this::transfer);
        afterTransfers();
    }

    private void recover() {
        var amount = config.recovery() * (1 - this.flux / config.capacity());

        this.flux = Math.min(config.capacity(), this.flux + amount);
    }

    private void transfer(SourceFlux other) {
        if (other.flux >= this.flux) {
            return;
        }

        var amount = config.transfer() * (this.flux - other.flux) / config.capacity();

        if (amount <= 0.1F) {
            return;
        }
        other.fluxReceived += amount;
        this.flux = Math.max(0, this.flux - amount);
    }

    private void afterTransfers() {
        this.flux = Math.min(config.capacity(), this.flux + fluxReceived);
        fluxReceived = 0;
    }

    boolean isNeighbor(SourceFlux other) {
        return (Math.abs(x - other.x) + Math.abs(z - other.z)) <= 1;
    }

    @Override
    public @Nonnull FloatTag serializeNBT() {
        return FloatTag.valueOf(flux);
    }

    @Override
    public void deserializeNBT(FloatTag nbt) {
        flux = nbt.getAsFloat();
    }

    @VisibleForTesting
    public int getX() {
        return x;
    }

    @VisibleForTesting
    public int getY() {
        return z;
    }

    @FunctionalInterface
    public interface NeighborSupplier {
        List<SourceFlux> getNeighbor(int x, int z);

        static NeighborSupplier of(Map<Long, SourceFlux> map) {
            return (x, z) -> Stream.of(
                            map.get(ChunkPos.asLong(x - 1, z)),
                            map.get(ChunkPos.asLong(x + 1, z)),
                            map.get(ChunkPos.asLong(x, z - 1)),
                            map.get(ChunkPos.asLong(x, z + 1))
                    )
                    .filter(s -> s != null && s.isNeighbor(map.get(ChunkPos.asLong(x, z))))
                    .toList();
        }
    }
}
