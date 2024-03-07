package sirttas.elementalcraft.block.diffuser;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class DiffuserBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity {

	private boolean hasDiffused;
	private final RuneHandler runeHandler;

	private int progress = 0;
	private ISingleElementStorage containerCache; // TODO use capability cache

	public DiffuserBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.DIFFUSER, pos, state);
		runeHandler = new RuneHandler(ECConfig.COMMON.diffuserMaxRunes.get(), this::setChanged);
	}


	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		hasDiffused = compound.getBoolean(ECNames.HAS_DIFFUSED);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putBoolean(ECNames.HAS_DIFFUSED, hasDiffused);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	@SuppressWarnings("unused")
	public static void serverTick(Level level, BlockPos pos, BlockState state, DiffuserBlockEntity diffuser) {
		ISingleElementStorage container = diffuser.getContainer();
		AtomicInteger amount = new AtomicInteger(ECConfig.COMMON.diffuserDiffusionAmount.get());
		
		diffuser.hasDiffused = false;
		if (container != null && !container.isEmpty()) {
			diffuser.getLevel().getEntities(null, new AABB(diffuser.getBlockPos()).inflate(ECConfig.COMMON.diffuserRange.get())).stream()
					.map(e -> e.getCapability(ElementalCraftCapabilities.ElementStorage.ENTITY, null))
					.filter(Objects::nonNull)
					.forEach(storage -> {
						if (!container.isEmpty() && amount.get() > 0 && container.transferTo(storage, container.getElementType(), diffuser.runeHandler.getTransferSpeed(amount.get()), Math.min(1, diffuser.runeHandler.getElementPreservation())) > 0) {
							diffuser.hasDiffused = true;
						}
					});
		}
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
