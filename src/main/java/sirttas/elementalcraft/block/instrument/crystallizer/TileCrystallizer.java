package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.block.instrument.AbstractTileLockableInstrument;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

public class TileCrystallizer extends AbstractTileLockableInstrument<TileCrystallizer, CrystallizationRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCrystallizer.NAME) public static final TileEntityType<TileCrystallizer> TYPE = null;

	private final InstrumentInventory inventory;

	public TileCrystallizer() {
		super(TYPE, CrystallizationRecipe.TYPE, ECConfig.COMMON.crystallizerTransferSpeed.get(), ECConfig.COMMON.crystallizerMaxRunes.get());
		inventory = new CrystallizerInventory(this::markDirty);
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
}
