package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

public class TilePureInfuser extends TileECContainer {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static TileEntityType<TilePureInfuser> TYPE;

	private float progress = 0;
	private PureInfusionRecipe recipe;
	private final SingleItemInventory inventory;

	public TilePureInfuser() {
		super(TYPE);
		inventory = new SingleItemInventory(this::forceSync);
	}

	public boolean isRunning() {
		return progress > 0;
	}

	public boolean isReciptAvalable() {
		if (recipe != null && recipe.matches(this)) {
			return true;
		}
		if (recipe != null) {
			this.forceSync();
		}
		recipe = this.getWorld().getRecipeManager().getRecipe(PureInfusionRecipe.TYPE, InventoryTileWrapper.from(this), this.getWorld()).orElse(null);
		return recipe != null;
	}

	public void process() {
		recipe.process(this);
		recipe = null;
		this.forceSync();
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
		if (recipe != null && progress >= recipe.getDuration()) {
			process();
			progress = 0;
		} else if (this.isReciptAvalable() && canConsume(Direction.NORTH) && canConsume(Direction.SOUTH) && canConsume(Direction.WEST) && canConsume(Direction.EAST)) {
			consume(Direction.NORTH);
			consume(Direction.SOUTH);
			consume(Direction.WEST);
			consume(Direction.EAST);
			progress++;
		} else if (recipe == null) {
			progress = 0;
		}
	}

	public float getProgress() {
		return progress;
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

	private boolean canConsume(Direction direction) {
		TilePedestal pedestal = getPedestal(direction);
		int elementPerTick = recipe.getElementPerTick();

		return pedestal != null && pedestal.getElementAmount() >= elementPerTick;
	}

	private void consume(Direction direction) {
		TilePedestal pedestal = getPedestal(direction);
		int elementPerTick = recipe.getElementPerTick();
		Direction offset = direction.getOpposite();

		if (pedestal != null) {
			pedestal.consumeElement(elementPerTick);
			ParticleHelper.createElementFlowParticle(pedestal.getElementType(), world, Vector3d.copyCentered(pos.offset(offset, 2)).add(0, 0.7, 0), offset, 2, world.rand);
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		progress = compound.getFloat(ECNames.PROGRESS);

	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putFloat(ECNames.PROGRESS, progress);
		return compound;
	}

	@Override
	public void clear() {
		super.clear();
		recipe = null;
		progress = 0;
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	public ItemStack getItem() {
		return inventory.getStackInSlot(0);
	}
}
