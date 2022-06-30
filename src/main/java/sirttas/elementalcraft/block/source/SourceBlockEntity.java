package sirttas.elementalcraft.block.source;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class SourceBlockEntity extends AbstractECBlockEntity implements IElementTypeProvider {

	private boolean analyzed = false;
	private boolean stabilized = false;
	
	private final SourceElementStorage elementStorage;
	private final Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits;

	public SourceBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SOURCE, pos, state);
		elementStorage = new SourceElementStorage(this);
		elementStorage.setElementType(ElementType.getElementType(state));
		traits = new TreeMap<>(Comparator.comparingInt(SourceTraits::getOrder));
	}


	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

    public static void serverTick(Level level, BlockPos pos, BlockState state, SourceBlockEntity source) {
        source.initTraits(false);
        if (source.elementStorage.isExhausted()) {
			if (source.isFleeting()) {
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			} else {
				source.elementStorage.insertElement(source.getRecoverRate(), false);
			}
        }
    }

    private void initTraits(boolean fleeting) {
        if (elementStorage.getElementType() == ElementType.NONE) {
            elementStorage.setElementType(ElementType.getElementType(this.getBlockState()));
			this.setChanged();
        }
        if (traits.isEmpty()) {
            for (var entry : ElementalCraftApi.SOURCE_TRAIT_MANAGER.getData().entrySet()) {
				var key = SourceTraits.key(entry.getKey());

				if (key.equals(SourceTraits.FLEETING) && !fleeting) {
					continue;
				}
                var value = entry.getValue().roll(level, worldPosition);

                if (value != null) {
                    traits.put(key, value);
                }
            }
			var capacity = getCapacity();

			elementStorage.setElementCapacity(capacity);
			elementStorage.setElementAmount(capacity);
			this.setChanged();
        }
    }

	public void resetTraits(boolean fleeting) {
		traits.clear();
		this.initTraits(fleeting);
	}
	
	private void addParticle(RandomSource rand) {
		if (level != null && level.isClientSide && rand.nextFloat() < 0.2F) {
			if (elementStorage.isExhausted()) {
				ParticleHelper.createExhaustedSourceParticle(elementStorage.getElementType(), level, Vec3.atCenterOf(worldPosition), rand);
			} else {
				ParticleHelper.createSourceParticle(elementStorage.getElementType(), level, Vec3.atCenterOf(worldPosition), rand);
			}
		}
	}

	public boolean isExhausted() {
		return elementStorage.isExhausted();
	}
	
	public int getRecoverRate() {
		var rate = getTrait(SourceTraits.RECOVER_RATE);
		var diurnal = 1F + getTrait(SourceTraits.DIURNAL_NOCTURNAL) * (this.level.isDay() ? 1 : this.level.isNight() ? -1 : 0);
		
		return Math.round(rate * diurnal) + (stabilized ? 20 : 0);
	}

	public int getCapacity() {
		var capacity = getTrait(SourceTraits.ELEMENT_CAPACITY);
		var fleeting = Math.max(1, getTrait(SourceTraits.FLEETING));

		return Math.round(capacity * fleeting);
	}
	
	public float getSpeedModifier() {
		return 1 + getTrait(SourceTraits.GENEROSITY);
	}
	
	public float getPreservationModifier() {
		return 1 + getTrait(SourceTraits.THRIFTINESS);
	}
	
	private float getTrait(ResourceKey<SourceTrait> trait) {
		var value = traits.get(trait);
		
		return value != null ? value.getValue() : 0;
	}

	public boolean isAnalyzed() {
		return analyzed;
	}
	
	public void setAnalyzed(boolean analyzed) {
		this.analyzed = analyzed;
		this.setChanged();
	}

	public boolean isFleeting() {
		return traits.containsKey(SourceTraits.FLEETING);
	}

	public boolean isStabilized() {
		return stabilized;
	}

	public void exhaust() {
		if (!this.isFleeting()) {
			elementStorage.setElementAmount(0);
		}
	}

	public void setStabilized(boolean stabilized) {
		this.stabilized = stabilized;
		this.setChanged();
	}

	@Override
	public ElementType getElementType() {
		return this.elementStorage.getElementType();
	}

	public float getRemainingRatio() {
		var capacity = this.elementStorage.getElementCapacity();

		if (capacity == 0) {
			return 0;
		}
		return this.elementStorage.getElementAmount() / (float) capacity;
	}

	public Map<ResourceKey<SourceTrait>, ISourceTraitValue> getTraits() {
		return traits;
	}
	
	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		elementStorage.setExhausted(compound.getBoolean(ECNames.EXHAUSTED));
		analyzed = compound.getBoolean(ECNames.ANALYZED);
		stabilized = compound.getBoolean(ECNames.STABILIZED);
		SourceTraitHelper.loadTraits(compound.getCompound(ECNames.TRAITS), traits);
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
		compound.putBoolean(ECNames.EXHAUSTED, elementStorage.isExhausted());
		compound.putBoolean(ECNames.ANALYZED, analyzed);
		compound.putBoolean(ECNames.STABILIZED, stabilized);
		compound.put(ECNames.TRAITS, SourceTraitHelper.saveTraits(traits));
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
