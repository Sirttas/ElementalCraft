package sirttas.elementalcraft.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class FireInfusionLootModifier extends LootModifier {

	public static final Codec<FireInfusionLootModifier> DIRECT_CODEC = RecordCodecBuilder.create(i -> codecStart(i).apply(i, FireInfusionLootModifier::new));

	private static final LootItemFunction FORTUNE = ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE).build();
	
	protected FireInfusionLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	private ItemStack applyAutoSmelt(ItemStack stack, LootContext context) {
		var level = context.getLevel();
		var recipe = level.getRecipeManager().byType(RecipeType.SMELTING).values().stream()
				.map(RecipeHolder::value)
				.filter(r -> r.getIngredients().get(0).test(stack))
				.findFirst();

		if (recipe.isPresent()) {
			ItemStack ret = recipe.get().getResultItem(level.registryAccess()).copy();

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
		var tool = context.getParamOrNull(LootContextParams.TOOL);

		if (tool != null && !tool.isEmpty() && ToolInfusionHelper.hasAutoSmelt(tool)) {
			return generatedLoot.stream()
					.map(s -> applyAutoSmelt(s, context))
					.collect(Collectors.toCollection(ObjectArrayList::new));
		}
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return DIRECT_CODEC;
	}
}
