package sirttas.elementalcraft.block.instrument.inscriber;

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
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

import javax.annotation.Nonnull;

public class InscriberBlockEntity extends AbstractInstrumentBlockEntity<InscriberBlockEntity, InscriptionRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + InscriberBlock.NAME) public static final BlockEntityType<InscriberBlockEntity> TYPE = null;

	private final InstrumentContainer inventory;

	public InscriberBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, InscriptionRecipe.TYPE, ECConfig.COMMON.inscriberTransferSpeed.get(), ECConfig.COMMON.inscriberMaxRunes.get());
		inventory = new InscriberContainer(this::setChanged);
		lockable = true;
		particleOffset = new Vec3(0, 0.2, 0);
	}

	public int getItemCount() {
		return inventory.getItemCount();
	}

	@Override
	protected void assemble() {
		clearContent();
		super.assemble();
	}
	
	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	@Override
	protected boolean progressOnTick() {
		return false;
	}

	public boolean useChisle() {
		return makeProgress();
	}
}
