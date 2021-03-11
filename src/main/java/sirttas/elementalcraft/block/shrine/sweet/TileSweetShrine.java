package sirttas.elementalcraft.block.shrine.sweet;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;

public class TileSweetShrine extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSweetShrine.NAME) public static final TileEntityType<TileSweetShrine> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).periode(ECConfig.COMMON.sweetShrinePeriode.get()).consumeAmount(ECConfig.COMMON.sweetShrineConsumeAmount.get())
			.range(ECConfig.COMMON.sweetShrineRange.get());

	public TileSweetShrine() {
		super(TYPE, PROPERTIES);
	}

	private <T extends Entity> List<T> getEntities(Class<T> clazz) {
		return this.getWorld().getEntitiesWithinAABB(clazz, getRangeBoundingBox(), e -> !e.isSpectator()).stream().collect(Collectors.toList());
	}


	@Override
	protected boolean doTick() {
		int consumeAmount = this.getConsumeAmount();

		if (this.hasUpgrade(ShrineUpgrades.NECTAR)) {
			getEntities(BeeEntity.class).forEach(e -> {
				if (this.elementStorage.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.setHasNectar(true);
				}
			});
		} else {
			getEntities(PlayerEntity.class).forEach(e -> {
				float strength = this.getMultiplier(BonusType.STRENGTH);

				if (this.elementStorage.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.getFoodStats().addStats((int) (ECConfig.COMMON.sweetShrineFood.get() * strength), (float) (ECConfig.COMMON.sweetShrineSaturation.get() * strength));
				}
			});
		}
		return false;
	}
}
