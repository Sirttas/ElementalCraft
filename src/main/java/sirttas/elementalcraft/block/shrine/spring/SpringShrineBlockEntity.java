package sirttas.elementalcraft.block.shrine.spring;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

public class SpringShrineBlockEntity extends AbstractShrineBlockEntity {


	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(SpringShrineBlock.NAME);

	public SpringShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SPRING_SHRINE, pos, state, PROPERTIES_KEY);
	}

	@Override
	public AABB getRange() {
		return new AABB(this.getTargetPos().above());
	}

	@Override
	protected boolean doPeriod() {
		var fillingDirection = getUpgradeDirection(ShrineUpgrades.FILLING);

		if (fillingDirection != null) {
			return LavaShrineBlockEntity.fill(this, fillingDirection, Fluids.WATER);
		}
		return ((BucketItem) Items.WATER_BUCKET).emptyContents(null, level, worldPosition.above(), null);
	}
}
