package sirttas.elementalcraft.world.feature.structure;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

public class SourceAltarStructure extends Structure<IElementTypeFeatureConfig> {

	public static final String NAME = "source_altar";

	public SourceAltarStructure() {
		super(IElementTypeFeatureConfig.CODEC);
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public IStartFactory<IElementTypeFeatureConfig> getStartFactory() {
		return Start::new;
	}

	@Override
	public String getStructureName() {
		return ElementalCraft.MODID + ":" + NAME;
	}

	public static class Start extends StructureStart<IElementTypeFeatureConfig> {

		public Start(Structure<IElementTypeFeatureConfig> structure, int x, int y, MutableBoundingBox mutableBoundingBox, int k, long l) {
			super(structure, x, y, mutableBoundingBox, k, l);
		}
		
		@Override
		public void func_230364_a_/* init */(DynamicRegistries dynamicRegistries, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn,
				IElementTypeFeatureConfig config) {
			this.components.add(new Piece(templateManagerIn, getRoll(), config.getElementType(rand), new BlockPos(chunkX * 16, 0, chunkZ * 16)));
			this.recalculateStructureSize();

		}
		
		private ResourceLocation getRoll() {
			int roll = this.rand.nextInt(20);
			
			if (roll == 0) {
				return ElementalCraft.createRL("altar/chapel");
			} else if (roll <= 3) {
				return ElementalCraft.createRL("altar/medium");
			}
			return ElementalCraft.createRL("altar/small");
		}
	}

	public static class Piece extends TemplateStructurePiece {

		private ResourceLocation templateName;
		private ElementType elementType;

		public Piece(TemplateManager templateManager, ResourceLocation templateName, ElementType elementType, BlockPos pos) {
			super(ECStructures.SOURCE_ALTAR_PIECE_TYPE, 0);
			this.templatePosition = pos;
			this.templateName = templateName;
			this.elementType = elementType;
			initTemplate(templateManager);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbt) {
			super(ECStructures.SOURCE_ALTAR_PIECE_TYPE, nbt);
			this.templateName = new ResourceLocation(nbt.getString("Template"));
			this.elementType = ElementType.byName(nbt.getString("ElementType"));
			initTemplate(templateManager);
		}

		private void initTemplate(TemplateManager templateManager) {
			this.setup(templateManager.getTemplateDefaulted(templateName), this.templatePosition,
					new PlacementSettings().setMirror(Mirror.NONE).setCenterOffset(new BlockPos(1, 0, 1)).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK));
		}

		@Override
		protected void readAdditional(CompoundNBT tagCompound) {
			super.readAdditional(tagCompound);
			tagCompound.putString("Template", this.templateName.toString());
			tagCompound.putString("ElementType", this.elementType.getString());
		}


		@Override
		public boolean func_230383_a_/* create */(ISeedReader worldIn, StructureManager structureManager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn,
				ChunkPos chunkPosIn, BlockPos pos) {
			this.templatePosition = this.templatePosition.add(0, worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ()) - 1, 0);
			return super.func_230383_a_/* create */(worldIn, structureManager, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn, pos);

		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
			if (function.endsWith("chest")) {
				this.generateChest(worldIn, sbb, rand, pos, ElementalCraft.createRL("chests/altar/" + getChestType(function) + '_' + elementType.getString()), null);
				worldIn.updateBlock(pos, Blocks.CHEST);
			} else if ("source".equals(function)) {
				worldIn.setBlockState(pos, ECBlocks.SOURCE.getDefaultState().with(ElementType.STATE_PROPERTY, elementType), 3);
			}
		}

		private String getChestType(String function) {
			String[] split = function.split("_");

			return (split.length > 1 ? split[0] : "small");
		}
	}

}
