package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

import javax.annotation.Nonnull;

public class CrystallizerBlockEntity extends AbstractInstrumentBlockEntity<CrystallizerBlockEntity, CrystallizationRecipe> {

	private static final Config<CrystallizerBlockEntity, CrystallizationRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.CRYSTALLIZER,
			ECRecipeTypes.CRYSTALLIZATION,
			ECConfig.SERVER.crystallizerTransferSpeed,
			ECConfig.SERVER.crystallizerMaxRunes,
			0,
			true,
			true
	);

	private final InstrumentContainer inventory;

	public CrystallizerBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
		inventory = new CrystallizerContainer(this);
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
}
