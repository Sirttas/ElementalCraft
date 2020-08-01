package sirttas.elementalcraft.block.shrine.sweet;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;
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

	private List<PlayerEntity> getEntities() {
		return this.getWorld().getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPos()).grow(ECConfig.CONFIG.sweetShrineRange.get()),
				e -> (!e.isSpectator() && e instanceof PlayerEntity)).stream().map(PlayerEntity.class::cast).collect(Collectors.toList());
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.overloadShrineConsumeAmount.get();

		if (this.hasWorld() && world instanceof ServerWorld && this.getElementAmount() >= consumeAmount) {
			getEntities().forEach(e -> {
				if (this.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					e.getFoodStats().addStats(1, 0);
				}
			});
		}
	}
}
