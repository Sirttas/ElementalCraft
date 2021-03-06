package sirttas.elementalcraft.datagen;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.instrument.mill.AirMillBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock.CoverType;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlock;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlock;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.block.sorter.SorterBlock;
import sirttas.elementalcraft.block.source.SourceBlock;

public class ECBlockStateProvider extends BlockStateProvider {

	private static final String SIDE = "_side";
	private static final String CORE = "_core";
	
	private ExistingFileHelper existingFileHelper;
	
	private ModelFile air;
	private ModelFile tankConnector;

	public ECBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, ElementalCraftApi.MODID, exFileHelper);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void registerStatesAndModels() {
		air = models().getExistingFile(new ResourceLocation("block/air"));
		tankConnector = models().getExistingFile(prefix("tank_connector"));
		
		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraftApi.MODID.equals(block.getRegistryName().getNamespace()) && !exists(block)) {
				save(block);
			}
		}
	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(block.getRegistryName(), ResourcePackType.CLIENT_RESOURCES, ".json", "blockstates");
	}

	private boolean modelExists(Block block) {
		return existingFileHelper.exists(block.getRegistryName(), ResourcePackType.CLIENT_RESOURCES, ".json", "models/block");
	}

	private ResourceLocation prefix(String name) {
		return ElementalCraft.createRL(ModelProvider.BLOCK_FOLDER + '/' + name);
	}

	private void save(Block block) {
		String name = block.getRegistryName().getPath();

		if (block instanceof SlabBlock) {
			slabBlock((SlabBlock) block);
		} else if (block instanceof StairsBlock) {
			stairsBlock((StairsBlock) block);
		} else if (block instanceof WallBlock) {
			wallBlock((WallBlock) block);
		} else if (block instanceof PaneBlock) {
			paneBlock((PaneBlock) block);
		} else if (block == ECBlocks.WHITE_ROCK_FENCE) {
			fenceBlock((FenceBlock) block, prefix("whiterock"), prefix("iron"));
		} else if (block instanceof OverloadShrineBlock) {
			ModelFile base = models().getExistingFile(prefix(name + "_base"));
			ModelFile top = models().getExistingFile(prefix(name + "_top"));
			ModelFile side = models().getExistingFile(prefix(name + SIDE));

			getMultipartBuilder(block).part().modelFile(base).addModel().end()
				.part().modelFile(top).addModel().condition(OverloadShrineBlock.FACING, Direction.UP).end()
				.part().modelFile(side).addModel().condition(OverloadShrineBlock.FACING, Direction.NORTH).end()
				.part().modelFile(side).rotationY(90).addModel().condition(OverloadShrineBlock.FACING, Direction.EAST).end()
				.part().modelFile(side).rotationY(180).addModel().condition(OverloadShrineBlock.FACING, Direction.SOUTH).end()
				.part().modelFile(side).rotationY(270).addModel().condition(OverloadShrineBlock.FACING, Direction.WEST).end();
		} else if (block instanceof BreedingShrineBlock) {
			ModelFile core = models().getExistingFile(prefix(name + CORE));
			ModelFile bowl = models().getExistingFile(prefix(name + "_bowl"));

			horizontalBlock(block, state -> state.getValue(BreedingShrineBlock.PART) == BreedingShrineBlock.Part.CORE ? core : bowl);
		} else if (block instanceof AirMillBlock) {
			getVariantBuilder(block)
				.partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).setModels(new ConfiguredModel(air))
				.partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).setModels(new ConfiguredModel(models().getExistingFile(prefix(name))));
		} else if (block instanceof ReservoirBlock) {
			ModelFile base = models().getExistingFile(prefix(name));
			ModelFile top = models().getExistingFile(prefix("reservoir_top"));
			ModelFile connector = models().getExistingFile(prefix(name + "_connector"));
			
			getMultipartBuilder(block)
			.part().modelFile(top).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).end()
			.part().modelFile(base).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).end()
			.part().modelFile(tankConnector).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).condition(BlockStateProperties.NORTH, true).end()
			.part().modelFile(tankConnector).rotationY(90).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).condition(BlockStateProperties.EAST, true).end()
			.part().modelFile(tankConnector).rotationY(180).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).condition(BlockStateProperties.SOUTH, true).end()
			.part().modelFile(tankConnector).rotationY(270).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).condition(BlockStateProperties.WEST, true).end()
			.part().modelFile(connector).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).condition(BlockStateProperties.NORTH, true).end()
			.part().modelFile(connector).rotationY(90).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).condition(BlockStateProperties.EAST, true).end()
			.part().modelFile(connector).rotationY(180).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).condition(BlockStateProperties.SOUTH, true).end()
			.part().modelFile(connector).rotationY(270).addModel().condition(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).condition(BlockStateProperties.WEST, true).end();
		} else if (block instanceof ElementContainerBlock) {
			tankPedestalBlock(block, models().getExistingFile(prefix(name)), tankConnector);
		} else if (block instanceof PedestalBlock) {
			tankPedestalBlock(block, models().getExistingFile(prefix(name)), models().getExistingFile(prefix("pedestal_connector")));
		} else if (block instanceof RetrieverBlock || block instanceof SorterBlock) {
			ModelFile core = models().getExistingFile(prefix(name + CORE));
			ModelFile source = models().getExistingFile(prefix("instrument_retriever_source"));
			ModelFile target = models().getExistingFile(prefix("instrument_retriever_target"));

			getMultipartBuilder(block).part().modelFile(core).addModel().end()
				.part().modelFile(source).uvLock(true).addModel().condition(ISorterBlock.SOURCE, Direction.SOUTH).end()
				.part().modelFile(source).rotationY(90).uvLock(true).addModel().condition(ISorterBlock.SOURCE, Direction.WEST).end()
				.part().modelFile(source).rotationY(180).uvLock(true).addModel().condition(ISorterBlock.SOURCE, Direction.NORTH).end()
				.part().modelFile(source).rotationY(270).uvLock(true).addModel().condition(ISorterBlock.SOURCE, Direction.EAST).end()
				.part().modelFile(source).rotationX(270).uvLock(true).addModel().condition(ISorterBlock.SOURCE, Direction.DOWN).end()
				.part().modelFile(source).rotationX(90).uvLock(true).addModel().condition(ISorterBlock.SOURCE, Direction.UP).end()
				.part().modelFile(target).rotationY(180).uvLock(true).addModel().condition(ISorterBlock.TARGET, Direction.SOUTH).end()
				.part().modelFile(target).rotationY(270).uvLock(true).addModel().condition(ISorterBlock.TARGET, Direction.WEST).end()
				.part().modelFile(target).uvLock(true).addModel().condition(ISorterBlock.TARGET, Direction.NORTH).end()
				.part().modelFile(target).rotationY(90).uvLock(true).addModel().condition(ISorterBlock.TARGET, Direction.EAST).end()
				.part().modelFile(target).rotationX(90).uvLock(true).addModel().condition(ISorterBlock.TARGET, Direction.DOWN).end()
				.part().modelFile(target).rotationX(270).uvLock(true).addModel().condition(ISorterBlock.TARGET, Direction.UP).end();
		} else if (block instanceof ElementPipeBlock) {
			if (block == ECBlocks.PIPE) {
				pipeBlock((ElementPipeBlock) block, name, "brass");
			} else if (block == ECBlocks.PIPE_IMPROVED) {
				pipeBlock((ElementPipeBlock) block, name, "pure_iron");
			} else {
				pipeBlock((ElementPipeBlock) block, name, "iron");
			}
		} else if (block.defaultBlockState().hasProperty(HorizontalBlock.FACING)) {
			horizontalBlock(block, models().getExistingFile(prefix(name)));
		} else if (block.defaultBlockState().hasProperty(DirectionalBlock.FACING)) {
			directionalBlock(block, models().getExistingFile(prefix(name)));
		} else if (block.defaultBlockState().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
			ModelFile upper = models().getExistingFile(prefix(name + "_upper"));
			ModelFile lower = models().getExistingFile(prefix(name + "_lower"));

			getVariantBuilder(block)
				.partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).setModels(new ConfiguredModel(upper))
				.partialState().with(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).setModels(new ConfiguredModel(lower));
		} else if (block instanceof SourceBlock) {
			simpleBlock(block, air);
		} else if (modelExists(block)) {
			simpleBlock(block, models().getExistingFile(prefix(name)));
		} else {
			simpleBlock(block);
		}
	}

	private void tankPedestalBlock(Block block, ModelFile base, ModelFile connector) {
		getMultipartBuilder(block).part().modelFile(base).addModel().end()
			.part().modelFile(connector).addModel().condition(BlockStateProperties.NORTH, true).end()
			.part().modelFile(connector).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end()
			.part().modelFile(connector).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
			.part().modelFile(connector).rotationY(270).addModel().condition(BlockStateProperties.WEST, true).end();
	}

	private void pipeBlock(ElementPipeBlock block, String name, String texture) {
		ModelFile core = models().withExistingParent(name + CORE, prefix("template_elementpipe_core")).texture("texture", prefix(texture));
		ModelFile frame = models().getExistingFile(prefix("cover_frame"));
		
		getMultipartBuilder(block).part().modelFile(core).addModel().end()
			.part().modelFile(frame).uvLock(true).addModel().condition(ElementPipeBlock.COVER, CoverType.FRAME).end();
	}

	private void slabBlock(SlabBlock block) {
		String name = block.getRegistryName().getPath();
		ResourceLocation sourceName = prefix(name.substring(0, name.length() - 5));
		ModelFile bottom = models().slab(name, sourceName, sourceName, sourceName);
		ModelFile top = models().slabTop(name + "_top", sourceName, sourceName, sourceName);
		ModelFile full = models().getExistingFile(sourceName);

		slabBlock(block, bottom, top, full);
	}

	private void stairsBlock(StairsBlock block) {
		String name = block.getRegistryName().getPath();
		ResourceLocation sourceName = prefix(name.substring(0, name.length() - 7));
		ModelFile stair = models().stairs(name, sourceName, sourceName, sourceName);
		ModelFile inner = models().stairsInner(name + "_inner", sourceName, sourceName, sourceName);
		ModelFile outer = models().stairsOuter(name + "_outer", sourceName, sourceName, sourceName);

		stairsBlock(block, stair, inner, outer);
	}

	private void wallBlock(WallBlock block) {
		String name = block.getRegistryName().getPath();
		ResourceLocation sourceName = prefix(name.substring(0, name.length() - 5));
		ModelFile post = models().wallPost(name + "_post", sourceName);
		ModelFile side = models().wallSide(name + SIDE, sourceName);
		ModelFile sideTall = models().wallSideTall(name + "_side_tall", sourceName);

		wallBlock(block, post, side, sideTall);
	}

	private void paneBlock(PaneBlock block) {
		String name = block.getRegistryName().getPath();
		ResourceLocation sourceName = prefix(name.substring(0, name.length() - 5));

		paneBlock(block, sourceName, sourceName);
	}

	public void fenceBlock(FenceBlock block, ResourceLocation postTexture, ResourceLocation sideTexture) {
		String baseName = block.getRegistryName().toString();

		fourWayBlock(block, models().fencePost(baseName + "_post", postTexture), models().fenceSide(baseName + SIDE, sideTexture));
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft Blockstates";
	}
}
