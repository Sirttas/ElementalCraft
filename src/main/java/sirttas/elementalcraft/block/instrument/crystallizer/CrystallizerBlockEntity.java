package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentInventory;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

public class CrystallizerBlockEntity extends AbstractInstrumentBlockEntity<CrystallizerBlockEntity, CrystallizationRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + CrystallizerBlock.NAME) public static final BlockEntityType<CrystallizerBlockEntity> TYPE = null;

	private final InstrumentInventory inventory;

	public CrystallizerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, CrystallizationRecipe.TYPE, ECConfig.COMMON.crystallizerTransferSpeed.get(), ECConfig.COMMON.crystallizerMaxRunes.get());
		inventory = new CrystallizerInventory(this::setChanged);
		lockable = true;
	}

	public int getItemCount() {
		return inventory.getItemCount();
	}

	@Override
	public void process() {
		super.process();
		if (this.level.isClientSide) {
			ParticleHelper.createCraftingParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(0, 0.2, 0), level.random);
		}
	}

	@Override
	protected void onProgress() {
		if (level.isClientSide) {
			ParticleHelper.createElementFlowParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(0, 0.2D, 0), Direction.UP, 1, level.random);
		}
	}

	@Override
	public Container getInventory() {
		return inventory;
	}
}
