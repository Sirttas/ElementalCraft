package sirttas.elementalcraft.block.pureinfuser;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.pureinfuser.pedestal.TilePedestal;
import sirttas.elementalcraft.block.tile.TileECCrafting;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

public class TilePureInfuser extends TileECCrafting<TilePureInfuser, PureInfusionRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static TileEntityType<TilePureInfuser> TYPE;

	private final SingleItemInventory inventory;
	private final Map<Direction, Integer> progress = new EnumMap<>(Direction.class);

	public TilePureInfuser() {
		super(TYPE, PureInfusionRecipe.TYPE, 100);
		inventory = new SingleItemInventory(this::forceSync);
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
		makeProgress();
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
			pedestal.forceSync();
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
		int transferAmount = Math.min(this.transferSpeed, recipe.getElementAmount() - oldProgress);

		if (pedestal != null && transferAmount > 0) {
			int newProgress = oldProgress + pedestal.getElementStorage().extractElement(transferAmount, false);

			progress.put(direction, newProgress);
			if (world.isRemote && newProgress / transferAmount > oldProgress / transferAmount) {
				ParticleHelper.createElementFlowParticle(pedestal.getElementType(), world, Vector3d.copyCentered(pedestal.getPos().offset(offset, 2)).add(0, 0.7, 0), offset, 2, world.rand);
			}
		}
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

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getProgress() {
		return 0;
	}
}
