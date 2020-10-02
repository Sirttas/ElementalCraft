package sirttas.elementalcraft.block.shrine.overload;

import java.util.Optional;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;

public class TileOverloadShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static TileEntityType<TileOverloadShrine> TYPE;

	public TileOverloadShrine() {
		super(TYPE, ElementType.AIR, ECConfig.CONFIG.overloadShrinePeriode.get());
	}

	Optional<ITickableTileEntity> getTarget() {
		return TileEntityHelper.getTileEntityAs(world, pos.offset(this.getBlockState().get(BlockOverloadShrine.FACING)), ITickableTileEntity.class);
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.overloadShrineConsumeAmount.get();

		if (this.getElementAmount() >= consumeAmount) {
			getTarget().ifPresent(t -> {
				t.tick();
				this.consumeElement(consumeAmount);
			});
		}
	}
}
