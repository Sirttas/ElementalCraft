package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

public class PureInfuserBlockEntity extends AbstractECCraftingBlockEntity<PureInfuserBlockEntity, PureInfusionRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + PureInfuserBlock.NAME) public static final BlockEntityType<PureInfuserBlockEntity> TYPE = null;

	private final SingleItemContainer inventory;
	private final Map<Direction, Integer> progress = new EnumMap<>(Direction.class);
	private final RuneHandler runeHandler;

	public PureInfuserBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PureInfusionRecipe.TYPE, ECConfig.COMMON.pureInfuserTransferSpeed.get());
		inventory = new SingleItemContainer(this::setChanged);
		runeHandler = new RuneHandler(ECConfig.COMMON.pureInfuserMaxRunes.get(), this::setChanged);
		progress.put(Direction.NORTH, 0);
		progress.put(Direction.SOUTH, 0);
		progress.put(Direction.WEST, 0);
		progress.put(Direction.EAST, 0);
	}

	@Override
	public void process() {
		super.process();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(ElementType.NONE, level, Vec3.atCenterOf(worldPosition).add(0, 0.7, 0), level.random);
		}
	}

	public static void tick(Level level, BlockPos pos, BlockState state, PureInfuserBlockEntity pureInfuser) {
		if (!pureInfuser.isPowered()) {
			pureInfuser.makeProgress();
		}
	}

	protected void makeProgress() {
		if (recipe != null && getProgress(Direction.NORTH) >= recipe.getElementAmount() && getProgress(Direction.SOUTH) >= recipe.getElementAmount()
				&& getProgress(Direction.WEST) >= recipe.getElementAmount() && getProgress(Direction.EAST) >= recipe.getElementAmount()) {
			process();
			progress.clear();
		} else if (this.isRecipeAvailable()) {
			makeProgress(Direction.NORTH);
			makeProgress(Direction.SOUTH);
			makeProgress(Direction.WEST);
			makeProgress(Direction.EAST);
		} else if (recipe == null) {
			progress.clear();
		}
	}

	public ItemStack getStackInPedestal(ElementType type) {
		PedestalBlockEntity pedestal = getPedestal(type);

		return pedestal != null ? pedestal.getItem() : ItemStack.EMPTY;
	}

	public PedestalBlockEntity getPedestal(ElementType type) {
		PedestalBlockEntity pedestal = getPedestal(Direction.NORTH);

		if (pedestal == null || pedestal.getElementType() != type) {
			pedestal = getPedestal(Direction.SOUTH);
		}
		if (pedestal == null || pedestal.getElementType() != type) {
			pedestal = getPedestal(Direction.WEST);
		}
		if (pedestal == null || pedestal.getElementType() != type) {
			pedestal = getPedestal(Direction.EAST);
		}
		return pedestal != null && pedestal.getElementType() == type ? pedestal : null;
	}

	public void emptyPedestals() {
		setPedestalInventory(Direction.NORTH, ItemStack.EMPTY);
		setPedestalInventory(Direction.SOUTH, ItemStack.EMPTY);
		setPedestalInventory(Direction.WEST, ItemStack.EMPTY);
		setPedestalInventory(Direction.EAST, ItemStack.EMPTY);
	}

	private void setPedestalInventory(Direction direction, ItemStack stack) {
		PedestalBlockEntity pedestal = getPedestal(direction);

		if (pedestal != null) {
			pedestal.getInventory().setItem(0, stack);
			pedestal.setChanged();
		}
	}

	private PedestalBlockEntity getPedestal(Direction direction) {
		BlockEntity te = this.level != null ? this.level.getBlockEntity(worldPosition.relative(direction, 3)) : null;
		return te instanceof PedestalBlockEntity ? (PedestalBlockEntity) te : null;
	}

	private void makeProgress(Direction direction) {
		PedestalBlockEntity pedestal = getPedestal(direction);
		Direction offset = direction.getOpposite();
		int oldProgress = getProgress(direction);

		if (pedestal != null) {
			float transferAmount = Math.min(getTransferSpeed(pedestal), (float) recipe.getElementAmount() - oldProgress);

			if (transferAmount > 0) {
				float preservation = runeHandler.getBonus(BonusType.ELEMENT_PRESERVATION) + pedestal.getRuneHandler().getBonus(BonusType.ELEMENT_PRESERVATION) + 1;
				float newProgress = oldProgress + pedestal.getElementStorage().extractElement(Math.round(transferAmount / preservation), false) * preservation;

				progress.put(direction, Math.round(newProgress));
				if (level != null && level.isClientSide && newProgress > 0 && getProgressRounded(transferAmount, newProgress) > getProgressRounded(transferAmount, oldProgress)) {
					ParticleHelper.createElementFlowParticle(pedestal.getElementType(), level, Vec3.atCenterOf(worldPosition).add(0, 0.7, 0), offset, 2.5f, level.random);
				}
			}
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

	public int getProgress(Direction direction) {
		return progress.getOrDefault(direction, 0);
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	public ItemStack getItem() {
		return inventory.getItem(0);
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	public boolean isRunning() {
		return progress.values().stream().anyMatch(i -> i > 0);
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
			this.progress.put(Direction.from2DDataValue(i), progressArray[i]);
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);

		compound.putIntArray(ECNames.PROGRESS, progress.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey().get2DDataValue())).mapToInt(Entry::getValue).toArray());
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}

}
