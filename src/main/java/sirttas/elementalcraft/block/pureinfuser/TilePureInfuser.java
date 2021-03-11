package sirttas.elementalcraft.block.pureinfuser;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pureinfuser.pedestal.TilePedestal;
import sirttas.elementalcraft.block.tile.AbstractTileECCrafting;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;
import sirttas.elementalcraft.rune.Rune.BonusType;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.handler.RuneHandler;

public class TilePureInfuser extends AbstractTileECCrafting<TilePureInfuser, PureInfusionRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static final TileEntityType<TilePureInfuser> TYPE = null;

	private final SingleItemInventory inventory;
	private final Map<Direction, Integer> progress = new EnumMap<>(Direction.class);
	private final RuneHandler runeHandler;

	public TilePureInfuser() {
		super(TYPE, PureInfusionRecipe.TYPE, ECConfig.COMMON.pureInfuserTransferSpeed.get());
		inventory = new SingleItemInventory(this::markDirty);
		runeHandler = new RuneHandler(ECConfig.COMMON.pureInfuserMaxRunes.get());
		progress.put(Direction.NORTH, 0);
		progress.put(Direction.SOUTH, 0);
		progress.put(Direction.WEST, 0);
		progress.put(Direction.EAST, 0);
	}

	@Override
	public void process() {
		super.process();
		if (this.world.isRemote) {
			ParticleHelper.createCraftingParticle(ElementType.NONE, world, Vector3d.copyCentered(pos).add(0, 0.7, 0), world.rand);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.isPowered()) {
			makeProgress();
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
		TilePedestal pedestal = getPedestal(type);

		return pedestal != null ? pedestal.getItem() : ItemStack.EMPTY;
	}

	public TilePedestal getPedestal(ElementType type) {
		TilePedestal pedestal = getPedestal(Direction.NORTH);

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
		TilePedestal pedestal = getPedestal(direction);

		if (pedestal != null) {
			pedestal.getInventory().setInventorySlotContents(0, stack);
			pedestal.markDirty();
		}
	}

	private TilePedestal getPedestal(Direction direction) {
		TileEntity te = this.hasWorld() ? this.getWorld().getTileEntity(pos.offset(direction, 3)) : null;
		return te instanceof TilePedestal ? (TilePedestal) te : null;
	}

	private void makeProgress(Direction direction) {
		TilePedestal pedestal = getPedestal(direction);
		Direction offset = direction.getOpposite();
		int oldProgress = getProgress(direction);

		if (pedestal != null) {
			float transferAmount = Math.min(getTransferSpeed(pedestal), (float) recipe.getElementAmount() - oldProgress);

			if (transferAmount > 0) {
				float preservation = runeHandler.getBonus(BonusType.ELEMENT_PRESERVATION) + pedestal.getRuneHandler().getBonus(BonusType.ELEMENT_PRESERVATION) + 1;
				float newProgress = oldProgress + pedestal.getElementStorage().extractElement(Math.round(transferAmount / preservation), false) * preservation;

				progress.put(direction, Math.round(newProgress));
				if (world.isRemote && newProgress > 0 && newProgress / transferAmount >= oldProgress / transferAmount) {
					ParticleHelper.createElementFlowParticle(pedestal.getElementType(), world, Vector3d.copyCentered(pedestal.getPos().offset(offset, 2)).add(0, 0.7, 0), offset, 2, world.rand);
				}
			}
		}
	}

	private float getTransferSpeed(TilePedestal pedestal) {
		return this.transferSpeed * (runeHandler.getBonus(BonusType.SPEED) + pedestal.getRuneHandler().getBonus(BonusType.SPEED) + 1);
	}

	public int getProgress(Direction direction) {
		return progress.getOrDefault(direction, 0);
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	public ItemStack getItem() {
		return inventory.getStackInSlot(0);
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
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		int[] progressArray = compound.getIntArray(ECNames.PROGRESS);
		
		for (int i = 0; i < progressArray.length; i++) {
			this.progress.put(Direction.byHorizontalIndex(i), progressArray[i]);
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.readNBT(runeHandler, null, compound.get(ECNames.RUNE_HANDLER));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);

		compound.putIntArray(ECNames.PROGRESS, progress.entrySet().stream().sorted(Comparator.comparingInt(e -> e.getKey().getHorizontalIndex())).mapToInt(Entry::getValue).toArray());
		compound.put(ECNames.RUNE_HANDLER, CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.writeNBT(runeHandler, null));
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}

}
