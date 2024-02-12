package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.entity.AbstractECContainerBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.IRuneableBlockEntity;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SourceBreederBlockEntity extends AbstractECContainerBlockEntity implements IElementTypeProvider, IRuneableBlockEntity {

    private final SourceBreederItemContainer container;
    private final RuneHandler runeHandler;

    private final Map<Direction, PedestalWrapper> pedestalWrappers;
    private final int baseCost;

    public SourceBreederBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SOURCE_BREEDER, pos, state);
        runeHandler = new RuneHandler(ECConfig.COMMON.sourceBreederMaxRunes.get(), this::setChanged);
        baseCost = ECConfig.COMMON.sourceBreedingBaseCost.get();
        container = new SourceBreederItemContainer(this::setChanged);
        pedestalWrappers = new EnumMap<>(Direction.class);
        pedestalWrappers.put(Direction.NORTH, new PedestalWrapper(Direction.NORTH));
        pedestalWrappers.put(Direction.SOUTH, new PedestalWrapper(Direction.SOUTH));
        pedestalWrappers.put(Direction.WEST, new PedestalWrapper(Direction.WEST));
        pedestalWrappers.put(Direction.EAST, new PedestalWrapper(Direction.EAST));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SourceBreederBlockEntity breeder) {
        breeder.refreshPedestals();
        breeder.makeProgress();
    }

    private void refreshPedestals() {
        pedestalWrappers.forEach((d, w) -> {
            if (w.isRemoved()) {
                w.progress = 0;
                w.lookupPedestal();
            }
        });
    }

    @Nonnull
    @Override
    public Container getInventory() {
        return container;
    }

    @Override
    public ElementType getElementType() {
        var stack = container.getItem(0);

        if (!stack.is(ECTags.Items.SOURCE_SEEDS)) {
            return ElementType.NONE;
        } else if (stack.getItem() instanceof IElementTypeProvider provider) {
            return provider.getElementType();
        }
        return ElementType.NONE;
    }

    private void makeProgress() {
        var type = getElementType();
        var activeWrappers = pedestalWrappers.values().stream()
                .filter(w -> !w.isRemoved())
                .toList();

        if (!container.getItem(0).is(ECTags.Items.SOURCE_SEEDS) || activeWrappers.size() != 2 || !activeWrappers.stream().allMatch(w -> w.pedestal.hasSource() && w.getElementType() == type)) {
            resetProgress();
            return;
        }

        activeWrappers.forEach(this::transfer);

        if (activeWrappers.stream().allMatch(w -> w.progress >= w.getCost())) {
            container.setItem(0, breed(type, activeWrappers.get(0).getTraitHolder(), activeWrappers.get(1).getTraitHolder()));
            RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), 0);
            RetrieverBlock.sendOutputToRetriever(level, worldPosition.above(), getInventory(), 0);
            resetProgress();
        }
    }

    private void resetProgress() {
        pedestalWrappers.values().forEach(w -> w.progress = 0);
    }

    private void transfer(PedestalWrapper wrapper) {
        var cost = wrapper.getCost();
        var oldProgress = wrapper.progress;

        float transferAmount = Math.min(getTransferSpeed(wrapper.pedestal), cost - wrapper.progress);

        if (transferAmount > 0) {
            float preservation = runeHandler.getBonus(Rune.BonusType.ELEMENT_PRESERVATION) + wrapper.pedestal.getRuneHandler().getBonus(Rune.BonusType.ELEMENT_PRESERVATION) + 1;

            wrapper.progress = Math.round(wrapper.progress + Math.max(1, wrapper.pedestal.getElementStorage().extractElement(Math.round(transferAmount / preservation), false)) * preservation);
        }

        if (level != null && level.isClientSide && wrapper.progress > oldProgress && level.random.nextDouble() < 0.2) {
            ParticleHelper.createElementFlowParticle(wrapper.getElementType(), level, Vec3.atCenterOf(wrapper.pedestal.getBlockPos()).relative(Direction.UP, 0.4), Vec3.atCenterOf(worldPosition).relative(Direction.UP, 1.7), level.random);
        } else if (level != null && !level.isClientSide) {
            this.setChanged();
        }
    }

    private float getTransferSpeed(SourceBreederPedestalBlockEntity pedestal) {
        return ECConfig.COMMON.sourceBreederTransferSpeed.get() * (runeHandler.getBonus(Rune.BonusType.SPEED) + pedestal.getRuneHandler().getBonus(Rune.BonusType.SPEED) + 1);
    }

    private ItemStack breed(ElementType elementType, ISourceTraitHolder source1, ISourceTraitHolder source2) {
        return ReceptacleHelper.create(elementType, SourceTraitHelper.breed(level.random, runeHandler.getBonus(Rune.BonusType.LUCK), container.getItem(0).is(ECTags.Items.NATURAL_SOURCE_SEEDS), source1.getTraits(), source2.getTraits()));
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putIntArray(ECNames.PROGRESS, pedestalWrappers.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().get2DDataValue()))
                .mapToInt(e -> e.getValue().progress)
                .toArray());
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
    }

    @Override
    public void load(@Nonnull CompoundTag compound) {
        super.load(compound);
        if (compound.contains(ECNames.RUNE_HANDLER)) {
            IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
        }
    }

    public List<Direction> getPedestalsDirections() {
        return pedestalWrappers.entrySet().stream()
                .filter(e -> !e.getValue().isRemoved())
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    @Nonnull
    public IRuneHandler getRuneHandler() {
        return runeHandler;
    }

    private class PedestalWrapper implements IElementTypeProvider {

        private final Direction direction;
        private SourceBreederPedestalBlockEntity pedestal;
        private int progress;

        public PedestalWrapper(Direction direction) {
            this.direction = direction;
            this.pedestal = null;
            this.progress = 0;
        }

        public boolean isRemoved() {
            return pedestal == null || pedestal.isRemoved();
        }

        @Override
        public ElementType getElementType() {
            return isRemoved() ? ElementType.NONE : pedestal.getElementType();
        }

        public void lookupPedestal() {
            var te = level != null ? level.getBlockEntity(worldPosition.relative(direction, 2)) : null;

            pedestal = te instanceof SourceBreederPedestalBlockEntity p ? p : null;
        }

        public ISourceTraitHolder getTraitHolder() {
            return pedestal.getTraitHolder();
        }

        public float getCost() {
            return baseCost * getTraitHolder().getBreedingCost();
        }

        public void setPedestalInventory(ItemStack stack) {
            if (isRemoved()) {
                return;
            }

            pedestal.getInventory().setItem(0, stack);
            pedestal.setChanged();
        }
    }
}
