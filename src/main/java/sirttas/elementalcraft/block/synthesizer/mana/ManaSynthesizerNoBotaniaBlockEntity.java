package sirttas.elementalcraft.block.synthesizer.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;

public class ManaSynthesizerNoBotaniaBlockEntity extends SolarSynthesizerBlockEntity {

	private int mana;

	public ManaSynthesizerNoBotaniaBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.MANA_SYNTHESIZER, ECConfig.COMMON.manaSynthesizerManaCapacity.get(), pos, state);
		this.mana = 0;
	}


	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		mana = compound.getInt("mana");
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("mana", mana);
	}
}
