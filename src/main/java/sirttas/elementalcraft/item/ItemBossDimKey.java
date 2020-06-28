package sirttas.elementalcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import sirttas.elementalcraft.world.dimension.boss.BossDimension;

public class ItemBossDimKey extends ItemEC {

	public static final String NAME = "bossdimkey";

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.changeDimension(BossDimension.getDimensionType(), BossDimension.TELEPORTER);
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
