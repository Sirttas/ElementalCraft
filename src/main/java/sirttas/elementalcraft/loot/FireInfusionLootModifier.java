package sirttas.elementalcraft.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.enchantment.ECEnchantmentHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class FireInfusionLootModifier extends LootModifier {

	public static final MapCodec<FireInfusionLootModifier> DIRECT_CODEC = RecordCodecBuilder.mapCodec(i -> codecStart(i).apply(i, FireInfusionLootModifier::new));

	
	protected FireInfusionLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	private ItemStack applyAutoSmelt(ItemStack stack, LootContext context) {
		var level = context.getLevel();
		var registry = level.registryAccess();
        var input = new SingleRecipeInput(stack);
		var recipe = level.recipeAccess().recipeMap().byType(RecipeType.SMELTING).stream()
				.map(RecipeHolder::value)
				.filter(r -> r.matches(input, level))
				.findFirst();

		if (recipe.isPresent()) {
			var ret = recipe.get().assemble(input).copy();

			ret.setCount(ret.getCount() * stack.getCount());
			if (stack.is(Tags.Items.ORES)) {
				ApplyBonusCount.addUniformBonusCount(ECEnchantmentHelper.getEnchantmentHolder(registry, Enchantments.FORTUNE)).build().apply(ret, context);
			}
			return ret;
		}
		return stack;
	}

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		var tool = context.getOptionalParameter(LootContextParams.TOOL);

		if (tool != null && tool.count() > 0 && ToolInfusionHelper.hasAutoSmelt(tool)) {
			return generatedLoot.stream()
					.map(s -> applyAutoSmelt(s, context))
					.collect(Collectors.toCollection(ObjectArrayList::new));
		}
		return generatedLoot;
	}

	@Override
	public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
		return DIRECT_CODEC;
	}
}
