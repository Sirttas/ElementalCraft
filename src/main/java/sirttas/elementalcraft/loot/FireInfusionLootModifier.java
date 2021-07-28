package sirttas.elementalcraft.loot;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FireInfusionLootModifier extends LootModifier {

	private static final LootItemFunction FORTUNE = ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE).build();
	
	protected FireInfusionLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
		evt.getRegistry().register(new Serializer().setRegistryName(ElementalCraftApi.MODID, "fireinfusion"));
	}

	private ItemStack applyAutoSmelt(ItemStack stack, LootContext context) {
		Optional<Recipe<Container>> recipe = context.getLevel().getRecipeManager().byType(RecipeType.SMELTING).values().stream().filter(r -> r.getIngredients().get(0).test(stack)).findFirst();

		if (recipe.isPresent()) {
			ItemStack ret = recipe.get().getResultItem().copy();

			ret.setCount(ret.getCount() * stack.getCount());
			if (Tags.Items.ORES.contains(stack.getItem())) {
				FORTUNE.apply(ret, context);
			}
			return ret;
		}
		return stack;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

		if (tool != null && !tool.isEmpty() && ToolInfusionHelper.hasAutoSmelt(tool)) {
			return generatedLoot.stream().map(s -> applyAutoSmelt(s, context)).collect(Collectors.toList());
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