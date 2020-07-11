package sirttas.elementalcraft.loot;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.infusion.InfusionHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FireInfusionLootModifier extends LootModifier {
	protected FireInfusionLootModifier(ILootCondition[] conditions) {
		super(conditions);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<GlobalLootModifierSerializer<?>> evt) {
		evt.getRegistry().register(new Serializer().setRegistryName(ElementalCraft.MODID, "fireinfusion"));
	}

	private ItemStack applyAutoSmelt(ItemStack stack, ServerWorld world) {
		Optional<IRecipe<IInventory>> recipe = world.getRecipeManager().getRecipes(IRecipeType.SMELTING).values().stream().filter(r -> r.getIngredients().get(0).test(stack)).findFirst();
		
		if (recipe.isPresent()) {
			ItemStack ret = recipe.get().getRecipeOutput().copy();

			ret.setCount(ret.getCount() * stack.getCount());
			return ret;
		}
		return stack;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		ItemStack tool = context.get(LootParameters.TOOL);

		if (tool != null && !tool.isEmpty() && InfusionHelper.hasFireInfusionAutoSmelt(tool)) {
			return generatedLoot.stream().map(s -> applyAutoSmelt(s, context.getWorld())).collect(Collectors.toList());
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<FireInfusionLootModifier> {
		@Override
		public FireInfusionLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
			return new FireInfusionLootModifier(conditions);
		}
	}
}