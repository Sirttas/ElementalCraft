package sirttas.elementalcraft.loot;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class FireInfusionLootModifier extends LootModifier {

	private static final LootItemFunction FORTUNE = ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE).build();
	
	protected FireInfusionLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	private ItemStack applyAutoSmelt(ItemStack stack, LootContext context) {
		var recipe = context.getLevel().getRecipeManager().byType(RecipeType.SMELTING).values().stream()
				.filter(r -> r.getIngredients().get(0).test(stack))
				.findFirst();

		if (recipe.isPresent()) {
			ItemStack ret = recipe.get().getResultItem().copy();

			ret.setCount(ret.getCount() * stack.getCount());
			if (stack.is(Tags.Items.ORES)) {
				FORTUNE.apply(ret, context);
			}
			return ret;
		}
		return stack;
	}

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

		if (tool != null && !tool.isEmpty() && ToolInfusionHelper.hasAutoSmelt(tool)) {
			return generatedLoot.stream()
					.map(s -> applyAutoSmelt(s, context))
					.collect(Collectors.toCollection(ObjectArrayList::new));
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<FireInfusionLootModifier> {
		@Override
		public FireInfusionLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
			return new FireInfusionLootModifier(conditions);
		}

		@Override
		public JsonObject write(FireInfusionLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
