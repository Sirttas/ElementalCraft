package sirttas.elementalcraft.block.extractor;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.ECNames;

public class TileExtractor extends TileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static TileEntityType<TileExtractor> TYPE;

	int extractionAmount;
	
	public TileExtractor() {
		this(ECConfig.COMMON.extractorExtractionAmount.get());
	}

	public TileExtractor(int extractionAmount) {
		super(TYPE);
		this.extractionAmount = extractionAmount;
	}


	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.extractionAmount = compound.getInt(ECNames.EXTRACTION_AMOUNT);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt(ECNames.EXTRACTION_AMOUNT, this.extractionAmount);
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
			getTank().inserElement(extractionAmount, sourceElementType, false);
		}
	}

	public boolean canExtract() {
		return canExtract(getSourceElementType());
	}

	private boolean canExtract(ElementType sourceElementType) {
		TileTank tank = getTank();

		return hasWorld() && sourceElementType != ElementType.NONE && tank != null && (tank.getElementAmount() < tank.getElementCapacity() || tank.getElementType() != sourceElementType);
	}
}
