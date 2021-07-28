package sirttas.elementalcraft.block.instrument.binder;

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
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

public class BinderBlockEntity extends AbstractInstrumentBlockEntity<IBinder, AbstractBindingRecipe> implements IBinder {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + BinderBlock.NAME) public static final BlockEntityType<BinderBlockEntity> TYPE = null;

	private final InstrumentInventory inventory;

	public BinderBlockEntity(BlockPos pos, BlockState state) {
		this(TYPE, pos, state, ECConfig.COMMON.binderTransferSpeed.get(), ECConfig.COMMON.binderMaxRunes.get());
	}

	protected BinderBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int transferSpeed, int maxRunes) {
		super(blockEntityType, pos, state, AbstractBindingRecipe.TYPE, transferSpeed, maxRunes);
		inventory = new InstrumentInventory(this::setChanged, 10);
		lockable = true;
	}

	@Override
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
