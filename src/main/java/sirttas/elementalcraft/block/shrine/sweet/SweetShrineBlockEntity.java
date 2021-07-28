package sirttas.elementalcraft.block.shrine.sweet;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;

public class SweetShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SweetShrineBlock.NAME) public static final BlockEntityType<SweetShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).periode(ECConfig.COMMON.sweetShrinePeriode.get()).consumeAmount(ECConfig.COMMON.sweetShrineConsumeAmount.get())
			.range(ECConfig.COMMON.sweetShrineRange.get());

	public SweetShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES);
	}

	private <T extends Entity> List<T> getEntities(Class<T> clazz) {
		return this.getLevel().getEntitiesOfClass(clazz, getRangeBoundingBox(), e -> !e.isSpectator()).stream().collect(Collectors.toList());
	}


	@Override
	protected boolean doPeriode() {
		int consumeAmount = this.getConsumeAmount();

		if (this.hasUpgrade(ShrineUpgrades.NECTAR.get())) {
			getEntities(Bee.class).forEach(e -> {
				if (this.elementStorage.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.setHasNectar(true);
				}
			});
		} else {
			getEntities(Player.class).forEach(e -> {
				float strength = this.getMultiplier(BonusType.STRENGTH);

				if (this.elementStorage.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.getFoodData().eat((int) (ECConfig.COMMON.sweetShrineFood.get() * strength), (float) (ECConfig.COMMON.sweetShrineSaturation.get() * strength));
				}
			});
		}
		return false;
	}
}
