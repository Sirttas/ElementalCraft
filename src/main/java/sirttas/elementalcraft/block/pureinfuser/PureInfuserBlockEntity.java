package sirttas.elementalcraft.block.pureinfuser;

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
import org.jetbrains.annotations.VisibleForTesting;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.crafting.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipeInput;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PureInfuserBlockEntity extends AbstractECCraftingBlockEntity<PureInfusionRecipeInput, PureInfusionRecipe> {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(PureInfuserBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private final SingleItemContainer inventory;
	private final Map<Direction, PedestalWrapper> pedestalWrappers;

	public PureInfuserBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.PURE_INFUSER, PROPERTIES, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
		pedestalWrappers = new EnumMap<>(Direction.class);
		pedestalWrappers.put(Direction.NORTH, new PedestalWrapper(Direction.NORTH));
		pedestalWrappers.put(Direction.SOUTH, new PedestalWrapper(Direction.SOUTH));
		pedestalWrappers.put(Direction.WEST, new PedestalWrapper(Direction.WEST));
		pedestalWrappers.put(Direction.EAST, new PedestalWrapper(Direction.EAST));
	}

	@Override
	public void process() {
		super.process();
		if (level.isClientSide) {
			ParticleHelper.createCraftingParticle(ElementType.NONE, level, Vec3.atCenterOf(worldPosition).add(0, 0.7, 0), level.random);
		}
	}

	@Override
	protected @NotNull PureInfusionRecipeInput createRecipeInput() {
		return new PureInfusionRecipeInput(
				pedestalWrappers.values().stream()
						.map(p -> p.pedestal.createRecipeInput())
						.collect(Collectors.toMap(SingleItemSingleElementRecipeInput::getElementType, Function.identity(), (i1, i2) -> i1)),
				getItem());
	}

	public static void tick(Level level, BlockPos pos, BlockState state, PureInfuserBlockEntity pureInfuser) {
		pureInfuser.refreshPedestals();

		if (!pureInfuser.isPowered()) {
			pureInfuser.makeProgress();
		}
		AbstractECCraftingBlockEntity.tick(pureInfuser);
	}

	@VisibleForTesting
	public void refreshPedestals() {
		pedestalWrappers.forEach((d, w) -> {
			if (w.isRemoved()) {
				w.lookupPedestal();
			}
		});
	}

	protected void makeProgress() {
		if (recipe != null && pedestalWrappers.values().stream().allMatch(w -> !w.isRemoved() && w.progress >= recipe.getElementAmount())) {
			process();
			resetProgress();
		} else if (this.isRecipeAvailable()) {
			pedestalWrappers.forEach(this::makeProgress);
		} else if (recipe == null) {
			resetProgress();
		}
	}

	@Override
	public boolean isRecipeAvailable() {
		if (pedestalWrappers.values().stream().anyMatch(w -> w.getElementType() == ElementType.NONE)) {
			return false;
		}

		return super.isRecipeAvailable();
	}

	private void resetProgress() {
		pedestalWrappers.values().forEach(w -> w.progress = 0);
	}

	@VisibleForTesting
	public PedestalBlockEntity getPedestal(ElementType type) {
		if (type == ElementType.NONE) {
			return null;
		}

		return pedestalWrappers.values().stream()
				.filter(w -> w.getElementType() == type)
				.map(w -> w.pedestal)
				.findFirst()
				.orElse(null);
	}

	public ElementType getPedestalElementType(Direction direction) {
		return pedestalWrappers.get(direction).getElementType();
	}

	private void makeProgress(Direction direction, PedestalWrapper wrapper) {
		if (wrapper.isRemoved()) {
			return;
		}

		var type = wrapper.getElementType();

		if (type == ElementType.NONE) {
			return;
		}

		var pedestal = wrapper.pedestal;
		var offset = direction.getOpposite();
		var oldProgress = wrapper.progress;
		var transferAmount = Math.min(getTransferSpeed(pedestal), (float) recipe.getElementAmount() - oldProgress);

		if (transferAmount <= 0) {
			return;
		}

		var preservation = runeHandler.getBonus(BonusType.ELEMENT_PRESERVATION) + pedestal.getRuneHandler().getBonus(BonusType.ELEMENT_PRESERVATION) + 1;
		var newProgress = oldProgress + pedestal.getElementStorage().extractElement(Math.max(1, Math.round(transferAmount / preservation)), false) * preservation;

		wrapper.progress = Math.round(newProgress);
		if (level != null && level.isClientSide && newProgress > 0 && getProgressRounded(transferAmount, newProgress) > getProgressRounded(transferAmount, oldProgress)) {
			ParticleHelper.createElementFlowParticle(type, level, Vec3.atCenterOf(worldPosition).add(0, 0.7, 0), offset, 2.5f, level.random);
		} else if (level != null && !level.isClientSide) {
			this.setChanged();
		}
	}

	@Override
	public void assemble() {
		inventory.setItem(0, recipe.assemble(createRecipeInput(), level.registryAccess()));
		pedestalWrappers.values().forEach(w -> w.setPedestalInventory(w.pedestal.getItem().getCraftingRemainingItem()));
	}

	private float getTransferSpeed(PedestalBlockEntity pedestal) {
		return this.getTransferSpeed() * (runeHandler.getBonus(BonusType.SPEED) + pedestal.getRuneHandler().getBonus(BonusType.SPEED) + 1);
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	public ItemStack getItem() {
		return inventory.getItem(0);
	}

	@Override
	public boolean isRunning() {
		return pedestalWrappers.values().stream().anyMatch(w -> !w.isRemoved() && w.progress > 0);
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		int[] progressArray = compound.getIntArray(ECNames.PROGRESS);

		for (int i = 0; i < progressArray.length; i++) {
			var direction = Direction.from2DDataValue(i);

			pedestalWrappers.get(direction).progress = progressArray[i];
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);

		compound.putIntArray(ECNames.PROGRESS, pedestalWrappers.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().get2DDataValue()))
				.mapToInt(e -> e.getValue().progress)
				.toArray());
	}

	private class PedestalWrapper implements IElementTypeProvider {

		private final Direction direction;
		private PedestalBlockEntity pedestal;
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
			var be = level != null ? level.getBlockEntity(worldPosition.relative(direction, 3)) : null;

			pedestal = be instanceof PedestalBlockEntity p ? p : null;
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
