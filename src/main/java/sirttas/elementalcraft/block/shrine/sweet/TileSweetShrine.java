package sirttas.elementalcraft.block.shrine.sweet;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileSweetShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSweetShrine.NAME) public static TileEntityType<TileSweetShrine> TYPE;

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

		if (this.getElementAmount() >= consumeAmount) {
			getEntities(PlayerEntity.class).forEach(e -> {
				if (this.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.getFoodStats().addStats(1, 0);
				}
			});
			getEntities(BeeEntity.class).forEach(e -> {
				if (this.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.setHasNectar(true);
				}
			});
		}
		return false;
	}
}
