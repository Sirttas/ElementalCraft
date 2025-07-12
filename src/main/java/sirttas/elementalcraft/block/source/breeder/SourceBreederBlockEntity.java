package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.crafting.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.source.breeding.SourceBreedingRecipe;
import sirttas.elementalcraft.recipe.source.breeding.SourceBreedingRecipeInput;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SourceBreederBlockEntity extends AbstractECCraftingBlockEntity<SourceBreedingRecipeInput, SourceBreedingRecipe> implements IElementTypeProvider {

    public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(SourceBreederBlock.NAME);
    private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    private final SourceBreederContainer container;

    private final Map<Direction, PedestalWrapper> pedestalWrappers;
    private final int baseCost;

    public SourceBreederBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SOURCE_BREEDER, PROPERTIES, pos, state);
        baseCost = ECConfig.SERVER.sourceBreedingBaseCost.get();
        container = new SourceBreederContainer(this::setChanged);
        pedestalWrappers = new EnumMap<>(Direction.class);
        pedestalWrappers.put(Direction.NORTH, new PedestalWrapper(Direction.NORTH));
        pedestalWrappers.put(Direction.SOUTH, new PedestalWrapper(Direction.SOUTH));
        pedestalWrappers.put(Direction.WEST, new PedestalWrapper(Direction.WEST));
        pedestalWrappers.put(Direction.EAST, new PedestalWrapper(Direction.EAST));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SourceBreederBlockEntity breeder) {
        breeder.refreshPedestals();

        if (!breeder.isPowered()) {
            breeder.makeProgress();
        }

        AbstractECCraftingBlockEntity.tick(breeder);
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
    public @NotNull ElementType getElementType() {
        var stack = container.getItem(0);

        if (!stack.is(ECTags.Items.SOURCE_SEEDS)) {
            return ElementType.NONE;
        }
        return ElementType.getElementType(stack);
    }

    private void makeProgress() {
        var type = getElementType();

        if (type == ElementType.NONE || !this.isRecipeAvailable()) {
            resetProgress();
            return;
        }

        var activeWrappers = getActiveWrappers();

        activeWrappers.forEach(this::transfer);

        if (activeWrappers.stream().allMatch(w -> w.progress >= w.getCost())) {
            process();
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
        return this.getTransferSpeed() * (runeHandler.getBonus(Rune.BonusType.SPEED) + pedestal.getRuneHandler().getBonus(Rune.BonusType.SPEED) + 1);
    }

    private ItemStack breed(ElementType elementType, ISourceTraitHolder source1, ISourceTraitHolder source2) {
        return ReceptacleHelper.create(elementType, SourceTraitHelper.breed(level.random, runeHandler.getBonus(Rune.BonusType.LUCK), source1.getTraits(), source2.getTraits()));
    }

    @Override
    protected void assemble() {
        var type = getElementType();
        var activeWrappers = getActiveWrappers();

        container.setItem(0, breed(type, activeWrappers.get(0).getTraitHolder(), activeWrappers.get(1).getTraitHolder()));
        for (var pedestalWrapper : activeWrappers) {
            var storage = pedestalWrapper.pedestal.getReceptacle().getCapability(ElementalCraftCapabilities.ElementStorage.ITEM);

            if (storage != null) {
                var drawn = Math.round(storage.getElementCapacity(type) * 0.25F) + 1; // TODO config

                storage.extractElement(drawn, type, false);
                if (storage.getElementAmount(type) <= 0) {
                    pedestalWrapper.setPedestalInventory(ItemStack.EMPTY);
                }
            }
        }
    }

    private @NotNull List<PedestalWrapper> getActiveWrappers() {
        return pedestalWrappers.values().stream()
                .filter(w -> !w.isEmpty())
                .toList();
    }

    @Override
    protected void retrieve() {
        RetrieverBlock.sendOutputToRetriever(level, worldPosition, getInventory(), 0);
        RetrieverBlock.sendOutputToRetriever(level, worldPosition.above(), getInventory(), 0);
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        compound.putIntArray(ECNames.PROGRESS, pedestalWrappers.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().get2DDataValue()))
                .mapToInt(e -> e.getValue().progress)
                .toArray());
        compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
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
    public boolean isRunning() {
        return getActiveWrappers().stream().anyMatch(w -> !w.isRemoved() && w.progress > 0);
    }

    @Override
    protected SourceBreedingRecipe lookupRecipe(@NotNull SourceBreedingRecipeInput recipeInput) {
        var recipe = new SourceBreedingRecipe();

        return recipe.matches(createRecipeInput(), level) ? recipe : null;
    }

    @NotNull
    @Override
    protected SourceBreedingRecipeInput createRecipeInput() {
        return new SourceBreedingRecipeInput(container.getItem(0), getElementType(),
                pedestalWrappers.values().stream()
                        .filter(w -> !w.isRemoved())
                        .map(w -> w.pedestal.createRecipeInput())
                        .toList());
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
        public @NotNull ElementType getElementType() {
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

        public boolean isEmpty() {
            return isRemoved() || pedestal.getReceptacle().isEmpty();
        }
    }
}
