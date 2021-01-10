package sirttas.elementalcraft.block.extractor;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.rune.Rune.BonusType;
import sirttas.elementalcraft.rune.capability.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.capability.RuneHandler;

public class TileExtractor extends TileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static TileEntityType<TileExtractor> TYPE;

	private int extractionAmount;
	private final RuneHandler runeHandler;
	
	public TileExtractor() {
		this(false);
	}

	public TileExtractor(boolean improved) {
		super(TYPE);
		if (improved) {
			this.extractionAmount = ECConfig.COMMON.improvedExtractorExtractionAmount.get();
			runeHandler = new RuneHandler(ECConfig.COMMON.improvedExtractorMaxRunes.get());
		} else {
			this.extractionAmount = ECConfig.COMMON.extractorExtractionAmount.get();
			runeHandler = new RuneHandler(ECConfig.COMMON.extractorMaxRunes.get());
		}
	}


	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.extractionAmount = compound.getInt(ECNames.EXTRACTION_AMOUNT);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.readNBT(runeHandler, null, compound.get(ECNames.RUNE_HANDLER));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt(ECNames.EXTRACTION_AMOUNT, this.extractionAmount);
		compound.put(ECNames.RUNE_HANDLER, CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.writeNBT(runeHandler, null));
		return compound;
	}

	protected Optional<BlockState> getSourceState() {
		return this.hasWorld() ? Optional.of(this.getWorld().getBlockState(pos.up())) : Optional.empty();
	}

	public ElementType getSourceElementType() {
		return getSourceState().filter(s -> s.getBlock() == ECBlocks.source).map(ElementType::getElementType).orElse(ElementType.NONE);
	}

	@Override
	public void tick() {
		ElementType sourceElementType = getSourceElementType();

		super.tick();
		if (canExtract(sourceElementType)) {
			getTank().insertElement((int) (extractionAmount * (runeHandler.getBonus(BonusType.SPEED) + 1)), sourceElementType, false);
		}
	}

	public boolean canExtract() {
		return canExtract(getSourceElementType());
	}

	private boolean canExtract(ElementType sourceElementType) {
		IElementStorage tank = getTank();

		return hasWorld() && sourceElementType != ElementType.NONE && tank != null && (tank.getElementAmount() < tank.getElementCapacity() || tank.getElementType() != sourceElementType);
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
