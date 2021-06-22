package sirttas.elementalcraft.block.diffuser;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECTickableBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class DiffuserBlockEntity extends AbstractECTickableBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + DiffuserBlock.NAME) public static final TileEntityType<DiffuserBlockEntity> TYPE = null;

	private boolean hasDiffused;
	private final RuneHandler runeHandler;

	public DiffuserBlockEntity() {
		super(TYPE);
		runeHandler = new RuneHandler(ECConfig.COMMON.diffuserMaxRunes.get());
	}


	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		hasDiffused = compound.getBoolean(ECNames.HAS_DIFFUSED);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putBoolean(ECNames.HAS_DIFFUSED, hasDiffused);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
		return compound;
	}

	@Override
	public void tick() {
		ISingleElementStorage tank = getTank();
		AtomicInteger amount = new AtomicInteger(ECConfig.COMMON.diffuserDiffusionAmount.get());
		
		hasDiffused = false;
		super.tick();
		if (tank != null && !tank.isEmpty()) {
			this.getLevel().getEntities(null, new AxisAlignedBB(this.getBlockPos()).inflate(ECConfig.COMMON.diffuserRange.get())).stream()
					.map(CapabilityElementStorage::get)
					.map(LazyOptional::resolve)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.forEach(storage -> {
						if (!tank.isEmpty() && amount.get() > 0) {
							amount.set(tank.transferTo(storage, runeHandler.getTransferSpeed(amount.get()), runeHandler.getElementPreservation()));
							hasDiffused = true;
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
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
		}
		return super.getCapability(cap, side);
	}
}
