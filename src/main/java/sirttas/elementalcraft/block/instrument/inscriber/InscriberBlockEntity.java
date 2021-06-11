package sirttas.elementalcraft.block.instrument.inscriber;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractLockableInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

public class InscriberBlockEntity extends AbstractLockableInstrumentBlockEntity<InscriberBlockEntity, InscriptionRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + InscriberBlock.NAME) public static final TileEntityType<InscriberBlockEntity> TYPE = null;

	private final InstrumentInventory inventory;

	public InscriberBlockEntity() {
		super(TYPE, InscriptionRecipe.TYPE, ECConfig.COMMON.inscriberTransferSpeed.get(), ECConfig.COMMON.inscriberMaxRunes.get());
		inventory = new InscriberInventory(this::setChanged);
	}

	public int getItemCount() {
		return inventory.getItemCount();
	}

	@Override
	public void process() {
		super.process();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(getElementType(), level, Vector3d.atCenterOf(worldPosition).add(0, 0.2, 0), level.random);
		}
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
}
