package sirttas.elementalcraft.world.feature.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

import java.util.Random;

public class SourceAltarStructure extends StructureFeature<IElementTypeFeatureConfig> {

	public static final String NAME = "source_altar";

	public SourceAltarStructure() {
		super(IElementTypeFeatureConfig.CODEC, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), SourceAltarStructure::generatePieces));
	}

	private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<IElementTypeFeatureConfig> context) {
		var random = context.random();

		builder.addPiece(new Piece(context.structureManager(), getRoll(random), new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ()), context.config().getElementType(random)));
	}

	private static ResourceLocation getRoll(Random random) {
		int roll = random.nextInt(20);

		if (roll == 0) {
			return ElementalCraft.createRL("altar/chapel");
		} else if (roll <= 3) {
			return ElementalCraft.createRL("altar/medium");
		}
		return ElementalCraft.createRL("altar/small");
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public String getFeatureName() {
		return ElementalCraftApi.MODID + ":" + NAME;
	}

	public static class Piece extends TemplateStructurePiece {

		private final ElementType elementType;

		public Piece(StructureManager manager, ResourceLocation templateName, BlockPos pos, ElementType elementType) {
			super(ECStructures.SOURCE_ALTAR_PIECE_TYPE, 0, manager, templateName, templateName.toString(), makeSettings(templateName), pos);
			this.elementType = elementType;
		}

		public Piece(StructureManager manager, CompoundTag tag) {
			super(ECStructures.SOURCE_ALTAR_PIECE_TYPE, tag, manager, Piece::makeSettings);
			this.elementType = ElementType.byName(tag.getString("ElementType"));
		}

		private static StructurePlaceSettings makeSettings(ResourceLocation id) {
			return new StructurePlaceSettings()
					.setMirror(Mirror.NONE)
					.setRotationPivot(new BlockPos(1, 0, 1))
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
			super.addAdditionalSaveData(context, tag);
			tag.putString("ElementType", this.elementType.getSerializedName());
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager structureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
			this.templatePosition = this.templatePosition.offset(0, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ()) - 1, 0);
			super.postProcess(level, structureManager, chunkGenerator, random, boundingBox, chunkPos, pos);
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
