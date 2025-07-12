package sirttas.elementalcraft.block.extractor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.retriever.RetrieverBlock;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleStackContainer;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ElementExtractorBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity {
	private final int extractionAmount;
	private final RuneHandler runeHandler;
	private BlockCapabilityCache<IElementStorage, Direction> sourceCache;

	private ISingleElementStorage containerCache; // TODO use capability cache

	public ElementExtractorBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.EXTRACTOR, pos, state);
		if (state.is(ECBlocks.RUDIMENTARY_EXTRACTOR.get())) {
			this.extractionAmount = ECConfig.SERVER.rudimentaryExtractorExtractionAmount.get();
			this.runeHandler = new RuneHandler(ECConfig.SERVER.rudimentaryExtractorMaxRunes.get(), this::setChanged);
		} else if (state.is(ECBlocks.IMPROVED_EXTRACTOR.get())) {
			this.extractionAmount = ECConfig.SERVER.improvedExtractorExtractionAmount.get();
			this.runeHandler = new RuneHandler(ECConfig.SERVER.improvedExtractorMaxRunes.get(), this::setChanged);
		} else {
			this.extractionAmount = ECConfig.SERVER.extractorExtractionAmount.get();
			this.runeHandler = new RuneHandler(ECConfig.SERVER.extractorMaxRunes.get(), this::setChanged);
		}
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	protected Optional<BlockState> getSourceState() {
		return this.level != null ? Optional.of(this.level.getBlockState(worldPosition.above())) : Optional.empty();
	}

	public ElementType getSourceElementType() {
		return getSourceState()
				.filter(s -> s.is(ECTags.Blocks.SOURCES))
				.map(ElementType::getElementType)
				.orElse(ElementType.NONE);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, ElementExtractorBlockEntity extractor) {
		var above = pos.above();

		if (extractor.sourceCache == null) {
			extractor.sourceCache = BlockCapabilityCache.create(ElementalCraftCapabilities.ElementStorage.BLOCK, (ServerLevel) level, above, Direction.DOWN, () -> !extractor.isRemoved(), () -> {});
		}

        if (!extractor.canExtract() || !(extractor.sourceCache.getCapability() instanceof SourceElementStorage sourceStorage)) {
            return;
        }

        extractor.runeHandler.handleElementTransfer(sourceStorage, extractor.getContainer(), extractor.extractionAmount);

        if (sourceStorage.getElementAmount() <= 0) {
            if (sourceStorage.getSource().isStabilized()) {
                var inv = new SingleStackContainer();

                inv.setItem(0, new ItemStack(ECItems.SOURCE_STABILIZER));
                RetrieverBlock.sendOutputToRetriever(level, pos, inv, 0);
                if (!inv.isEmpty()) {
                    Block.popResource(level, above, inv.getItem(0));
                }
            }
            level.removeBlock(above, false);
        }
    }

	public boolean canExtract() {
		if (this.level == null || this.isPowered()) {
			return false;
		}

		var sourceStorage = sourceCache != null && sourceCache.getCapability() instanceof SourceElementStorage s ? s : null;

		if (sourceStorage == null) {
			sourceStorage = level.getCapability(ElementalCraftCapabilities.ElementStorage.BLOCK, worldPosition.above(), Direction.DOWN) instanceof SourceElementStorage s ? s : null;
		}

		if (sourceStorage == null) {
			return false;
		}

		ElementType sourceElementType = sourceStorage.getElementType();
		ISingleElementStorage container = getContainer();

		return hasLevel() && sourceElementType != ElementType.NONE && container != null && (container.getElementAmount() < container.getElementCapacity() || container.getElementType() != sourceElementType);
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

	@Override
	public ISingleElementStorage getContainer() {
		if (containerCache == null) {
			containerCache = IContainerTopBlockEntity.super.getContainer();
		}
		return containerCache;
	}
}
