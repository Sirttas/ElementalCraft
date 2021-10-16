package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

public class BinderBlockEntity extends AbstractInstrumentBlockEntity<IBinder, AbstractBindingRecipe> implements IBinder {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + BinderBlock.NAME) public static final BlockEntityType<BinderBlockEntity> TYPE = null;

	private final InstrumentContainer inventory;

	public BinderBlockEntity(BlockPos pos, BlockState state) {
		this(TYPE, pos, state, ECConfig.COMMON.binderTransferSpeed.get(), ECConfig.COMMON.binderMaxRunes.get());
	}

	protected BinderBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int transferSpeed, int maxRunes) {
		super(blockEntityType, pos, state, AbstractBindingRecipe.TYPE, transferSpeed, maxRunes);
		inventory = new InstrumentContainer(this::setChanged, 10);
		lockable = true;
		particleOffset = new Vec3(0, 0.2, 0);
	}

	@Override
	public int getItemCount() {
		return inventory.getItemCount();
	}

	@Override
	protected void assemble() {
		clearContent();
		super.assemble();
	}
	
	@Override
	public Container getInventory() {
		return inventory;
	}
	
}
