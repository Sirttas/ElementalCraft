package sirttas.elementalcraft.datagen.managed.pure.ore.loader.loader;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPureOreLoaderBuilder implements IPureOreLoaderBuilder {

    private static final AtomicInteger ORDER_INCREMENT = new AtomicInteger(0);

    protected final HolderSet<Item> source;
    protected int elementConsumption;
    protected int inputSize;
    protected int outputSize;
    protected double luckRatio;
    protected int order;

    protected AbstractPureOreLoaderBuilder(HolderSet<Item> source) {
        this.source = source;
        this.elementConsumption = 2500;
        this.inputSize = 1;
        this.outputSize = 2;
        this.luckRatio = 0;
        this.order = ORDER_INCREMENT.getAndIncrement();
    }

    public AbstractPureOreLoaderBuilder consumption(int elementConsumption) {
        this.elementConsumption = elementConsumption;
        return this;
    }

    public AbstractPureOreLoaderBuilder inputSize(int inputSize) {
        this.inputSize = inputSize;
        return this;
    }

    public AbstractPureOreLoaderBuilder outputSize(int outputSize) {
        this.outputSize = outputSize;
        return this;
    }

    public AbstractPureOreLoaderBuilder luckRatio(double luckRatio) {
        this.luckRatio = luckRatio;
        return this;
    }

    public AbstractPureOreLoaderBuilder order(int order) {
        this.order = order;
        if (order >= ORDER_INCREMENT.get()) {
            ORDER_INCREMENT.set(order + 1);
        }
        return this;
    }

}
