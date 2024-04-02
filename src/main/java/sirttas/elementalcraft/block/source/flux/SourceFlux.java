package sirttas.elementalcraft.block.source.flux;

import net.minecraft.nbt.FloatTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.Nonnull;
import java.util.Comparator;

public class SourceFlux implements INBTSerializable<FloatTag> {

    public static final Comparator<? super SourceFlux> COMPARATOR = Comparator.comparingDouble(s -> s.flux);

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

    void recover() {
        var amount = config.recovery() * (1 - this.flux / config.capacity());

        this.flux = Math.min(config.capacity(), this.flux + amount);
    }

    void transfer(SourceFlux other) {
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

    void afterTransfers() {
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
}
