package sirttas.elementalcraft.loot;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.enchantment.ECEnchantmentHelper;

import java.util.List;

public class LootHelper {

	private LootHelper() {}
	
	public static List<ItemStack> getDrops(ServerLevel level, BlockPos pos) {
		return getDrops(level, pos, ItemStack.EMPTY);
	}

	public static List<ItemStack> getDrops(ServerLevel level, BlockPos pos, boolean silkTouch) {
		if (silkTouch) {
			var stack = new ItemStack(Items.NETHERITE_PICKAXE);

			stack.enchant(ECEnchantmentHelper.getEnchantmentHolder(level.registryAccess(), Enchantments.SILK_TOUCH), 1);
			return getDrops(level, pos, stack);
		}
		return getDrops(level, pos);
	}

	public static List<ItemStack> getDrops(ServerLevel level, BlockPos pos, ItemStack stack) {
		var state = level.getBlockState(pos);

		return state.getDrops(new LootParams.Builder(level)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withParameter(LootContextParams.TOOL, stack));
	}
}
