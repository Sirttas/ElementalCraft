package sirttas.elementalcraft.block.shrine;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECTickableBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.config.ECConfig;

public abstract class AbstractShrineBlockEntity extends AbstractECTickableBlockEntity {

	protected static final List<Direction> DEFAULT_UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	private final double basePeriode;
	private final int baseElementCapacity;
	private final float baseRange;
	private final int baseConsumeAmount;
	private final Map<Direction, ShrineUpgrade> upgrades = new EnumMap<>(Direction.class);
	private final Map<ShrineUpgrade.BonusType, Float> upgradeMultipliers = new EnumMap<>(ShrineUpgrade.BonusType.class);
	protected final ShrineElementStorage elementStorage;

	private boolean running = false;
	private double tick = 0;
	private int rangeRenderTimer = 0;

	protected AbstractShrineBlockEntity(TileEntityType<?> tileEntityTypeIn, Properties properties) {
		super(tileEntityTypeIn);
		basePeriode = properties.periode;
		baseElementCapacity = properties.capacity;
		baseRange = properties.range;
		baseConsumeAmount = properties.consumeAmount;
		if (basePeriode <= 0) {
			throw new IllegalArgumentException("Shrine periode should not be 0");
		}
		elementStorage = new ShrineElementStorage(properties.elementType, properties.capacity, this::setChanged);
	}


	protected int consumeElement() {
		return consumeElement(this.getConsumeAmount());
	}

	protected int consumeElement(int i) {
		return elementStorage.extractElement(i, false);
	}

	protected abstract boolean doTick();

	@Override
	public final void tick() {
		if (this.isDirty()) {
			refreshUpgrades();
		}
		double periode = getPeriod();
		int consumeAmount = this.getConsumeAmount();

		super.tick();
		running = false;
		if (this.hasLevel() && !this.isPowered()) {
			tick++;
			if (periode <= 0) {
				ElementalCraftApi.LOGGER.warn("Shrine periode should not be 0");
				periode = 1;
			}
			while (tick >= periode) {
				if (elementStorage.getElementAmount() >= consumeAmount && doTick()) {
					this.consumeElement();
				}
				tick -= periode;
			}
		}
		if (rangeRenderTimer > 0) {
			rangeRenderTimer--;
		}
	}

	public void refreshUpgrades() {
		if (this.hasLevel()) {
			this.upgrades.clear();
			this.upgradeMultipliers.clear();
			getUpgradeDirections().forEach(direction -> {
				BlockPos pos = getBlockPos().relative(direction);
				BlockState state = this.getLevel().getBlockState(pos);
				Block block = state.getBlock();
	
				if (block instanceof AbstractShrineUpgradeBlock) {
					ShrineUpgrade upgrade = ((AbstractShrineUpgradeBlock) block).getUpgrade();

					if (upgrade != null) {
						setUpgrade(direction, upgrade);
					}
				}
			});
			getUpgradeDirections().forEach(direction -> {
				BlockPos pos = getBlockPos().relative(direction);
				BlockState state = this.getLevel().getBlockState(pos);
				
				if (!state.canSurvive(this.level, pos)) {
					this.level.destroyBlock(pos, true);
				}
			});
		}
	}

	protected float getMultiplier(ShrineUpgrade.BonusType type) {
		return this.upgradeMultipliers.getOrDefault(type, 1F);
	}

	public int getUpgradeCount(ShrineUpgrade upgrade) {
		return upgrade == null ? 0 : (int) upgrades.values().stream().filter(upgrade::equals).count();
	}

	public boolean hasUpgrade(ShrineUpgrade upgrade) {
		return getUpgradeCount(upgrade) > 0;
	}

	private void setUpgrade(Direction direction, ShrineUpgrade upgrade) {
		ShrineUpgrade old = upgrades.get(direction);

		if (old != null) {
			old.getBonuses().forEach((type, bonus) -> upgradeMultipliers.put(type, getMultiplier(type) / bonus));
		}
		upgrades.put(direction, upgrade);
		upgrade.getBonuses().forEach((type, bonus) -> upgradeMultipliers.put(type, getMultiplier(type) * bonus));
		elementStorage.setCapacity((int) (this.baseElementCapacity * getMultiplier(BonusType.CAPACITY)));
	}

	public Collection<ShrineUpgrade> getAllUpgrades() {
		return upgrades.values();
	}

	public List<Direction> getUpgradeDirections() {
		return DEFAULT_UPGRRADE_DIRECTIONS;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean showsRange() {
		return this.rangeRenderTimer > 0;
	}

	@OnlyIn(Dist.CLIENT)
	public void startShowingRange() {
		this.rangeRenderTimer = 600;
	}

	public AxisAlignedBB getRangeBoundingBox() {
		return new AxisAlignedBB(this.getBlockPos()).inflate(this.getRange());
	}

	public float getRange() {
		return baseRange * getMultiplier(BonusType.RANGE);
	}

	public int getIntegerRange() {
		return Math.round(getRange());
	}

	public int getConsumeAmount() {
		return Math.round(baseConsumeAmount * getMultiplier(BonusType.ELEMENT_CONSUMPTION));
	}

	public double getPeriod() {
		return this.basePeriode * getMultiplier(BonusType.SPEED);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		running = compound.getBoolean(ECNames.RUNNING);
		refreshUpgrades();
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
		compound.putBoolean(ECNames.RUNNING, running);
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}

	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	public static final class Properties {
		private double periode;
		private int consumeAmount;
		private int capacity;
		private float range;
		private ElementType elementType;

		private Properties(ElementType elementType) {
			this.elementType = elementType;
			consumeAmount = 0;
			periode = 1;
			range = 1;
			capacity = ECConfig.COMMON.shrinesCapacity.get();
		}

		public static Properties create(ElementType elementType) {
			return new Properties(elementType);
		}


		public Properties consumeAmount(int consumeAmount) {
			this.consumeAmount = consumeAmount;
			return this;
		}

		public Properties periode(double periode) {
			this.periode = periode;
			return this;
		}

		public Properties capacity(int capacity) {
			this.capacity = capacity;
			return this;
		}

		public Properties range(float range) {
			this.range = range;
			return this;
		}
	}
}
