package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.BindingRecipe;

public class TileBinder extends TileInstrument<IBinder, BindingRecipe> implements IBinder {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static TileEntityType<TileBinder> TYPE;

	private final InstrumentInventory inventory;
	private boolean locked = false;

	public TileBinder() {
		this(TYPE);
	}

	public TileBinder(TileEntityType<?> tileEntityType) {
		this(tileEntityType, ECConfig.COMMON.binderTransferSpeed.get(), ECConfig.COMMON.binderMaxRunes.get());
	}

	protected TileBinder(TileEntityType<?> tileEntityType, int transferSpeed, int maxRunes) {
		super(tileEntityType, BindingRecipe.TYPE, transferSpeed, maxRunes);
		inventory = new InstrumentInventory(this::markDirty, 10);
	}

	@Override
	public int getItemCount() {
		return inventory.getItemCount();
	}

	@Override
	public void process() {
		super.process();
		if (this.world.isRemote) {
			ParticleHelper.createCraftingParticle(getElementType(), world, Vector3d.copyCentered(pos).add(0, 0.2, 0), world.rand);
		}
		locked = true;
	}

	@Override
	protected void onProgress() {
		if (world.isRemote) {
			ParticleHelper.createElementFlowParticle(getElementType(), world, Vector3d.copyCentered(pos).add(0, 0.2D, 0), Direction.UP, 1, world.rand);
		}
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	@Override
	public void tick() {
		super.tick();
		if (locked && inventory.getStackInSlot(0).isEmpty()) {
			locked = false;
		}
	}

	public boolean isLocked() {
		return locked;
	}

	@Override
	public boolean canSorterInsert() {
		return !locked && super.canSorterInsert();
	}
}
