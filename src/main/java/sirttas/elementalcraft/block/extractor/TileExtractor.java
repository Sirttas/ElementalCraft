package sirttas.elementalcraft.block.extractor;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
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

	public TileExtractor() {
		this(TYPE);
	}

	public TileExtractor(TileEntityType<?> type) {
		super(type);
		this.setPasive(true);
	}

	public boolean hasSource() {
		return getSourceElementType() != ElementType.NONE;
	}

	public TileTank getTank() {
		TileEntity te = this.hasWorld() ? this.getWorld().getTileEntity(pos.down()) : null;
		return te instanceof TileTank ? (TileTank) te : null;
	}

	protected BlockState getSourceState() {
		return this.hasWorld() ? this.getWorld().getBlockState(pos.up()) : null;
	}

	public ElementType getSourceElementType() {
		BlockState source = getSourceState();

		return source != null && source.getBlock() == ECBlocks.source ? ElementType.getElementType(source) : ElementType.NONE;
	}

	protected int getExtractionAmount() {
		return ECConfig.CONFIG.extractorExtractionAmount.get();
	}

	@Override
	public void tick() {
		super.tick();
		if (canExtract()) {
			getTank().inserElement(getExtractionAmount(), getSourceElementType(), false);
		}

	}

	public boolean canExtract() {
		TileTank tank = getTank();

		return hasWorld() && hasSource() && tank != null && tank.getElementAmount() < tank.getMaxElement();
	}
}
