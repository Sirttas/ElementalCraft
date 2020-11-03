package sirttas.elementalcraft.block.shrine;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.IElementReceiver;
import sirttas.elementalcraft.block.shrine.upgrade.BlockShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.ECNames;

public abstract class TileShrine extends TileECTickable implements IElementReceiver {

	protected static final List<Direction> DEFAULT_UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	private final ElementType elementType;
	private final double basePeriode;
	private final int baseElementCapacity;
	private final float baseRange;
	private final int baseConsumeAmount;
	private final Map<Direction, ShrineUpgrade> upgrades = new EnumMap<>(Direction.class);
	private final Map<ShrineUpgrade.BonusType, Float> upgradeMultipliers = new EnumMap<>(ShrineUpgrade.BonusType.class);

	private int elementAmount = 0;
	private boolean running = false;
	private double tick = 0;
	private int rangeRenderTimer = 0;

	public TileShrine(TileEntityType<?> tileEntityTypeIn, Properties properties) {
		super(tileEntityTypeIn);
		elementType = properties.elementType;
		basePeriode = properties.periode;
		baseElementCapacity = properties.capacity;
		baseRange = properties.range;
		baseConsumeAmount = properties.consumeAmount;
		if (basePeriode == 0) {
			throw new IllegalArgumentException("Shrine periode should not be 0");
		}
	}


	protected int consumeElement() {
		return consumeElement(this.getConsumeAmount());
	}

	protected int consumeElement(int i) {
		if (this.isPowered()) {
			return 0;
		}
		int newCount = Math.max(elementAmount - i, 0);
		int ret = elementAmount - newCount;

		elementAmount = newCount;
		if (ret > 0) {
			running = true;
		}
		this.forceSync();
		return ret;
	}

	@Override
	public int inserElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return 0;
		} else {
			int newCount = Math.min(elementAmount + count, getElementCapacity());
			int ret = count - newCount + elementAmount;

			if (!simulate) {
				elementAmount = newCount;
			}
			return ret;
		}
	}

	protected abstract boolean doTick();

	@Override
	public final void tick() {
		if (this.isToSync()) {
			refreshUpgrades();
		}
		double periode = getPeriod();
		int consumeAmount = this.getConsumeAmount();

		super.tick();
		running = false;
		if (this.hasWorld() && !this.isPowered()) {
			tick++;
			if (periode == 0) {
				ElementalCraft.LOGGER.warn("Shrine periode should not be 0");
				periode = 1;
			}
			while (tick >= periode) {
				if (this.getElementAmount() >= consumeAmount && doTick()) {
					this.consumeElement();
				}
				tick -= periode;
			}
		}
		if (rangeRenderTimer > 0) {
			rangeRenderTimer--;
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		refreshUpgrades();
	}

	public void refreshUpgrades() {
		if (this.hasWorld()) {
			this.upgrades.clear();
			this.upgradeMultipliers.clear();
			getUpgradeDirections().forEach(direction -> {
				BlockState state = this.getWorld().getBlockState(getPos().offset(direction));
				Block block = state.getBlock();

				if (block instanceof BlockShrineUpgrade) {
					setUpgrade(direction, ((BlockShrineUpgrade) block).getUpgrade());
				}
			});
		}
	}

	protected float getMultiplier(ShrineUpgrade.BonusType type) {
		Float val = this.upgradeMultipliers.get(type);

		if (val != null && val != 0) {
			return val;
		}
		return 1;
	}

	public int getUpgradeCount(ShrineUpgrade upgrade) {
		return upgrade == null ? 0 : (int) upgrades.values().stream().filter(upgrade::equals).count();
	}

	public boolean hasUpgrade(ShrineUpgrade upgrade) {
		return getUpgradeCount(upgrade) > 0;
	}

	public void setUpgrade(Direction direction, ShrineUpgrade upgrade) {
		ShrineUpgrade old = upgrades.get(direction);

		if (old != null) {
			old.getBonuses().forEach((type, bonus) -> upgradeMultipliers.put(type, getMultiplier(type) / bonus));
		}
		upgrades.put(direction, upgrade);
		upgrade.getBonuses().forEach((type, bonus) -> upgradeMultipliers.put(type, getMultiplier(type) * bonus));
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
		return new AxisAlignedBB(this.getPos()).grow(this.getRange());
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getElementCapacity() {
		return Math.round(baseElementCapacity * getMultiplier(BonusType.CAPACITY));
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
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		elementAmount = compound.getInt(ECNames.ELEMENT_AMOUNT);
		running = compound.getBoolean(ECNames.RUNNING);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt(ECNames.ELEMENT_AMOUNT, elementAmount);
		compound.putBoolean(ECNames.RUNNING, running);
		return compound;
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
