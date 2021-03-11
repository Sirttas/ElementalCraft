package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.AbstractTileLockableInstrument;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;

public class TileInfuser extends AbstractTileLockableInstrument<IInfuser, AbstractInfusionRecipe> implements IInfuser {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static final TileEntityType<TileInfuser> TYPE = null;

	private final SingleItemInventory inventory;

	public TileInfuser() {
		super(TYPE, AbstractInfusionRecipe.TYPE, ECConfig.COMMON.infuserTransferSpeed.get(), ECConfig.COMMON.infuserMaxRunes.get());
		inventory = new SingleItemInventory(this::markDirty);
	}

	@Override
	protected AbstractInfusionRecipe lookupRecipe() {
		return this.lookupInfusionRecipe(world);
	}


	@Override
	public void process() {
		super.process();
		if (this.world.isRemote) {
			ParticleHelper.createCraftingParticle(getElementType(), world, Vector3d.copyCentered(pos), world.rand);
		}
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}
}
