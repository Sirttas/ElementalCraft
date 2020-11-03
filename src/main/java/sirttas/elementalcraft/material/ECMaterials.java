package sirttas.elementalcraft.material;

import java.util.function.Supplier;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.item.ECItems;

public class ECMaterials {

	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };

	public static final Material SOURCE = new Material(MaterialColor.AIR, false, false, false, false, false, true, PushReaction.DESTROY);

	public static final IItemTier TOOL_WATER = new ECItemTier(2, 250, 6.0F, 2.0F, 17, () -> Ingredient.fromItems(ECItems.waterCrystal));
	public static final IItemTier TOOL_FIRE = new ECItemTier(2, 250, 6.0F, 3.0F, 14, () -> Ingredient.fromItems(ECItems.fireCrystal));
	public static final IItemTier TOOL_EARTH = new ECItemTier(2, 500, 6.0F, 2.0F, 14, () -> Ingredient.fromItems(ECItems.earthCrystal));
	public static final IItemTier TOOL_AIR = new ECItemTier(2, 250, 7.0F, 2.0F, 14, () -> Ingredient.fromItems(ECItems.airCrystal));
	public static final IItemTier TOOL_PURE = new ECItemTier(5, 2500, 10.0F, 5.0F, 19, () -> Ingredient.fromItems(ECItems.pureCrystal));

	public static final IArmorMaterial ARMOR_WATER = new ECArmorMaterial("iron", 15, new int[] { 2, 5, 6, 2 }, 11, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F,
			() -> Ingredient.fromItems(ECItems.waterCrystal));
	public static final IArmorMaterial ARMOR_FIRE = new ECArmorMaterial("iron", 17, new int[] { 3, 5, 7, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F,
			() -> Ingredient.fromItems(ECItems.fireCrystal));
	public static final IArmorMaterial ARMOR_EARTH = new ECArmorMaterial("iron", 20, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, 0.0F,
			() -> Ingredient.fromItems(ECItems.earthCrystal));
	public static final IArmorMaterial ARMOR_AIR = new ECArmorMaterial("iron", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F, 0.0F,
			() -> Ingredient.fromItems(ECItems.airCrystal));
	public static final IArmorMaterial ARMOR_PURE = new ECArmorMaterial("diamond", 44, new int[] { 3, 6, 8, 3 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F, 0.1F,
			() -> Ingredient.fromItems(ECItems.pureCrystal));

	private static class ECItemTier implements IItemTier {

		private final int harvestLevel;
		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int enchantability;
		private final LazyValue<Ingredient> repairMaterial;

		private ECItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
		      this.harvestLevel = harvestLevelIn;
		      this.maxUses = maxUsesIn;
		      this.efficiency = efficiencyIn;
		      this.attackDamage = attackDamageIn;
		      this.enchantability = enchantabilityIn;
		      this.repairMaterial = new LazyValue<>(repairMaterialIn);
		   }

		@Override
		public int getMaxUses() {
			return this.maxUses;
		}

		@Override
		public float getEfficiency() {
			return this.efficiency;
		}

		@Override
		public float getAttackDamage() {
			return this.attackDamage;
		}

		@Override
		public int getHarvestLevel() {
			return this.harvestLevel;
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return this.repairMaterial.getValue();
		}
	}

	private static class ECArmorMaterial implements IArmorMaterial {

		private final String name;
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final int enchantability;
		private final SoundEvent soundEvent;
		private final float toughness;
		private final LazyValue<Ingredient> repairMaterial;
		private final float knockbackResistance;

		private ECArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn, SoundEvent equipSoundIn, float toughnessIn, float knockbackResistance,
				Supplier<Ingredient> repairMaterialSupplier) {
			this.name = nameIn;
			this.maxDamageFactor = maxDamageFactorIn;
			this.damageReductionAmountArray = damageReductionAmountsIn;
			this.enchantability = enchantabilityIn;
			this.soundEvent = equipSoundIn;
			this.toughness = toughnessIn;
			this.knockbackResistance = knockbackResistance;
			this.repairMaterial = new LazyValue<>(repairMaterialSupplier);
		}

		@Override
		public int getDurability(EquipmentSlotType slotIn) {
			return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}

		@Override
		public int getDamageReductionAmount(EquipmentSlotType slotIn) {
			return this.damageReductionAmountArray[slotIn.getIndex()];
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public SoundEvent getSoundEvent() {
			return this.soundEvent;
		}

		@Override
		public Ingredient getRepairMaterial() {
			return this.repairMaterial.getValue();
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public String getName() {
			return this.name;
		}

		@Override
		public float getToughness() {
			return this.toughness;
		}

		@Override
		public float getKnockbackResistance() {
			return knockbackResistance;
		}
	}
}
