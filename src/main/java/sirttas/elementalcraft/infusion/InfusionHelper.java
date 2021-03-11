package sirttas.elementalcraft.infusion;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.NonNullList;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.tag.ECTags;

public class InfusionHelper {

	private static final UUID INFUSION_MODIFIER = UUID.fromString("908183f3-2486-41db-b92b-e2b16156bc07");

	private static Map<ElementType, Map<ItemClass, ObjIntConsumer<ItemStack>>> infusionMap;
	static {
		infusionMap = new EnumMap<>(ElementType.class);
		infusionMap.put(ElementType.FIRE, new EnumMap<>(ItemClass.class));
		infusionMap.put(ElementType.WATER, new EnumMap<>(ItemClass.class));
		infusionMap.put(ElementType.EARTH, new EnumMap<>(ItemClass.class));
		infusionMap.put(ElementType.AIR, new EnumMap<>(ItemClass.class));

		addInfusionToMap(ItemClass.SWORD, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.LOOTING));
		addInfusionToMap(ItemClass.SWORD, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FIRE_ASPECT));
		addInfusionToMap(ItemClass.SWORD, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.SHARPNESS));
		addInfusionToMap(ItemClass.SWORD, ElementType.AIR,
				(s, m) -> applyAttributeModifierInfusion(s, EquipmentSlotType.MAINHAND, Attributes.ATTACK_SPEED, ECConfig.COMMON.swordAirInfusionSpeedBonus.get(), m > 0));
		addInfusionToMap(ItemClass.PICKAXE, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FORTUNE));
		addInfusionToMap(ItemClass.PICKAXE, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.UNBREAKING));
		addInfusionToMap(ItemClass.PICKAXE, ElementType.AIR, (s, m) -> addEnchantmentLevel(s, m, Enchantments.EFFICIENCY));
		addInfusionToMap(ItemClass.AXE, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.LOOTING));
		addInfusionToMap(ItemClass.AXE, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FIRE_ASPECT));
		addInfusionToMap(ItemClass.AXE, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.SHARPNESS /* TODO chopping ? */));
		addInfusionToMap(ItemClass.AXE, ElementType.AIR, (s, m) -> addEnchantmentLevel(s, m, Enchantments.EFFICIENCY));
		addInfusionToMap(ItemClass.BOW, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.PUNCH));
		addInfusionToMap(ItemClass.BOW, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FLAME));
		addInfusionToMap(ItemClass.BOW, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.POWER));
		addInfusionToMap(ItemClass.CROSSBOW, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.MULTISHOT));
		addInfusionToMap(ItemClass.CROSSBOW, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.PIERCING));
		addInfusionToMap(ItemClass.CROSSBOW, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.UNBREAKING));
		addInfusionToMap(ItemClass.CROSSBOW, ElementType.AIR, (s, m) -> addEnchantmentLevel(s, m, Enchantments.QUICK_CHARGE));

		addInfusionToMap(ItemClass.HELMET, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.RESPIRATION));
		addInfusionToMap(ItemClass.HELMET, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FIRE_PROTECTION));
		addInfusionToMap(ItemClass.HELMET, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.PROTECTION));
		addInfusionToMap(ItemClass.HELMET, ElementType.AIR, (s, m) -> addEnchantmentLevel(s, m, Enchantments.PROJECTILE_PROTECTION));
		addInfusionToMap(ItemClass.CHESTPLATE, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.BLAST_PROTECTION));
		addInfusionToMap(ItemClass.CHESTPLATE, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FIRE_PROTECTION));
		addInfusionToMap(ItemClass.CHESTPLATE, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.PROTECTION));
		addInfusionToMap(ItemClass.LEGGINGS, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.BLAST_PROTECTION));
		addInfusionToMap(ItemClass.LEGGINGS, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FIRE_PROTECTION));
		addInfusionToMap(ItemClass.LEGGINGS, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.PROTECTION));
		addInfusionToMap(ItemClass.LEGGINGS, ElementType.AIR,
				(s, m) -> applyAttributeModifierInfusion(s, EquipmentSlotType.LEGS, Attributes.MOVEMENT_SPEED, ECConfig.COMMON.leggingsAirInfusionSpeedBonus.get(), m > 0));
		addInfusionToMap(ItemClass.BOOTS, ElementType.WATER, (s, m) -> addEnchantmentLevel(s, m, Enchantments.DEPTH_STRIDER));
		addInfusionToMap(ItemClass.BOOTS, ElementType.FIRE, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FIRE_PROTECTION));
		addInfusionToMap(ItemClass.BOOTS, ElementType.EARTH, (s, m) -> addEnchantmentLevel(s, m, Enchantments.PROTECTION));
		addInfusionToMap(ItemClass.BOOTS, ElementType.AIR, (s, m) -> addEnchantmentLevel(s, m, Enchantments.FEATHER_FALLING));

	}

	private static void addInfusionToMap(ItemClass itemClass, ElementType type, ObjIntConsumer<ItemStack> consumer) {
		infusionMap.get(type).put(itemClass, consumer);
	}

	public static void addEnchantmentLevel(ItemStack stack, int levelToAdd, Enchantment enchantment) {
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		Integer froLvl = map.get(enchantment);
		int level = Math.min((froLvl != null ? froLvl : 0) + levelToAdd, enchantment.getMaxLevel() + 1);

		if (level > 0) {
			map.put(enchantment, level);
		} else {
			map.remove(enchantment);
		}
		EnchantmentHelper.setEnchantments(map, stack);
	}

	public static List<ToolInfusionRecipe> getRecipes() {
		return Stream.of(ElementType.values()).filter(e -> e != ElementType.NONE)
				.flatMap(e -> Stream.of(ItemClass.values()).flatMap(c -> c.tag.getAllElements().stream().map(i -> new DisplayToolInfusionRecipe(i, e)))).collect(Collectors.toList());
	}

	private static void applyAttributeModifierInfusion(ItemStack stack, EquipmentSlotType slot, Attribute attribute, double amount, boolean isApplying) {
		if (!stack.hasTag() || !stack.getTag().contains("AttributeModifiers", 9)) {
			if (isApplying) {
				stack.getAttributeModifiers(slot).forEach((k, v) -> stack.addAttributeModifier(k, v, slot));
			}
		} else {
			Multimap<Attribute, AttributeModifier> map = stack.getAttributeModifiers(slot).entries().stream().filter(entry -> !entry.getValue().getID().equals(INFUSION_MODIFIER)).collect(Multimaps.toMultimap(Entry::getKey, Entry::getValue, HashMultimap::create));
			
			stack.getTag().getList("AttributeModifiers", 10).clear();
			map.forEach((key, value) -> stack.addAttributeModifier(key, value, slot));

		}
		if (isApplying) {
			stack.addAttributeModifier(attribute, new AttributeModifier(INFUSION_MODIFIER, "Infusion modifier", amount, AttributeModifier.Operation.ADDITION), slot);
		}
	}

	private static void applyInfusion(ItemStack stack, int mult) {
		ElementType type = getInfusion(stack);
		Optional<ItemClass> opt = getItemClass(stack);

		if (type != ElementType.NONE && opt.isPresent()) {
			ObjIntConsumer<ItemStack> consumer = infusionMap.get(type).get(opt.get());

			if (consumer != null) {
				consumer.accept(stack, mult);
			}
		}
	}

	public static void unapplyInfusion(ItemStack stack) {
		CompoundNBT nbt = getInfusionTag(stack);

		if (nbt != null && isApplied(stack)) {
			applyInfusion(stack, -1);
			nbt.putBoolean(ECNames.INFUSION_APPLIED, false);
		}
	}

	public static void applyInfusion(ItemStack stack) {
		CompoundNBT nbt = getInfusionTag(stack);

		if (nbt != null && hasInfusion(stack) && !isApplied(stack)) {
			applyInfusion(stack, 1);
			nbt.putBoolean(ECNames.INFUSION_APPLIED, true);
		}
	}

	public static ElementType getInfusion(ItemStack stack) {
		CompoundNBT nbt = getInfusionTag(stack);

		if (!stack.isEmpty() && nbt != null && nbt.contains(ECNames.INFUSION_TYPE)) {
			return ElementType.byName(nbt.getString(ECNames.INFUSION_TYPE));
		}
		return ElementType.NONE;
	}

	public static boolean hasInfusion(ItemStack stack) {
		return !hasInfusion(stack, ElementType.NONE);
	}

	public static boolean hasInfusion(ItemStack stack, ElementType type) {
		return getInfusion(stack) == type;
	}

	public static boolean hasInfusion(LivingEntity entity, EquipmentSlotType slot, ElementType type) {
		return hasInfusion(entity.getItemStackFromSlot(slot), type);
	}

	public static void setInfusion(ItemStack stack, ElementType type) {
		CompoundNBT nbt = getOrCreateInfusionTag(stack);

		if (!stack.isEmpty()) {
			unapplyInfusion(stack);
			nbt.putString(ECNames.INFUSION_TYPE, type.getString());
			applyInfusion(stack);
		}
	}

	public static void removeInfusion(ItemStack stack) {
		CompoundNBT nbt = getInfusionTag(stack);

		if (!stack.isEmpty() && nbt != null) {
			unapplyInfusion(stack);
			NBTHelper.getECTag(stack).remove(ECNames.INFUSION);
		}
	}

	public static boolean hasFireInfusionAutoSmelt(ItemStack stack) {
		return hasInfusion(stack, ElementType.FIRE) && hasClass(stack, ItemClass.PICKAXE);
	}

	public static boolean hasAirInfusionFasterDraw(ItemStack stack) {
		return hasInfusion(stack, ElementType.AIR) && hasClass(stack, ItemClass.BOW);
	}

	private static boolean hasClass(ItemStack stack, ItemClass itemClass) {
		Optional<ItemClass> opt = getItemClass(stack);
		
		return opt.isPresent() && opt.get() == itemClass;
	}

	public static boolean isInfusable(ItemStack stack) {
		return getItemClass(stack).isPresent();
	}

	private static Optional<ItemClass> getItemClass(ItemStack stack) {
		return Stream.of(ItemClass.values()).filter(c -> c.tag.contains(stack.getItem())).findFirst();
	}

	private static CompoundNBT getInfusionTag(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		if (nbt == null || !nbt.contains(ECNames.INFUSION)) {
			return null;
		}
		return nbt.getCompound(ECNames.INFUSION);
	}

	private static CompoundNBT getOrCreateInfusionTag(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getOrCreateECTag(stack);

		if (!nbt.contains(ECNames.INFUSION)) {
			nbt.put(ECNames.INFUSION, new CompoundNBT());
		}
		return nbt.getCompound(ECNames.INFUSION);
	}

	public static boolean isApplied(ItemStack stack) {
		CompoundNBT nbt = getInfusionTag(stack);

		return nbt != null && hasInfusion(stack) && nbt.getBoolean(ECNames.INFUSION_APPLIED);
	}

	public enum ItemClass {
		SWORD(ECTags.Items.INFUSABLE_SWORDS), PICKAXE(ECTags.Items.INFUSABLE_PICKAXES), AXE(ECTags.Items.INFUSABLE_AXES), SHOVEL(ECTags.Items.INFUSABLE_SHOVELS), HOE(ECTags.Items.INFUSABLE_HOES),
		SHIELD(ECTags.Items.INFUSABLE_SHILDS), BOW(ECTags.Items.INFUSABLE_BOWS), CROSSBOW(ECTags.Items.INFUSABLE_CROSSBOWS), HELMET(ECTags.Items.INFUSABLE_HELMETS),
		CHESTPLATE(ECTags.Items.INFUSABLE_CHESTPLATES), LEGGINGS(ECTags.Items.INFUSABLE_LEGGINGS), BOOTS(ECTags.Items.INFUSABLE_BOOTS);
		
		INamedTag<Item> tag;

		ItemClass(INamedTag<Item> tags) {
			this.tag = tags;
		}
	}

	private static class DisplayToolInfusionRecipe extends ToolInfusionRecipe {

		Item item;

		private DisplayToolInfusionRecipe(Item item, ElementType elementType) {
			this.item = item;
			this.with(elementType);
			this.id = ElementalCraft.createRL(item.getRegistryName().getNamespace() + '_' + item.getRegistryName().getPath() + "_tool_infusion_with_" + elementType.getString());
		}

		@Override
		public NonNullList<Ingredient> getIngredients() {
			return NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(item));
		}

		@Override
		@Deprecated
		public ItemStack getRecipeOutput() {
			ItemStack stack = new ItemStack(item);

			setInfusion(stack, elementType);
			return stack;
		}
	}
}
