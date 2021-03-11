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
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.AbstractTileECTickable;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.particle.ParticleHelper;

public class TileSource extends AbstractTileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSource.NAME) public static final TileEntityType<TileSource> TYPE = null;

	private final SourceElementStorage elementStorage;
	private int recoverRate;

	public TileSource() {
		super(TYPE);
		elementStorage = new SourceElementStorage(RandomUtils.nextInt(ECConfig.COMMON.sourceCapacityMin.get(), ECConfig.COMMON.sourceCapacityMax.get()), this::markDirty);
		recoverRate = ECConfig.COMMON.sourceRecoverRate.get();
	}

	public TileSource(ElementType elementType) {
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
		elementStorage.insertElement(recoverRate, false);
		this.addParticle(world.rand);
		super.tick();
	}

	private void addParticle(Random rand) {
		if (world.isRemote && rand.nextFloat() < 0.2F) {
			if (elementStorage.isExhausted()) {
				ParticleHelper.createExhaustedSourceParticle(elementStorage.getElementType(), world, Vector3d.copyCentered(pos), rand);
			} else {
				ParticleHelper.createSourceParticle(elementStorage.getElementType(), world, Vector3d.copyCentered(pos), rand);
			}
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.readNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		this.recoverRate = compound.getInt(ECNames.RECOVER_RATE);
		elementStorage.setExhausted(compound.getBoolean(ECNames.EXHAUSTED));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.writeNBT());
		compound.putInt(ECNames.RECOVER_RATE, this.recoverRate);
		compound.putBoolean(ECNames.EXHAUSTED, elementStorage.isExhausted());
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
