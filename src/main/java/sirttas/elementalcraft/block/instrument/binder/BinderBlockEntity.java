package sirttas.elementalcraft.block.instrument.binder;

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
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

public class BinderBlockEntity extends AbstractLockableInstrumentBlockEntity<IBinder, AbstractBindingRecipe> implements IBinder {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + BinderBlock.NAME) public static final TileEntityType<BinderBlockEntity> TYPE = null;

	private final InstrumentInventory inventory;

	public BinderBlockEntity() {
		this(TYPE);
	}

	public BinderBlockEntity(TileEntityType<?> tileEntityType) {
		this(tileEntityType, ECConfig.COMMON.binderTransferSpeed.get(), ECConfig.COMMON.binderMaxRunes.get());
	}

	protected BinderBlockEntity(TileEntityType<?> tileEntityType, int transferSpeed, int maxRunes) {
		super(tileEntityType, AbstractBindingRecipe.TYPE, transferSpeed, maxRunes);
		inventory = new InstrumentInventory(this::setChanged, 10);
	}

	@Override
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
