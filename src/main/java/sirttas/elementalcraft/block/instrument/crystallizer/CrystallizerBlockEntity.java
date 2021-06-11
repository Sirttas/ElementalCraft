package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractLockableInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

public class CrystallizerBlockEntity extends AbstractLockableInstrumentBlockEntity<CrystallizerBlockEntity, CrystallizationRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + CrystallizerBlock.NAME) public static final TileEntityType<CrystallizerBlockEntity> TYPE = null;

	private final InstrumentInventory inventory;

	public CrystallizerBlockEntity() {
		super(TYPE, CrystallizationRecipe.TYPE, ECConfig.COMMON.crystallizerTransferSpeed.get(), ECConfig.COMMON.crystallizerMaxRunes.get());
		inventory = new CrystallizerInventory(this::setChanged);
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
	protected void onProgress() {
		if (level.isClientSide) {
			ParticleHelper.createElementFlowParticle(getElementType(), level, Vector3d.atCenterOf(worldPosition).add(0, 0.2D, 0), Direction.UP, 1, level.random);
		}
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}
}
