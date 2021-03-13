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
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.AbstractTileECContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleItemInventory;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.handler.RuneHandler;

public class TileSolarSynthesizer extends AbstractTileECContainer {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSolarSynthesizer.NAME) public static final TileEntityType<TileSolarSynthesizer> TYPE = null;

	private final SingleItemInventory inventory;
	private final RuneHandler runeHandler;
	private boolean working;

	public TileSolarSynthesizer() {
		super(TYPE);
		inventory = new SingleItemInventory(this::markDirty);
		runeHandler = new RuneHandler(2);
		working = false;
	}


	@Override
	public void tick() {
		super.tick();
		ISingleElementStorage tank = getTank();
		
		if (tank != null && world.getDimensionType().hasSkyLight() && world.canSeeSky(pos) && world.isDaytime()) {
			ItemStack stack = inventory.getStackInSlot(0);
			boolean hasExtract = getElementStorage()
					.map(storage -> storage.transferTo(tank, runeHandler.getTransferSpeed(ECConfig.COMMON.lenseElementMultiplier.get()), runeHandler.getElementPreservation()) > 0).orElse(false);
			
			if (hasExtract || working) {
				working = hasExtract;
				this.markDirty();
			}
			if (!stack.isEmpty() && stack.getDamage() >= stack.getMaxDamage()) {
				Vector3d position = Vector3d.copyCentered(pos).add(0, 6.5 / 16, 0);
				
				inventory.setInventorySlotContents(0, ItemStack.EMPTY);
				world.playSound(position.getX(), position.getY(), position.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + world.rand.nextFloat() * 0.4F, false);
				ParticleHelper.createItemBreakParticle(world, position, world.rand, stack, 3);
				this.markDirty();
			}
		}
	}

	public boolean isWorking() {
		return working;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.readNBT(runeHandler, null, compound.get(ECNames.RUNE_HANDLER));
		}
		working = compound.getBoolean(ECNames.WORKING);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put(ECNames.RUNE_HANDLER, CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.writeNBT(runeHandler, null));
		compound.putBoolean(ECNames.WORKING, working);
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed) {
			if (cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
				return CapabilityElementStorage.get(inventory.getStackInSlot(0)).cast();
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
