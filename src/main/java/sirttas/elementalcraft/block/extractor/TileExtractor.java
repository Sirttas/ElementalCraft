package sirttas.elementalcraft.block.extractor;

import java.util.Optional;

import net.minecraft.block.BlockState;
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



	protected Optional<BlockState> getSourceState() {
		return this.hasWorld() ? Optional.ofNullable(this.getWorld().getBlockState(pos.up())) : Optional.empty();
	}

	public ElementType getSourceElementType() {
		return getSourceState().filter(s -> s.getBlock() == ECBlocks.source).map(ElementType::getElementType).orElse(ElementType.NONE);
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
