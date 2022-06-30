package sirttas.elementalcraft.block.diffuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DiffuserBlockEntity extends AbstractECBlockEntity implements IContainerTopBlockEntity {

	private boolean hasDiffused;
	private final RuneHandler runeHandler;

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
		ISingleElementStorage tank = diffuser.getContainer();
		AtomicInteger amount = new AtomicInteger(ECConfig.COMMON.diffuserDiffusionAmount.get());
		
		diffuser.hasDiffused = false;
		if (tank != null && !tank.isEmpty()) {
			diffuser.getLevel().getEntities(null, new AABB(diffuser.getBlockPos()).inflate(ECConfig.COMMON.diffuserRange.get())).stream()
					.map(CapabilityElementStorage::get)
					.map(LazyOptional::resolve)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.forEach(storage -> {
						if (!tank.isEmpty() && amount.get() > 0) {
							amount.set(diffuser.runeHandler.handleElementTransfer(tank, storage, amount.get()));
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
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
