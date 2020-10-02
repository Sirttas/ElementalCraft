package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.BinderRecipe;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public class TileBinder extends TileInstrument {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static TileEntityType<TileBinder> TYPE;

	private final BinderInventory inventory;

	public TileBinder() {
		super(TYPE);
		inventory = new BinderInventory(this::forceSync);
	}

	public int getItemCount() {
		return inventory.getItemCount();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IInstrumentRecipe<TileBinder> lookupRecipe() {
		return this.getWorld().getRecipeManager().getRecipe(BinderRecipe.TYPE, InventoryTileWrapper.from(this), this.getWorld()).orElse(null);
	}

	@Override
	public void process() {
		super.process();
		if (this.world.isRemote) {
			ParticleHelper.createCraftingParticle(getTankElementType(), world, Vector3d.copy(pos).add(0, 0.2, 0), world.rand);
		}
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}
}
