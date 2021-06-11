package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractLockableInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class InfuserBlockEntity extends AbstractLockableInstrumentBlockEntity<IInfuser, IInfusionRecipe> implements IInfuser {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + InfuserBlock.NAME) public static final TileEntityType<InfuserBlockEntity> TYPE = null;

	private final SingleItemInventory inventory;

	public InfuserBlockEntity() {
		super(TYPE, IInfusionRecipe.TYPE, ECConfig.COMMON.infuserTransferSpeed.get(), ECConfig.COMMON.infuserMaxRunes.get());
		inventory = new SingleItemInventory(this::setChanged);
	}

	@Override
	protected IInfusionRecipe lookupRecipe() {
		return this.lookupInfusionRecipe(level);
	}

	@Override
	protected boolean shouldRetriverExtractOutput() {
		return this.recipe == null;
	}

	@Override
	public void process() {
		super.process();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(getElementType(), level, Vector3d.atCenterOf(worldPosition), level.random);
		}
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}
}
