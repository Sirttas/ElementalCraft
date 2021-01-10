package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

public class TileInfuser extends TileInstrument<TileInfuser, AbstractInfusionRecipe> {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static TileEntityType<TileInfuser> TYPE;

	private final SingleItemInventory inventory;
	private ToolInfusionRecipe toolInfusionRecipe = new ToolInfusionRecipe();

	public TileInfuser() {
		super(TYPE, AbstractInfusionRecipe.TYPE, ECConfig.COMMON.infuserTransferSpeed.get(), ECConfig.COMMON.infuserMaxRunes.get());
		inventory = new SingleItemInventory(this::forceSync);
	}

	@Override
	protected AbstractInfusionRecipe lookupRecipe() {
		return toolInfusionRecipe.matches(this) ? toolInfusionRecipe.with(this.getElementType()) : super.lookupRecipe();
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

	public ItemStack getItem() {
		return inventory.getStackInSlot(0);
	}
}
