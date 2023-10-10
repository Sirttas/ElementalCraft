package sirttas.elementalcraft.block.shrine.sweet;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.ArrayList;
import java.util.List;

public class SweetShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(SweetShrineBlock.NAME);

	public SweetShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SWEET_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private <T extends Entity> List<T> getEntities(Class<T> clazz) {
		return new ArrayList<>(this.getLevel().getEntitiesOfClass(clazz, getRange(), e -> !e.isSpectator()));
	}

	@Override
	protected boolean doPeriod() {
		int consumeAmount = this.getConsumeAmount();

		if (this.hasUpgrade(ShrineUpgrades.NECTAR)) {
			getEntities(Bee.class).forEach(e -> {
				if (this.elementStorage.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.setHasNectar(true);
				}
			});
		} else {
			getEntities(Player.class).forEach(e -> {
				var foodData = e.getFoodData();

				if (this.elementStorage.getElementAmount() >= consumeAmount && foodData.getSaturationLevel() < 20) {
					this.consumeElement(consumeAmount);
					foodData.eat((int) Math.round(this.getStrength()), (float) this.getStrength(1));
				}
			});
		}
		return false;
	}
}
