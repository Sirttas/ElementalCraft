package sirttas.elementalcraft.infusion;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.ECNames;
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
				(s, m) -> applyAttributeModifierInfusion(s, EquipmentSlotType.MAINHAND, SharedMonsterAttributes.ATTACK_SPEED, ECConfig.CONFIG.swordAirInfusionSpeedBonus.get() * m));
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
				(s, m) -> applyAttributeModifierInfusion(s, EquipmentSlotType.LEGS, SharedMonsterAttributes.MOVEMENT_SPEED, ECConfig.CONFIG.leggingsAirInfusionSpeedBonus.get() * m));
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
		int level = (froLvl != null ? froLvl : 0) + levelToAdd;

		if (level > 0) {
			map.put(enchantment, level);
		} else {
			map.remove(enchantment);
		}
		EnchantmentHelper.setEnchantments(map, stack);
	}

	public static List<ToolInfusionRecipe> getRecipes() {
		return Stream.of(ElementType.values()).filter(e -> e != ElementType.NONE).flatMap(e -> Stream.of(ItemClass.values()).flatMap(c -> c.tag.getAllElements().stream()
				.map(i -> new DisplayToolInfusionRecipe(i, e)))) .collect(Collectors.toList());
	}

	private static void addAttributeModifier(ItemStack stack, IAttribute attribute, double amount, EquipmentSlotType slot) {
		if (SharedMonsterAttributes.ATTACK_SPEED.equals(attribute)) {
			stack.addAttributeModifier(attribute.getName(), new AttributeModifier(Item.ATTACK_SPEED_MODIFIER, "Weapon modifier", amount, AttributeModifier.Operation.ADDITION), slot);
		} else if (SharedMonsterAttributes.ATTACK_DAMAGE.equals(attribute)) {
			stack.addAttributeModifier(attribute.getName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", amount, AttributeModifier.Operation.ADDITION), slot);
		} else {
			stack.addAttributeModifier(attribute.getName(), new AttributeModifier(INFUSION_MODIFIER, "Infusion modifier", amount, AttributeModifier.Operation.ADDITION), slot);
		}
	}

	private static void applyAttributeModifierInfusion(ItemStack stack, EquipmentSlotType slot, IAttribute attribute, double amount) {
		stack.getAttributeModifiers(slot).forEach((k, v) -> {
			if (k.equals(attribute.getName())) {
				addAttributeModifier(stack, attribute, v.getAmount() + amount, slot);
			} else {
				stack.addAttributeModifier(k, v, slot);
			}
		});
		if (!stack.getAttributeModifiers(slot).containsKey(attribute.getName())) {
			addAttributeModifier(stack, attribute, amount, slot);
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
			nbt.putString(ECNames.INFUSION_TYPE, type.getName());
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

	public static boolean canAirInfusionFly(PlayerEntity player) {
		return hasInfusion(player.getItemStackFromSlot(EquipmentSlotType.CHEST), ElementType.AIR) && !player.onGround && !player.abilities.isFlying && !player.isPassenger();
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
		
		Tag<Item> tag;

		ItemClass(Tag<Item> tag) {
			this.tag = tag;
		}
	}

	private static class DisplayToolInfusionRecipe extends ToolInfusionRecipe {

		Item item;

		private DisplayToolInfusionRecipe(Item item, ElementType elementType) {
			this.item = item;
			this.with(elementType);
			this.id = new ResourceLocation(ElementalCraft.MODID, item.getRegistryName().getNamespace() + '_' + item.getRegistryName().getPath() + "_tool_infusion_with_" + elementType.getName());
		}

		@Override
		public NonNullList<Ingredient> getIngredients() {
			return NonNullList.from(Ingredient.EMPTY, Ingredient.fromItems(item));
		}

		@Override
		public ItemStack getRecipeOutput() {
			ItemStack stack = new ItemStack(item);

			setInfusion(stack, elementType);
			return stack;
		}
	}
}
