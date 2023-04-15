package sirttas.elementalcraft.block.extractor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ExtractorBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity {
	private int extractionAmount;
	private final RuneHandler runeHandler;

	private ISingleElementStorage containerCache;

	public ExtractorBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.EXTRACTOR, pos, state);
		if (state.is(ECBlocks.EXTRACTOR_IMPROVED.get())) {
			this.extractionAmount = ECConfig.COMMON.improvedExtractorExtractionAmount.get();
			runeHandler = new RuneHandler(ECConfig.COMMON.improvedExtractorMaxRunes.get(), this::setChanged);
		} else {
			this.extractionAmount = ECConfig.COMMON.extractorExtractionAmount.get();
			runeHandler = new RuneHandler(ECConfig.COMMON.extractorMaxRunes.get(), this::setChanged);
		}
	}


	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		this.extractionAmount = compound.getInt(ECNames.EXTRACTION_AMOUNT);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt(ECNames.EXTRACTION_AMOUNT, this.extractionAmount);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	protected Optional<BlockState> getSourceState() {
		return this.level != null ? Optional.of(this.level.getBlockState(worldPosition.above())) : Optional.empty();
	}

	public ElementType getSourceElementType() {
		return getSourceState().filter(s -> s.getBlock() == ECBlocks.SOURCE.get()).map(ElementType::getElementType).orElse(ElementType.NONE);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ExtractorBlockEntity extractor) {
		if (extractor.canExtract()) {
			BlockEntityHelper.getBlockEntityAs(level, pos.above(), SourceBlockEntity.class).map(SourceBlockEntity::getElementStorage)
					.ifPresent(sourceStorage ->  extractor.runeHandler.handleElementTransfer(sourceStorage, extractor.getContainer(), extractor.extractionAmount));
		}
	}

	public boolean canExtract() {
		if (this.level == null) {
			return false;
		}

		return BlockEntityHelper.getBlockEntityAs(this.level, this.worldPosition.above(), SourceBlockEntity.class).map(source -> {
			if (source.isExhausted()) {
				return false;
			}

			ElementType sourceElementType = source.getElementType();
			ISingleElementStorage container = getContainer();

			return hasLevel() && sourceElementType != ElementType.NONE && container != null && (container.getElementAmount() < container.getElementCapacity() || container.getElementType() != sourceElementType);
		}).orElse(false);
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	public ISingleElementStorage getContainer() {
		if (containerCache == null) {
			containerCache = IContainerTopBlockEntity.super.getContainer();
		}
		return containerCache;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == ElementalCraftCapabilities.RUNE_HANDLE) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
