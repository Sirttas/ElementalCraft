package sirttas.elementalcraft.block.instrument.inscriber;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

public class TileInscriber extends TileInstrument<TileInscriber, InscriptionRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInscriber.NAME) public static TileEntityType<TileInscriber> TYPE;

	private final InstrumentInventory inventory;
	private boolean locked = false;

	public TileInscriber() {
		super(TYPE, InscriptionRecipe.TYPE, ECConfig.COMMON.inscriberTransferSpeed.get(), ECConfig.COMMON.inscriberMaxRunes.get());
		inventory = new InscriberInventory(this::forceSync);
	}

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
	public IInventory getInventory() {
		return inventory;
	}

	@Override
	protected boolean progressOnTick() {
		return false;
	}

	@Override
	public boolean makeProgress() {
		return super.makeProgress();
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

}
