package sirttas.elementalcraft.block.synthesizer.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManaSynthesizerBlockEntity extends SolarSynthesizerBlockEntity {

//	private final ManaSynthesizerManaReceiver manaReceiver;

	public ManaSynthesizerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.MANA_SYNTHESIZER, pos, state);
//		manaReceiver = new ManaSynthesizerManaReceiver(this);
	}

	//	public static void serverTick(Level level, BlockPos pos, BlockState state, ManaSynthesizerBlockEntity manaSynthesizer) {
//		var ratio = ECConfig.COMMON.manaElementRatio.get();
//		var mana = Math.min(ECConfig.COMMON.manaSythesizerManaCapacity.get() / 20, manaSynthesizer.manaReceiver.getCurrentMana());
//
//		if (mana > 0) {
//			var synthesized = manaSynthesizer.handleSynthesis((float) (mana * ratio));
//
//			if (synthesized > 0) {
//				manaSynthesizer.manaReceiver.receiveMana(-(int) Math.round(synthesized / ratio));
//				manaSynthesizer.breakLense(level, pos);
//			}
//		}
//	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
//		manaReceiver.setMana(compound.getInt("mana"));
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
//		compound.putInt("mana", manaReceiver.getCurrentMana());
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove) {
//			if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
//				return LazyOptional.of(manaReceiver != null ? () -> manaReceiver : null).cast();
//			} else
			if (cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
				return getElementStorage(ECConfig.COMMON.manaSythesizerLenseElementMultiplier.get());
			}
		}
		return super.getCapability(cap, side);
	}
}
