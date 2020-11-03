package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

public class TileInfuser extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static TileEntityType<TileInfuser> TYPE;

	private final SingleItemInventory inventory;
	private ToolInfusionRecipe toolInfusionRecipe = new ToolInfusionRecipe();

	public TileInfuser() {
		super(TYPE);
		inventory = new SingleItemInventory(this::forceSync);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IInstrumentRecipe<TileInfuser> lookupRecipe() {
		return toolInfusionRecipe.matches(this) ? toolInfusionRecipe.with(this.getTankElementType())
				: this.getWorld().getRecipeManager().getRecipe(AbstractInfusionRecipe.TYPE, InventoryTileWrapper.from(this), this.getWorld()).orElse(null);
	}


	@Override
	public void process() {
		super.process();
		if (this.world.isRemote) {
			ParticleHelper.createCraftingParticle(getTankElementType(), world, Vector3d.copyCentered(pos), world.rand);
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
