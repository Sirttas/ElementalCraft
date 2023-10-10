package sirttas.elementalcraft.block.shrine.enderlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.entity.EntityHelper;

import java.util.List;

public class EnderLockShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(EnderLockShrineBlock.NAME);
	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public EnderLockShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.ENDER_LOCK_SHRINE, pos, state, PROPERTIES_KEY);
	}

	@Override
	public void onLoad() {
		EnderLockHandler.add(this);
	}
	
	@Override
	protected boolean doPeriod() {
		return false;
	}

	public boolean doLock(Entity entity) {
		int consumeAmount = this.getConsumeAmount();

		if ((!this.hasUpgrade(ShrineUpgrades.PROTECTION) || EntityHelper.isHostile(entity)) && isInRange(entity.getPosition(0)) && (this.elementStorage.getElementAmount() >= consumeAmount)) {
			this.consumeElement(consumeAmount);
			return true;
		}
		return false;
	}

	private boolean isInRange(Vec3 position) {
		var range = this.getRange();

		return ShapeHelper.vectorCollideWithBoundingBox(range, position);
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
