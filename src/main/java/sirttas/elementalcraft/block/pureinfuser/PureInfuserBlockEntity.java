package sirttas.elementalcraft.block.pureinfuser;

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
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.entity.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

public class PureInfuserBlockEntity extends AbstractECCraftingBlockEntity<PureInfuserBlockEntity, PureInfusionRecipe> {

	private static final Config<PureInfuserBlockEntity, PureInfusionRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.PURE_INFUSER,
			ECRecipeTypes.PURE_INFUSION,
			ECConfig.COMMON.pureInfuserTransferSpeed,
			ECConfig.COMMON.pureInfuserMaxRunes
	);

	private final SingleItemContainer inventory;
	private final Map<Direction, PedestalWrapper> pedestalWrappers;

	public PureInfuserBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
		pedestalWrappers = new EnumMap<>(Direction.class);
		pedestalWrappers.put(Direction.NORTH, new PedestalWrapper());
		pedestalWrappers.put(Direction.SOUTH, new PedestalWrapper());
		pedestalWrappers.put(Direction.WEST, new PedestalWrapper());
		pedestalWrappers.put(Direction.EAST, new PedestalWrapper());
	}

	@Override
	public void process() {
		super.process();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(ElementType.NONE, level, Vec3.atCenterOf(worldPosition).add(0, 0.7, 0), level.random);
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, PureInfuserBlockEntity pureInfuser) {
		pureInfuser.refreshPedestals();

		if (!pureInfuser.isPowered()) {
			pureInfuser.makeProgress();
		}
	}

	private void refreshPedestals() {
		pedestalWrappers.forEach((d, w) -> {
			if (w.isRemoved()) {
				w.lookupPedestal(d);
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

	public ItemStack getStackInPedestal(ElementType type) {
		PedestalBlockEntity pedestal = getPedestal(type);

		return pedestal != null ? pedestal.getItem() : ItemStack.EMPTY;
	}

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

	public void emptyPedestals() {
		pedestalWrappers.values().forEach(w -> w.setPedestalInventory(ItemStack.EMPTY));
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
		inventory.setItem(0, recipe.assemble(this));
		emptyPedestals();
	}

	private float getTransferSpeed(PedestalBlockEntity pedestal) {
		return this.transferSpeed * (runeHandler.getBonus(BonusType.SPEED) + pedestal.getRuneHandler().getBonus(BonusType.SPEED) + 1);
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
	public int getProgress() {
		return 0;
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		int[] progressArray = compound.getIntArray(ECNames.PROGRESS);

		for (int i = 0; i < progressArray.length; i++) {
			var direction = Direction.from2DDataValue(i);

			pedestalWrappers.get(direction).progress = progressArray[i];
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);

		compound.putIntArray(ECNames.PROGRESS, pedestalWrappers.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getKey().get2DDataValue()))
				.mapToInt(e -> e.getValue().progress)
				.toArray());
	}

	private class PedestalWrapper implements IElementTypeProvider {

		private PedestalBlockEntity pedestal;
		private int progress;

		public PedestalWrapper() {
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

		public void lookupPedestal(Direction direction) {
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
