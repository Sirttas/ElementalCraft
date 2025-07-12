package sirttas.elementalcraft.block.synthesizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
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

    protected boolean working;
    private ISingleElementStorage containerCache; // TODO use capability cache

    protected AbstractSynthesizerBlockEntity(
            Supplier<? extends BlockEntityType<?>> blockEntityType,
            Holder<IConfigurableBlockEntityProperties> propertiesHolder,
            BlockPos pos,
            BlockState state) {
        super(blockEntityType, pos, state);

        var properties = getProperties(propertiesHolder);

        runeHandler = new RuneHandler(properties.maxRunes(), this::setChanged);
        transferSpeed = properties.transferSpeed();
        synthesisMultiplier = properties.synthesisMultiplier();
        range = properties.range().isBound() ? properties.range().value() : Range.DEFAULT;
        bufferElementStorage = new SingleElementStorage(properties.elementType(), properties.bufferCapacity(), this::setChanged);
        working = false;
    }

    public static void renderElementFlow(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        BlockEntityHelper.getBlockEntityAs(level, pos, AbstractSynthesizerBlockEntity.class)
                .filter(AbstractSynthesizerBlockEntity::isWorking)
                .ifPresent(synthesizer -> ParticleHelper.createElementFlowParticle(synthesizer.getElementType(), level, Vec3.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
    }

    private static @NotNull SynthesizerProperties getProperties(Holder<IConfigurableBlockEntityProperties> propertiesHolder) {
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
            working = false;
            setChanged();
            return;
        }

        var synthesized = runeHandler.handleElementTransfer(bufferElementStorage, container, type, transferSpeed);
        var hasSynthesized = synthesized > 0;

        if (hasSynthesized || working) {
            working = hasSynthesized;
            setChanged();
        }
    }

    protected abstract int synthesizeElement();

    public boolean isWorking() {
        return working;
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
    protected void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        if (compound.contains(ECNames.ELEMENT_STORAGE)) {
            bufferElementStorage.deserializeNBT(provider, compound.getCompound(ECNames.ELEMENT_STORAGE));
        }
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, Tag.OBJECT_HEADER));
        }
        working = compound.getBoolean(ECNames.WORKING);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.put(ECNames.ELEMENT_STORAGE, bufferElementStorage.serializeNBT(provider));
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
        compound.putBoolean(ECNames.WORKING, working);
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentInput input) {
        super.applyImplicitComponents(input);
        bufferElementStorage.setElementAmount(input.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0));
    }

    @Override
    protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(ECDataComponents.ELEMENT_AMOUNT, bufferElementStorage.getElementAmount());
    }

    @Override
    @Deprecated
    public void removeComponentsFromTag(@NotNull CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(ECNames.ELEMENT_STORAGE);
    }
}
