package sirttas.elementalcraft.block.shrine.spring;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

public class SpringShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<@NotNull IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(SpringShrineBlock.NAME);
	private static final Holder<@NotNull IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    private static final FluidStackTemplate WATER_TEMPLATE = new FluidStackTemplate(Fluids.WATER, 1);

	public SpringShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SPRING_SHRINE, PROPERTIES, pos, state);
	}

	private BlockPos above() {
		return this.getTargetPos().above();
	}

	@Override
	protected boolean doPeriod() {
		var fillingDirection = getUpgradeDirection(ShrineUpgrades.FILLING);

		if (fillingDirection != null) {
			return MeltingShrineBlockEntity.fill(this, fillingDirection, WATER_TEMPLATE);
		}
		return ((BucketItem) Items.WATER_BUCKET).emptyContents(null, level, above(), null);
	}
}
