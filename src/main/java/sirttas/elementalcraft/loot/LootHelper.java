package sirttas.elementalcraft.loot;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class LootHelper {

	private static ItemStack silkTouchPickaxe = ItemStack.EMPTY;

	private LootHelper() {}
	
	public static List<ItemStack> getDrops(ServerWorld world, BlockPos pos) {
		return getDrops(world, pos, ItemStack.EMPTY);
	}

	public static List<ItemStack> getDrops(ServerWorld world, BlockPos pos, boolean silkTouch) {
		if (silkTouchPickaxe.isEmpty() && silkTouch) {
			silkTouchPickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
			silkTouchPickaxe.addEnchantment(Enchantments.SILK_TOUCH, 1);
		}

		return getDrops(world, pos, silkTouch ? silkTouchPickaxe.copy() : ItemStack.EMPTY);
	}

	public static List<ItemStack> getDrops(ServerWorld world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);
		return state.getDrops(new LootContext.Builder(world).withRandom(world.rand).withParameter(LootParameters.ORIGIN, Vector3d.copyCentered(pos))
				.withParameter(LootParameters.TOOL, stack));
	}
}
