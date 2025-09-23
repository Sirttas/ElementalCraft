package sirttas.elementalcraft.block.diffuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.range.RangeRenderTimer;
import sirttas.elementalcraft.range.Ranges;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class DiffuserBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity {

	private static final Holder<Range> RANGE = ElementalCraftApi.RANGE_MANAGER.getOrCreateHolder(Ranges.DIFFUSER);

	private boolean hasDiffused;
	private final RuneHandler runeHandler;
	private final RangeRenderTimer rangeRenderTimer;
	private ISingleElementStorage containerCache; // TODO use capability cache

	public DiffuserBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.DIFFUSER, pos, state);
		runeHandler = new RuneHandler(ECConfig.SERVER.diffuserMaxRunes.get(), this::setChanged);
		rangeRenderTimer = new RangeRenderTimer();
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		hasDiffused = compound.getBoolean(ECNames.HAS_DIFFUSED);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, Tag.OBJECT_HEADER));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.putBoolean(ECNames.HAS_DIFFUSED, hasDiffused);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	@SuppressWarnings("unused")
	public static void serverTick(Level level, BlockPos pos, BlockState state, DiffuserBlockEntity diffuser) {
		var container = diffuser.getContainer();
		var amount = new AtomicInteger(ECConfig.SERVER.diffuserDiffusionAmount.get());

		diffuser.hasDiffused = false;
		if (container != null && !container.isEmpty()) {
			level.getEntities(null, diffuser.getRange()).stream()
					.map(e -> e.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY, null))
					.filter(Objects::nonNull)
					.forEach(storage -> {
						if (!container.isEmpty() && amount.get() > 0 && container.transferTo(storage, container.getElementType(), diffuser.runeHandler.getTransferSpeed(amount.get()), Math.min(1, diffuser.runeHandler.getElementPreservation())) > 0) {
							diffuser.hasDiffused = true;
						}
					});
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, DiffuserBlockEntity diffuser) {
		diffuser.rangeRenderTimer.tick();
	}

	public boolean showsRange() {
		return rangeRenderTimer.showsRange();
	}

	public void startShowingRange() {
		rangeRenderTimer.startShowingRange();
	}

	public AABB getRange() {
		return this.runeHandler.getRange(RANGE.value()).move(this.worldPosition);
	}

	public boolean hasDiffused() {
		return hasDiffused;
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
