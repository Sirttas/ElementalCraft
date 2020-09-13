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

public class TileExtractor extends TileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static TileEntityType<TileExtractor> TYPE;

	int extractionAmount;
	
	public TileExtractor() {
		this(ECConfig.CONFIG.extractorExtractionAmount.get());
	}

	public TileExtractor(int extractionAmount) {
		super(TYPE);
		this.setPasive(true);
		this.extractionAmount = extractionAmount;
	}

	public boolean hasSource() {
		return getSourceElementType() != ElementType.NONE;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.extractionAmount = compound.getInt("extraction_amount");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("extraction_amount", this.extractionAmount);
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
		super.tick();
		if (canExtract()) {
			getTank().inserElement(extractionAmount, getSourceElementType(), false);
		}
	}

	public boolean canExtract() {
		TileTank tank = getTank();

		return hasWorld() && hasSource() && tank != null && tank.getElementAmount() < tank.getMaxElement();
	}
}
