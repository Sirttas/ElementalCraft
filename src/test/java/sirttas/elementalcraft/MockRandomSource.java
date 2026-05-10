package sirttas.elementalcraft;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

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

    private Behavior behavior;

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
        this.behavior = Behavior.FIFO;
    }

    public MockRandomSource fifo() {
        this.behavior = Behavior.FIFO;
        return this;
    }

    public MockRandomSource lifo() {
        this.behavior = Behavior.LIFO;
        return this;
    }

    @Override
    public RandomSource fork() {
        return new MockRandomSource(fallback.fork());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
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
        return this.behavior == Behavior.FIFO ? integers.removeFirst() : integers.removeLast();
    }

    @Override
    public int nextInt(int bound) {
        if (integers.isEmpty()) {
            return fallback.nextInt(bound);
        }
        return this.behavior == Behavior.FIFO ? integers.removeFirst() : integers.removeLast();
    }

    public MockRandomSource pushInt(int value) {
        integers.addLast(value);
        return this;
    }

    @Override
    public long nextLong() {
        if (longs.isEmpty()) {
            return fallback.nextLong();
        }
        return this.behavior == Behavior.FIFO ? longs.removeFirst() : longs.removeLast();
    }

    public MockRandomSource pushLong(long value) {
        longs.addLast(value);
        return this;
    }

    @Override
    public boolean nextBoolean() {
        if (booleans.isEmpty()) {
            return fallback.nextBoolean();
        }
        return this.behavior == Behavior.FIFO ? booleans.removeFirst() : booleans.removeLast();
    }

    public MockRandomSource pushBoolean(boolean value) {
        booleans.addLast(value);
        return this;
    }

    @Override
    public float nextFloat() {
        if (floats.isEmpty()) {
            return fallback.nextFloat();
        }
        return this.behavior == Behavior.FIFO ? floats.removeFirst() : floats.removeLast();
    }

    public MockRandomSource pushFloat(float value) {
        floats.addLast(value);
        return this;
    }

    @Override
    public double nextDouble() {
        if (doubles.isEmpty()) {
            return fallback.nextDouble();
        }
        return this.behavior == Behavior.FIFO ? doubles.removeFirst() : doubles.removeLast();
    }

    public MockRandomSource pushDouble(double value) {
        doubles.addLast(value);
        return this;
    }

    @Override
    public double nextGaussian() {
        if (gaussians.isEmpty()) {
            return fallback.nextGaussian();
        }
        return this.behavior == Behavior.FIFO ? gaussians.removeFirst() : gaussians.removeLast();
    }

    public MockRandomSource pushGaussian(double value) {
        gaussians.addLast(value);
        return this;
    }

    public enum Behavior {
        FIFO, LIFO
    }

    private record MockPositionalRandomFactory(PositionalRandomFactory fallback) implements PositionalRandomFactory {

        @Override
            public RandomSource fromHashOf(String name) {
                return new MockRandomSource(fallback.fromHashOf(name));
            }

            @Override
            public RandomSource fromSeed(long seed) {
                return new MockRandomSource(fallback.fromSeed(seed));
            }

            @Override
            public RandomSource at(int x, int y, int z) {
                return new MockRandomSource(fallback.at(x, y, z));
            }

            @Override
            public void parityConfigString(StringBuilder builder) {
                builder.append("MockPositionalRandomFactory{");
                fallback.parityConfigString(builder);
                builder.append("}");
            }
        }
}
