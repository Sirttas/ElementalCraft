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

	private static ItemStack SILK_TOUCH_PICK = ItemStack.EMPTY;

	public static List<ItemStack> getDrops(ServerWorld world, BlockPos pos, boolean silkTouch) {
		if (SILK_TOUCH_PICK.isEmpty() && silkTouch) {
			SILK_TOUCH_PICK = new ItemStack(Items.NETHERITE_PICKAXE);
			SILK_TOUCH_PICK.addEnchantment(Enchantments.SILK_TOUCH, 1);
		}

		return getDrops(world, pos, silkTouch ? SILK_TOUCH_PICK.copy() : ItemStack.EMPTY);
	}

	public static List<ItemStack> getDrops(ServerWorld world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);
		return state.getDrops(new LootContext.Builder(world).withRandom(world.rand).withParameter(LootParameters.field_237457_g_/* POSITION */, Vector3d.copyCentered(pos))
				.withParameter(LootParameters.TOOL, stack));
	}
}
