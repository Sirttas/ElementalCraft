package sirttas.elementalcraft.block.manasynthesizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.block.solarsynthesizer.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.elemental.LenseItem;
import vazkii.botania.api.BotaniaForgeCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManaSynthesizerBlockEntity extends SolarSynthesizerBlockEntity {

	public static final RegistryObject<BlockEntityType<ManaSynthesizerBlockEntity>> TYPE = RegistryObject.create(ElementalCraft.createRL(ManaSynthesizerBlock.NAME),  ForgeRegistries.BLOCK_ENTITIES);

	private final ManaSynthesizerManaReceiver manaReceiver;

	public ManaSynthesizerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE.get(), pos, state);
		manaReceiver = new ManaSynthesizerManaReceiver(this);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ManaSynthesizerBlockEntity manaSynthesizer) {
		var ratio = ECConfig.COMMON.manaElementRatio.get();
		var mana = Math.min(ECConfig.COMMON.manaSythesizerManaCapacity.get() / 20, manaSynthesizer.manaReceiver.getCurrentMana());

		if (mana > 0) {
			var synthesized = manaSynthesizer.handleSynthesis((float) (mana * ratio));

			if (synthesized > 0) {
				manaSynthesizer.manaReceiver.receiveMana(-(int) Math.round(synthesized / ratio));
				manaSynthesizer.breakLense(level, pos);
			}
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

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove) {
			if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
				return LazyOptional.of(manaReceiver != null ? () -> manaReceiver : null).cast();
			} else if (cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
				var item = getInventory().getItem(0);

				if (item.getItem() instanceof LenseItem lenseItem) {
					return LazyOptional.of(() -> lenseItem.getStorage(item, ECConfig.COMMON.manaSythesizerLenseElementMultiplier.get())).cast();
				}
				return CapabilityElementStorage.get(item).cast();
			}
		}
		return super.getCapability(cap, side);
	}
}
