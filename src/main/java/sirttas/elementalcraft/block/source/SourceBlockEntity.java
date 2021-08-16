package sirttas.elementalcraft.block.source;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;

public class SourceBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceBlock.NAME) public static final BlockEntityType<SourceBlockEntity> TYPE = null;

	private final SourceElementStorage elementStorage;
	private int recoverRate;

	public SourceBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		elementStorage = new SourceElementStorage(RandomUtils.nextInt(ECConfig.COMMON.sourceCapacityMin.get(), ECConfig.COMMON.sourceCapacityMax.get()), this::setChanged);
		recoverRate = ECConfig.COMMON.sourceRecoverRate.get();
		elementStorage.setElementType(ElementType.getElementType(state));
	}


	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, SourceBlockEntity source) {
		if (source.elementStorage.getElementType() == ElementType.NONE) {
			source.elementStorage.setElementType(ElementType.getElementType(state));
		}
		if (source.elementStorage.isExhausted()) {
			source.elementStorage.insertElement(source.recoverRate, false);
		}
	}
	
	public static void clientTick(Level level, BlockPos pos, BlockState state, SourceBlockEntity source) {
		commonTick(level, pos, state, source);
		source.addParticle(level.random);
	}

	private void addParticle(Random rand) {
		if (level.isClientSide && rand.nextFloat() < 0.2F) {
			if (elementStorage.isExhausted()) {
				ParticleHelper.createExhaustedSourceParticle(elementStorage.getElementType(), level, Vec3.atCenterOf(worldPosition), rand);
			} else {
				ParticleHelper.createSourceParticle(elementStorage.getElementType(), level, Vec3.atCenterOf(worldPosition), rand);
			}
		}
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		recoverRate = compound.getInt(ECNames.RECOVER_RATE);
		elementStorage.setExhausted(compound.getBoolean(ECNames.EXHAUSTED));
		if (recoverRate <= 10) { // TODO later remove
			recoverRate = ECConfig.COMMON.sourceRecoverRate.get();
		}
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
		compound.putInt(ECNames.RECOVER_RATE, this.recoverRate);
		compound.putBoolean(ECNames.EXHAUSTED, elementStorage.isExhausted());
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
