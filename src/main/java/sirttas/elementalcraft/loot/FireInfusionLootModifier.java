package sirttas.elementalcraft.loot;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.infusion.InfusionHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FireInfusionLootModifier extends LootModifier {

	private static final ILootFunction FORTUNE = ApplyBonus.uniformBonusCount(Enchantments.FORTUNE).build();
	
	protected FireInfusionLootModifier(ILootCondition[] conditions) {
		super(conditions);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
		evt.getRegistry().register(new Serializer().setRegistryName(ElementalCraft.MODID, "fireinfusion"));
	}

	private ItemStack applyAutoSmelt(ItemStack stack, LootContext context) {
		Optional<IRecipe<IInventory>> recipe = context.getWorld().getRecipeManager().getRecipes(IRecipeType.SMELTING).values().stream().filter(r -> r.getIngredients().get(0).test(stack)).findFirst();

		if (recipe.isPresent()) {
			ItemStack ret = recipe.get().getRecipeOutput().copy();

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
		ItemStack tool = context.get(LootParameters.TOOL);

		if (tool != null && !tool.isEmpty() && InfusionHelper.hasFireInfusionAutoSmelt(tool)) {
			return generatedLoot.stream().map(s -> applyAutoSmelt(s, context)).collect(Collectors.toList());
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<FireInfusionLootModifier> {
		@Override
		public FireInfusionLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
			return new FireInfusionLootModifier(conditions);
		}

		@Override
		public JsonObject write(FireInfusionLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}