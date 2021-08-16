package sirttas.elementalcraft.block.source;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECTickableBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;

public class SourceBlockEntity extends AbstractECTickableBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceBlock.NAME) public static final TileEntityType<SourceBlockEntity> TYPE = null;

	private final SourceElementStorage elementStorage;
	private int recoverRate;

	public SourceBlockEntity() {
		super(TYPE);
		elementStorage = new SourceElementStorage(RandomUtils.nextInt(ECConfig.COMMON.sourceCapacityMin.get(), ECConfig.COMMON.sourceCapacityMax.get()), this::setChanged);
		recoverRate = ECConfig.COMMON.sourceRecoverRate.get();
	}

	public SourceBlockEntity(ElementType elementType) {
		this();
		elementStorage.setElementType(elementType);

	}

	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@Override
	public void tick() {
		if (elementStorage.getElementType() == ElementType.NONE) {
			elementStorage.setElementType(ElementType.getElementType(this.getBlockState()));
		}
		if (elementStorage.isExhausted()) {
			elementStorage.insertElement(recoverRate, false);
		}
		this.addParticle(level.random);
		super.tick();
	}

	private void addParticle(Random rand) {
		if (level.isClientSide && rand.nextFloat() < 0.2F) {
			if (elementStorage.isExhausted()) {
				ParticleHelper.createExhaustedSourceParticle(elementStorage.getElementType(), level, Vector3d.atCenterOf(worldPosition), rand);
			} else {
				ParticleHelper.createSourceParticle(elementStorage.getElementType(), level, Vector3d.atCenterOf(worldPosition), rand);
			}
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
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
	public CompoundNBT save(CompoundNBT compound) {
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
