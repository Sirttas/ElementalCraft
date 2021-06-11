package sirttas.elementalcraft.block.solarsynthesizer;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
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
import sirttas.elementalcraft.block.entity.AbstractECContainerBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;

public class SolarSynthesizerBlockEntity extends AbstractECContainerBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SolarSynthesizerBlock.NAME) public static final TileEntityType<SolarSynthesizerBlockEntity> TYPE = null;

	private final SingleItemInventory inventory;
	private final RuneHandler runeHandler;
	private boolean working;

	public SolarSynthesizerBlockEntity() {
		super(TYPE);
		inventory = new SingleItemInventory(this::setChanged);
		runeHandler = new RuneHandler(2);
		working = false;
	}


	@Override
	public void tick() {
		super.tick();
		ISingleElementStorage tank = getTank();
		
		if (tank != null && level.dimensionType().hasSkyLight() && level.canSeeSky(worldPosition) && level.isDay()) {
			ItemStack stack = inventory.getItem(0);
			boolean hasExtract = getElementStorage()
					.map(storage -> storage.transferTo(tank, runeHandler.getTransferSpeed(ECConfig.COMMON.lenseElementMultiplier.get()), runeHandler.getElementPreservation()) > 0).orElse(false);
			
			if (hasExtract || working) {
				working = hasExtract;
				this.setChanged();
			}
			if (!stack.isEmpty() && stack.getDamageValue() >= stack.getMaxDamage()) {
				Vector3d position = Vector3d.atCenterOf(worldPosition).add(0, 6.5 / 16, 0);
				
				inventory.setItem(0, ItemStack.EMPTY);
				level.playLocalSound(position.x(), position.y(), position.z(), SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F, false);
				ParticleHelper.createItemBreakParticle(level, position, level.random, stack, 3);
				this.setChanged();
			}
		}
	}

	public boolean isWorking() {
		return working;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
		working = compound.getBoolean(ECNames.WORKING);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
		compound.putBoolean(ECNames.WORKING, working);
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove) {
			if (cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
				return CapabilityElementStorage.get(inventory.getItem(0)).cast();
			} else if (cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
				return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	public Optional<ISingleElementStorage> getElementStorage() {
		return CapabilityElementStorage.get(this).filter(ISingleElementStorage.class::isInstance).map(ISingleElementStorage.class::cast);
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}
}
