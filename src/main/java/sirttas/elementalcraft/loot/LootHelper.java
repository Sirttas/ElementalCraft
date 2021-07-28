package sirttas.elementalcraft.loot;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

public class LootHelper {

	private static ItemStack silkTouchPickaxe = ItemStack.EMPTY;

	private LootHelper() {}
	
	public static List<ItemStack> getDrops(ServerLevel world, BlockPos pos) {
		return getDrops(world, pos, ItemStack.EMPTY);
	}

	public static List<ItemStack> getDrops(ServerLevel world, BlockPos pos, boolean silkTouch) {
		if (silkTouchPickaxe.isEmpty() && silkTouch) {
			silkTouchPickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
			silkTouchPickaxe.enchant(Enchantments.SILK_TOUCH, 1);
		}

		return getDrops(world, pos, silkTouch ? silkTouchPickaxe.copy() : ItemStack.EMPTY);
	}

	public static List<ItemStack> getDrops(ServerLevel world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);
		return state.getDrops(new LootContext.Builder(world).withRandom(world.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withParameter(LootContextParams.TOOL, stack));
	}
}
