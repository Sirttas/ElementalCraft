package sirttas.elementalcraft;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;

public class MockRandomSource implements RandomSource {

    private final RandomSource fallback;
    private final Deque<Integer> integers;
    private final Deque<Long> longs;
    private final Deque<Boolean> booleans;
    private final Deque<Float> floats;
    private final Deque<Double> doubles;
    private final Deque<Double> gaussians;

    public MockRandomSource() {
        this(RandomSource.create());
    }

    public MockRandomSource(RandomSource fallback) {
        this.fallback = fallback;
        this.integers = new ArrayDeque<>();
        this.longs = new ArrayDeque<>();
        this.booleans = new ArrayDeque<>();
        this.floats = new ArrayDeque<>();
        this.doubles = new ArrayDeque<>();
        this.gaussians = new ArrayDeque<>();
    }

    @Override
    public @NotNull RandomSource fork() {
        return new MockRandomSource(fallback.fork());
    }

    @Override
    public @NotNull PositionalRandomFactory forkPositional() {
        return new MockPositionalRandomFactory(fallback.forkPositional());
    }

    @Override
    public void setSeed(long seed) {
        fallback.setSeed(seed);
    }

    @Override
    public int nextInt() {
        if (integers.isEmpty()) {
            return fallback.nextInt();
        }
        return integers.pop();
    }

    @Override
    public int nextInt(int bound) {
        if (integers.isEmpty()) {
            return fallback.nextInt(bound);
        }
        return integers.pop();
    }

    public MockRandomSource pushInt(int value) {
        integers.push(value);
        return this;
    }

    @Override
    public long nextLong() {
        if (longs.isEmpty()) {
            return fallback.nextLong();
        }
        return longs.pop();
    }

    public MockRandomSource pushLong(long value) {
        longs.push(value);
        return this;
    }

    @Override
    public boolean nextBoolean() {
        if (booleans.isEmpty()) {
            return fallback.nextBoolean();
        }
        return booleans.pop();
    }

    public MockRandomSource pushBoolean(boolean value) {
        booleans.push(value);
        return this;
    }

    @Override
    public float nextFloat() {
        if (floats.isEmpty()) {
            return fallback.nextFloat();
        }
        return floats.pop();
    }

    public MockRandomSource pushFloat(float value) {
        floats.push(value);
        return this;
    }

    @Override
    public double nextDouble() {
        if (doubles.isEmpty()) {
            return fallback.nextDouble();
        }
        return doubles.pop();
    }

    public MockRandomSource pushDouble(double value) {
        doubles.push(value);
        return this;
    }

    @Override
    public double nextGaussian() {
        if (gaussians.isEmpty()) {
            return fallback.nextGaussian();
        }
        return gaussians.pop();
    }

    public MockRandomSource pushGaussian(double value) {
        gaussians.push(value);
        return this;
    }

    private record MockPositionalRandomFactory(PositionalRandomFactory fallback) implements PositionalRandomFactory {

        @Override
            public @NotNull RandomSource fromHashOf(@NotNull String name) {
                return new MockRandomSource(fallback.fromHashOf(name));
            }

            @Override
            public @NotNull RandomSource fromSeed(long seed) {
                return new MockRandomSource(fallback.fromSeed(seed));
            }

            @Override
            public @NotNull RandomSource at(int x, int y, int z) {
                return new MockRandomSource(fallback.at(x, y, z));
            }

            @Override
            public void parityConfigString(@NotNull StringBuilder builder) {
                builder.append("MockPositionalRandomFactory{");
                fallback.parityConfigString(builder);
                builder.append("}");
            }
        }
}
