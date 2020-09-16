package sirttas.elementalcraft.block.shrine.sweet;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileSweetShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSweetShrine.NAME) public static TileEntityType<TileSweetShrine> TYPE;


	public TileSweetShrine() {
		super(TYPE, ElementType.WATER, ECConfig.CONFIG.sweetShrinePeriode.get());
	}

	private <T extends Entity> List<T> getEntities(Class<T> clazz) {
		return this.getWorld().getEntitiesWithinAABB(clazz, new AxisAlignedBB(this.getPos()).grow(ECConfig.CONFIG.sweetShrineRange.get()), e -> !e.isSpectator()).stream()
				.collect(Collectors.toList());
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.sweetShrineConsumeAmount.get();

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
	}
}
