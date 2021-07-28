package sirttas.elementalcraft.block.shrine.enderlock;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;

public class EnderLockShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + EnderLockShrineBlock.NAME) public static final BlockEntityType<EnderLockShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).consumeAmount(ECConfig.COMMON.enderLockShrineConsumeAmount.get()).range(ECConfig.COMMON.enderLockShrineRange.get());

	protected static final List<Direction> UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public EnderLockShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES);
	}

	@Override
	public AABB getRangeBoundingBox() {
		int range = ECConfig.COMMON.enderLockShrineRange.get();

		return new AABB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, 2, 0);
	}

	@Override
	protected boolean doPeriode() {
		return false;
	}

	public boolean doLock(Entity entity) {
		int consumeAmount = this.getConsumeAmount();

		if ((!this.hasUpgrade(ShrineUpgrades.PROTECTION.get()) || EntityHelper.isHostile(entity)) && (this.elementStorage.getElementAmount() >= consumeAmount)) {
			this.consumeElement(consumeAmount);
			return true;
		}
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRRADE_DIRECTIONS;
	}
}
