package sirttas.elementalcraft.block.instrument.inscriber;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

import javax.annotation.Nonnull;

public class InscriberBlockEntity extends AbstractInstrumentBlockEntity<InscriberBlockEntity, InscriptionRecipe> {

	private static final Config<InscriberBlockEntity, InscriptionRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.INSCRIBER,
			ECRecipeTypes.INSCRIPTION,
			ECConfig.COMMON.inscriberTransferSpeed,
			ECConfig.COMMON.inscriberMaxRunes,
			0,
			true,
			true
	);

	private final InstrumentContainer inventory;

	public InscriberBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
		inventory = new InscriberContainer(this::setChanged);
		particleOffset = new Vec3(0, 0.2, 0);
	}

	public int getItemCount() {
		return inventory.getItemCount();
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

	public boolean useChisel() {
		return makeProgress();
	}
}
