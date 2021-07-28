package sirttas.elementalcraft.block.extractor;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class ExtractorBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ExtractorBlock.NAME) public static final BlockEntityType<ExtractorBlockEntity> TYPE = null;

	private int extractionAmount;
	private final RuneHandler runeHandler;

	public ExtractorBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		if (state.getBlock() == ECBlocks.EXTRACTOR_IMPROVED) {
			this.extractionAmount = ECConfig.COMMON.improvedExtractorExtractionAmount.get();
			runeHandler = new RuneHandler(ECConfig.COMMON.improvedExtractorMaxRunes.get());
		} else {
			this.extractionAmount = ECConfig.COMMON.extractorExtractionAmount.get();
			runeHandler = new RuneHandler(ECConfig.COMMON.extractorMaxRunes.get());
		}
	}


	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.extractionAmount = compound.getInt(ECNames.EXTRACTION_AMOUNT);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.putInt(ECNames.EXTRACTION_AMOUNT, this.extractionAmount);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
		return compound;
	}

	protected Optional<BlockState> getSourceState() {
		return this.hasLevel() ? Optional.of(this.getLevel().getBlockState(worldPosition.above())) : Optional.empty();
	}

	public ElementType getSourceElementType() {
		return getSourceState().filter(s -> s.getBlock() == ECBlocks.SOURCE).map(ElementType::getElementType).orElse(ElementType.NONE);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, ExtractorBlockEntity extractor) {
		ElementType sourceElementType = extractor.getSourceElementType();

		if (extractor.canExtract(sourceElementType)) {
			BlockEntityHelper.getTileEntityAs(level, pos.above(), SourceBlockEntity.class).map(SourceBlockEntity::getElementStorage)
					.ifPresent(sourceStorage -> sourceStorage.transferTo(extractor.getTank(), extractor.runeHandler.getTransferSpeed(extractor.extractionAmount), extractor.runeHandler.getElementPreservation()));
		}
	}

	public boolean canExtract() {
		return canExtract(getSourceElementType());
	}

	private boolean canExtract(ElementType sourceElementType) {
		ISingleElementStorage tank = getTank();

		return hasLevel() && sourceElementType != ElementType.NONE && tank != null && (tank.getElementAmount() < tank.getElementCapacity() || tank.getElementType() != sourceElementType);
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
