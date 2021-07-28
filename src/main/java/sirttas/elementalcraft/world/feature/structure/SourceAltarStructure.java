package sirttas.elementalcraft.world.feature.structure;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

public class SourceAltarStructure extends StructureFeature<IElementTypeFeatureConfig> {

	public static final String NAME = "source_altar";

	public SourceAltarStructure() {
		super(IElementTypeFeatureConfig.CODEC);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public StructureStartFactory<IElementTypeFeatureConfig> getStartFactory() {
		return Start::new;
	}

	@Override
	public String getFeatureName() {
		return ElementalCraftApi.MODID + ":" + NAME;
	}

	public static class Start extends StructureStart<IElementTypeFeatureConfig> {

		public Start(StructureFeature<IElementTypeFeatureConfig> structure, ChunkPos pos, int k, long l) {
			super(structure, pos, k, l);
		}
		
		@Override
		public void generatePieces(RegistryAccess dynamicRegistries, ChunkGenerator generator, StructureManager templateManagerIn, ChunkPos pos, Biome biomeIn,
				IElementTypeFeatureConfig config, LevelHeightAccessor heightAccessor) {
			this.pieces.add(new Piece(templateManagerIn, getRoll(), config.getElementType(random), pos.getBlockAt(0, 0, 0)));
			this.createBoundingBox();
		}
		
		private ResourceLocation getRoll() {
			int roll = this.random.nextInt(20);
			
			if (roll == 0) {
				return ElementalCraft.createRL("altar/chapel");
			} else if (roll <= 3) {
				return ElementalCraft.createRL("altar/medium");
			}
			return ElementalCraft.createRL("altar/small");
		}
	}

	public static class Piece extends TemplateStructurePiece {

		private ElementType elementType;

		public Piece(StructureManager templateManager, ResourceLocation templateName, ElementType elementType, BlockPos pos) {
			super(ECStructures.SOURCE_ALTAR_PIECE_TYPE, 0, templateManager, templateName, templateName.toString(), makeSettings(templateName), pos);
			this.elementType = elementType;
		}

		public Piece(ServerLevel level, CompoundTag nbt) {
			super(ECStructures.SOURCE_ALTAR_PIECE_TYPE, nbt, level, Piece::makeSettings);
			this.elementType = ElementType.byName(nbt.getString("ElementType"));
		}
		
		private static StructurePlaceSettings makeSettings(ResourceLocation id) {
			return new StructurePlaceSettings()
					.setMirror(Mirror.NONE)
					.setRotationPivot(new BlockPos(1, 0, 1))
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		}

		@Override
		protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
			super.addAdditionalSaveData(level, tagCompound);
			tagCompound.putString("ElementType", this.elementType.getSerializedName());
		}


		@Override
		public boolean postProcess(WorldGenLevel worldIn, StructureFeatureManager structureManager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox mutableBoundingBoxIn,
				ChunkPos chunkPosIn, BlockPos pos) {
			this.templatePosition = this.templatePosition.offset(0, worldIn.getHeight(Heightmap.Types.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ()) - 1, 0);
			return super.postProcess(worldIn, structureManager, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn, pos);

		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor worldIn, Random rand, BoundingBox sbb) {
			if (function.endsWith("chest")) {
				this.createChest(worldIn, sbb, rand, pos, ElementalCraft.createRL("chests/altar/" + getChestType(function) + '_' + elementType.getSerializedName()), null);
				worldIn.blockUpdated(pos, Blocks.CHEST);
			} else if ("source".equals(function)) {
				worldIn.setBlock(pos, ECBlocks.SOURCE.defaultBlockState().setValue(ElementType.STATE_PROPERTY, elementType), 3);
			}
		}

		private String getChestType(String function) {
			String[] split = function.split("_");

			return (split.length > 1 ? split[0] : "small");
		}
	}

}
