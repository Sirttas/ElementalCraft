package sirttas.elementalcraft.world.feature.structure;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.property.ECProperties;

public class SourceAltarStructure extends ScatteredStructure<NoFeatureConfig> {

	public static final String NAME = "source_altar";

	public static final IStructurePieceType PIECE_TYPE = Piece::new;
	
	private static final ResourceLocation SMALL = ElementalCraft.createRL("altar/small");
	private static final ResourceLocation MEDIUM = ElementalCraft.createRL("altar/medium");

	public SourceAltarStructure() {
		super(NoFeatureConfig::deserialize);
	}

	@Override
	public IStartFactory getStartFactory() {
		return Start::new;
	}

	@Override
	public String getStructureName() {
		return NAME;
	}

	@Override
	public int getSize() {
		return 3;
	}

	@Override
	protected int getSeedModifier() {
		return 4847339;
	}

	@Override
	protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
		return ECConfig.CONFIG.sourceAltarDistance.get();
	}

	public static class Start extends StructureStart {

		public Start(Structure<?> structure, int x, int y, MutableBoundingBox mutableboundingbox, int ref, long seed) {
			super(structure, x, y, mutableboundingbox, ref, seed);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
			this.components.add(new Piece(templateManagerIn, this.rand.nextInt(8) < 6 ? SMALL : MEDIUM, ElementType.random(rand), new BlockPos(chunkX * 16, 0, chunkZ * 16)));
			this.recalculateStructureSize();
		}
	}

	public static class Piece extends TemplateStructurePiece {

		private ResourceLocation templateName;
		private ElementType elementType;

		public Piece(TemplateManager templateManager, ResourceLocation templateName, ElementType elementType, BlockPos pos) {
			super(PIECE_TYPE, 0);
			this.templatePosition = pos;
			this.templateName = templateName;
			this.elementType = elementType;
			initTemplate(templateManager);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbt) {
			super(PIECE_TYPE, nbt);
			this.templateName = new ResourceLocation(nbt.getString("Template"));
			this.elementType = ElementType.byName(nbt.getString("ElementType"));
			initTemplate(templateManager);
		}

		@Override
		protected void readAdditional(CompoundNBT tagCompound) {
			super.readAdditional(tagCompound);
			tagCompound.putString("Template", this.templateName.toString());
			tagCompound.putString("ElementType", this.elementType.getName());
		}

		private void initTemplate(TemplateManager templateManager) {
			this.setup(templateManager.getTemplateDefaulted(templateName), this.templatePosition,
					new PlacementSettings().setMirror(Mirror.NONE).setCenterOffset(new BlockPos(1, 0, 1)).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK));
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn) {
			this.templatePosition = this.templatePosition.add(0, worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ()) - 1, 0);
			return super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn);

		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
			if ("chest".equals(function)) {
				this.generateChest(worldIn, sbb, rand, pos, ElementalCraft.createRL("chests/altar/small_" + elementType.getName()), null);
				worldIn.notifyNeighbors(pos, Blocks.CHEST);
			} else if ("source".equals(function)) {
				worldIn.setBlockState(pos, ECBlocks.source.getDefaultState().with(ECProperties.ELEMENT_TYPE, elementType), 3);
			}
		}
	}

}
