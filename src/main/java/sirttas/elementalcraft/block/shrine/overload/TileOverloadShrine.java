package sirttas.elementalcraft.block.shrine.overload;

import java.util.Optional;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileOverloadShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static TileEntityType<TileOverloadShrine> TYPE;

	public TileOverloadShrine() {
		super(TYPE, ElementType.AIR);
	}

	Optional<ITickable> getTarget() {
		return Optional.ofNullable(world.getTileEntity(pos.offset(this.getBlockState().get(BlockOverloadShrine.FACING)))).filter(ITickable.class::isInstance).map(ITickable.class::cast);
	}

	@Override
	public void tick() {
		int consumeAmount = ECConfig.CONFIG.overloadShrineConsumeAmount.get();

		super.tick();
		if (this.hasWorld() && world instanceof ServerWorld && randomChance(ECConfig.CONFIG.overloadShrineChance.get()) && this.getElementAmount() >= consumeAmount) {
			getTarget().ifPresent(t -> {
				t.tick();
				this.consumeElement(consumeAmount);
			});
		}
	}
}
