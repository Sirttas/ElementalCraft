package sirttas.elementalcraft.block.synthesizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;
import sirttas.elementalcraft.container.IRuneableBlockEntity;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.range.RangeHelper;

import javax.annotation.Nonnull;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class AbstractSynthesizerBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity, IRuneableBlockEntity, IElementStorageBlocKEntity, IElementTypeProvider {

    protected final RuneHandler runeHandler;
    protected final int transferSpeed;
    protected final float synthesisMultiplier;
    protected final Range range;
    protected final SingleElementStorage bufferElementStorage;

    protected boolean running; // TODO rename to running
    private ISingleElementStorage containerCache; // TODO use capability cache

    protected AbstractSynthesizerBlockEntity(
            Supplier<? extends BlockEntityType<?>> blockEntityType,
            Holder<@NotNull IConfigurableBlockEntityProperties> propertiesHolder,
            BlockPos pos,
            BlockState state) {
        super(blockEntityType, pos, state);

        var properties = getProperties(propertiesHolder);

        runeHandler = new RuneHandler(properties.maxRunes(), this::setChanged);
        transferSpeed = properties.transferSpeed();
        synthesisMultiplier = properties.synthesisMultiplier();
        range = properties.range().isBound() ? properties.range().value() : Range.DEFAULT;
        bufferElementStorage = new SingleElementStorage(properties.elementType(), properties.bufferCapacity(), this::setChanged);
        running = false;
    }

    public static void renderElementFlow(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        BlockEntityHelper.getBlockEntityAs(level, pos, AbstractSynthesizerBlockEntity.class)
                .filter(AbstractSynthesizerBlockEntity::isRunning)
                .ifPresent(synthesizer -> ParticleHelper.createElementFlowParticle(synthesizer.getElementType(), level, Vec3.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
    }

    private static @NotNull SynthesizerProperties getProperties(Holder<@NotNull IConfigurableBlockEntityProperties> propertiesHolder) {
        if (!propertiesHolder.isBound()) {
            throw new IllegalStateException("Properties not bound");
        }

        var value = propertiesHolder.value();

        if (!(value instanceof SynthesizerProperties properties)) {
            throw new IllegalStateException("Invalid properties type");
        }
        return properties;
    }

    protected void handleSynthesis() {
        var container = getContainer();
        var type = getElementType();

        if (container == null || type == ElementType.NONE) {
            return;
        } else if (bufferElementStorage.getElementAmount() < transferSpeed) {
            bufferElementStorage.insertElement(synthesizeElement(), false);
            setChanged();
        }

        if (bufferElementStorage.getElementAmount() <= 0) {
            running = false;
            setChanged();
            return;
        }

        var synthesized = runeHandler.handleElementTransfer(bufferElementStorage, container, type, transferSpeed);
        var hasSynthesized = synthesized > 0;

        if (hasSynthesized || running) {
            running = hasSynthesized;
            setChanged();
        }
    }

    protected abstract int synthesizeElement();

    public boolean isRunning() {
        return running;
    }

    public AABB getRange() {
        return this.runeHandler.getRange(this.range).move(this.worldPosition);
    }

    public Stream<BlockPos> getBlocksInRange() {
        return RangeHelper.getBlocksInAABB(getRange());
    }

    @NotNull
    @Override
    public ElementType getElementType() {
        return bufferElementStorage.getElementType();
    }

    @Override
    public IElementStorage getElementStorage() {
        return bufferElementStorage;
    }

    @Override
    public ISingleElementStorage getContainer() {
        if (containerCache == null) {
            containerCache = IContainerTopBlockEntity.super.getContainer();
        }
        return containerCache;
    }

    @Override
    public RuneHandler getRuneHandler() {
        return runeHandler;
    }

    @Override
    protected void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        input.readChild(ECNames.ELEMENT_STORAGE, bufferElementStorage);
        input.readChild(ECNames.RUNE_HANDLER, runeHandler);
        running = input.getBooleanOr(ECNames.RUNNING, false);
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput output) {
        super.saveAdditional(output);
        output.putChild(ECNames.ELEMENT_STORAGE, bufferElementStorage);
        output.putChild(ECNames.RUNE_HANDLER, runeHandler);
        output.putBoolean(ECNames.RUNNING, running);
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter getter) {
        super.applyImplicitComponents(getter);
        bufferElementStorage.setElementAmount(getter.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0));
    }

    @Override
    protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(ECDataComponents.ELEMENT_AMOUNT, bufferElementStorage.getElementAmount());
    }

    @Override
    @Deprecated
    public void removeComponentsFromTag(@NotNull ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(ECNames.ELEMENT_STORAGE);
    }
}
