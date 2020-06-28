package sirttas.elementalcraft.block.extractor.improved;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.extractor.TileExtractor;
import sirttas.elementalcraft.config.ECConfig;

public class TileImprovedExtractor extends TileExtractor {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedExtractor.NAME) public static TileEntityType<TileImprovedExtractor> TYPE;

	// TODO make it exaust sources
	public TileImprovedExtractor() {
		super(TYPE);
	}

	@Override
	protected int getExtractionAmount() {
		return ECConfig.CONFIG.improvedExtractorExtractionAmount.get();
	}
}
