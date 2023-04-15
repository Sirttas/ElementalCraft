package sirttas.elementalcraft.world.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SourceAltarStructure extends Structure {

	public static final String NAME = "source_altar";

	public static final Codec<SourceAltarStructure> CODEC = simpleCodec(SourceAltarStructure::new);

	public SourceAltarStructure(Structure.StructureSettings settings) {
		super(settings);
	}

	private void generatePieces(StructurePiecesBuilder builder, GenerationContext context, ElementType elementType) {
		var random = context.random();

		builder.addPiece(new Piece(context.structureTemplateManager(), getRoll(random), new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ()), elementType));
	}

	private static ResourceLocation getRoll(RandomSource random) {
		int roll = random.nextInt(20);

		if (roll == 0) {
			return ElementalCraft.createRL("altar/chapel");
		} else if (roll <= 3) {
			return ElementalCraft.createRL("altar/medium");
		}
		return ElementalCraft.createRL("altar/small");
	}

	@Nonnull
    @Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	@Nonnull
	@Override
	public Optional<GenerationStub> findGenerationPoint(@Nonnull GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, b -> this.generatePieces(b, context, ElementType.random(context.random())));
	}

	@Nonnull
	@Override
	public StructureType<?> type() {
		return ECStructureTypes.SOURCE_ALTAR.get();
	}

	public static class Piece extends TemplateStructurePiece {

		private final ElementType elementType;

		public Piece(StructureTemplateManager manager, ResourceLocation templateName, BlockPos pos, ElementType elementType) {
			super(ECStructureTypes.SOURCE_ALTAR_PIECE_TYPE.get(), 0, manager, templateName, templateName.toString(), makeSettings(templateName), pos);
			this.elementType = elementType;
		}

		public Piece(StructureTemplateManager manager, CompoundTag tag) {
			super(ECStructureTypes.SOURCE_ALTAR_PIECE_TYPE.get(), tag, manager, Piece::makeSettings);
			this.elementType = ElementType.byName(tag.getString("ElementType"));
		}

		private static StructurePlaceSettings makeSettings(ResourceLocation id) {
			return new StructurePlaceSettings()
					.setMirror(Mirror.NONE)
					.setRotationPivot(new BlockPos(1, 0, 1))
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
		}

		@Override
		protected void addAdditionalSaveData(@Nonnull StructurePieceSerializationContext context, @Nonnull CompoundTag tag) {
			super.addAdditionalSaveData(context, tag);
			tag.putString("ElementType", this.elementType.getSerializedName());
		}

		@Override
		public void postProcess(WorldGenLevel level, @Nonnull StructureManager structureManager, @Nonnull ChunkGenerator chunkGenerator, @Nonnull RandomSource random, @Nonnull BoundingBox boundingBox, @Nonnull ChunkPos chunkPos, @Nonnull BlockPos pos) {
			this.templatePosition = new BlockPos(templatePosition.getX(), level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ()) - 1, templatePosition.getZ());
			super.postProcess(level, structureManager, chunkGenerator, random, boundingBox, chunkPos, pos);
		}

		@Override
		protected void handleDataMarker(String name, @Nonnull BlockPos pos, @Nonnull ServerLevelAccessor level, @Nonnull RandomSource rand, @Nonnull BoundingBox sbb) {
			if (name.endsWith("chest")) {
				this.createChest(level, sbb, rand, pos, ElementalCraft.createRL("chests/altar/" + getChestType(name) + '_' + elementType.getSerializedName()), null);
				level.blockUpdated(pos, Blocks.CHEST);
			} else if (name.startsWith("source")) {
				level.setBlock(pos, ECBlocks.SOURCE.get().defaultBlockState().setValue(ElementType.STATE_PROPERTY, elementType), 3);
				BlockEntityHelper.getBlockEntityAs(level, pos, SourceBlockEntity.class).ifPresent(s -> s.resetTraits(level, getSourceLuck(name)));
				level.blockUpdated(pos, ECBlocks.SOURCE.get());
			}
		}

		private String getChestType(String name) {
			String[] split = name.split("_");

			return (split.length > 1 ? split[0] : "small");
		}

		private int getSourceLuck(String name) {
			String[] split = name.split("_");

			try {
				return (split.length > 1 ? Integer.parseInt(split[1]) : 0);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}
}
