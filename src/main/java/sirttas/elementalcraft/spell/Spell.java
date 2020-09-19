package sirttas.elementalcraft.spell;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.item.holder.ItemElementHolder;

public class Spell extends net.minecraftforge.registries.ForgeRegistryEntry<Spell> {

	protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("9da31711-7ea8-41d6-a4ef-3a6f7f679637");
	protected static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("1a6a2857-a598-40e4-9161-5b58bb14e9bc");

	public static final IForgeRegistry<Spell> REGISTRY = RegistryManager.ACTIVE.getRegistry(Spell.class);

	private String translationKey;
	protected final int cooldown;
	protected final int consumeAmount;
	protected final ElementType elementType;
	protected final Type type;

	public Spell(Properties properties) {
		this.type = properties.type;
		this.elementType = properties.elementType;
		this.consumeAmount = properties.consumeAmount;
		this.cooldown = properties.cooldown;
	}

	public String getTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.makeTranslationKey("spell", REGISTRY.getKey(this));
		}

		return this.translationKey;
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(getTranslationKey());
	}

	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return HashMultimap.create();
	}

	public Multimap<Attribute, AttributeModifier> getOnUseAttributeModifiers() {
		return HashMultimap.create();
	}

	public boolean consume(Entity sender) {
		if (sender instanceof PlayerEntity && !((PlayerEntity) sender).isCreative()) {
			ItemStack stack = ItemElementHolder.find((PlayerEntity) sender, this.elementType);
			
			if (!stack.isEmpty()) {
				ItemElementHolder holder = (ItemElementHolder) stack.getItem();

				if (holder.getElementAmount(stack) >= consumeAmount) {
					holder.extractElement(stack, consumeAmount);
					return true;
				}
			}
			return false;
		}
		return true;
	}

	protected boolean consume(Entity sender, IItemProvider item, int count) {
		if (sender instanceof PlayerEntity && !((PlayerEntity) sender).isCreative()) {
			PlayerInventory inv = ((PlayerEntity) sender).inventory;
			int slot = inv.getSlotFor(new ItemStack(item));

			if (slot >= 0) {
				ItemStack ret = inv.decrStackSize(slot, count);

				if (!ret.isEmpty()) {
					return consume(sender, item, count - ret.getCount());
				}
				return true;
			}
			return false;
		}
		return true;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getConsumeAmount() {
		return consumeAmount;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public Type getSpellType() {
		return type;
	}

	public void addInformation(List<ITextComponent> tooltip) {
		// provided for override
	}

	public enum Type {
		NONE, COMBAT, UTILITY, MIXED
	}

	public static final class Properties {
		private int cooldown;
		private int consumeAmount;
		private ElementType elementType;
		private final Type type;

		private Properties(Type type) {
			this.type = type;
			this.elementType = ElementType.NONE;
			cooldown = 0;
			consumeAmount = 0;
		}

		public static Properties create(Type type) {
			return new Properties(type);
		}

		public Properties cooldown(int cooldown) {
			this.cooldown = cooldown;
			return this;
		}

		public Properties consumeAmount(int consumeAmount) {
			this.consumeAmount = consumeAmount;
			return this;
		}

		public Properties elementType(ElementType elementType) {
			this.elementType = elementType;
			return this;
		}
	}
}
