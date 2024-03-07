package sirttas.elementalcraft.block.synthesizer.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.EmptyElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractECContainerBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;
import sirttas.elementalcraft.container.IRuneableBlockEntity;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SolarSynthesizerBlockEntity extends AbstractECContainerBlockEntity implements IContainerTopBlockEntity, IRuneableBlockEntity, IElementStorageBlocKEntity {

	private final SingleItemContainer inventory;
	private final RuneHandler runeHandler;

	protected final int multiplier;
	protected boolean working;
	private ISingleElementStorage containerCache; // TODO use capability cache

	public SolarSynthesizerBlockEntity(BlockPos pos, BlockState state) {
		this(ECBlockEntityTypes.SOLAR_SYNTHESIZER, ECConfig.COMMON.solarSynthesizerLensElementMultiplier.get(), pos, state);
	}

	protected SolarSynthesizerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, int multiplier, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
		runeHandler = new RuneHandler(ECConfig.COMMON.solarSynthesizerMaxRunes.get(), this::setChanged);
		this.multiplier = multiplier;
		working = false;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SolarSynthesizerBlockEntity solarSynthesizer) {
		if (level.dimensionType().hasSkyLight() && level.canSeeSky(pos) && level.isDay()) {
			var synthesized = solarSynthesizer.handleSynthesis(solarSynthesizer.multiplier);

			if (synthesized > 0) {
				solarSynthesizer.breakLens(level, pos);
			}
		} else {
			solarSynthesizer.working = false;
		}
	}

	protected void breakLens(Level level, BlockPos pos) {
		ItemStack stack = inventory.getItem(0);

		if (!stack.isEmpty() && stack.getDamageValue() >= stack.getMaxDamage()) {
			Vec3 position = Vec3.atCenterOf(pos).add(0, 6.5 / 16, 0);

			inventory.setItem(0, ItemStack.EMPTY);
			level.playSound(null, position.x(), position.y(), position.z(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
			ParticleHelper.createItemBreakParticle(level, position, level.random, stack, 3);
			setChanged();
		}
	}

	protected int handleSynthesis(float amount) {
		ISingleElementStorage container = getContainer();

		if (container != null) {
			var synthesized = runeHandler.handleElementTransfer(getElementStorage(), container, amount);
			var hasSynthesized = synthesized > 0;
			
			if (hasSynthesized || working) {
				working = hasSynthesized;
				setChanged();
			}
			return synthesized;
		}
		return 0;
	}

	public boolean isWorking() {
		return working;
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
		working = compound.getBoolean(ECNames.WORKING);
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
		compound.putBoolean(ECNames.WORKING, working);
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	@Override
	public ISingleElementStorage getContainer() {
		if (containerCache == null) {
			containerCache = IContainerTopBlockEntity.super.getContainer();
		}
		return containerCache;
	}

	@Nonnull
	@Override
	public ISingleElementStorage getElementStorage() {
		var item = getInventory().getItem(0);

		if (item.isEmpty()) {
			return EmptyElementStorage.getSingle(ElementType.NONE);
		}

		var storage = item.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM_LENS, multiplier);

		if (storage == null) {
			return EmptyElementStorage.getSingle(ElementType.NONE);
		}
		return storage;
	}

	@Override
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}
}
