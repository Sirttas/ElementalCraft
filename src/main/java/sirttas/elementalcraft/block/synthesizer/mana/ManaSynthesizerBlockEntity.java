package sirttas.elementalcraft.block.synthesizer.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;

public class ManaSynthesizerBlockEntity extends SolarSynthesizerBlockEntity {

	private final ManaSynthesizerManaReceiver manaReceiver;

	public ManaSynthesizerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.MANA_SYNTHESIZER, ECConfig.COMMON.manaSynthesizerManaCapacity.get(), pos, state);
		manaReceiver = new ManaSynthesizerManaReceiver(this);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ManaSynthesizerBlockEntity manaSynthesizer) {
		double ratio = ECConfig.COMMON.manaElementRatio.get();
		var mana = Math.min(manaSynthesizer.multiplier / 20, manaSynthesizer.manaReceiver.getCurrentMana());

		if (mana > 0) {
			var synthesized = manaSynthesizer.handleSynthesis((float) (mana * ratio));

			if (synthesized > 0) {
				manaSynthesizer.manaReceiver.receiveMana(-(int) Math.round(synthesized / ratio));
				manaSynthesizer.breakLens(level, pos);
			}
		} else {
			manaSynthesizer.working = false;
		}
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		manaReceiver.setMana(compound.getInt("mana"));
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt("mana", manaReceiver.getCurrentMana());
	}
}
