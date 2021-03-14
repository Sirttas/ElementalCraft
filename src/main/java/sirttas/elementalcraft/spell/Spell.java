package sirttas.elementalcraft.spell;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class Spell extends ForgeRegistryEntry<Spell> implements IElementTypeProvider {

	protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("9da31711-7ea8-41d6-a4ef-3a6f7f679637");
	protected static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("1a6a2857-a598-40e4-9161-5b58bb14e9bc");

	public static final IForgeRegistry<Spell> REGISTRY = RegistryManager.ACTIVE.getRegistry(Spell.class);

	private String translationKey;
	protected SpellProperties properties = SpellProperties.NONE;

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

	public ActionResultType castOnEntity(Entity sender, Entity target) {
		return ActionResultType.PASS;
	}

	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		return ActionResultType.PASS;
	}

	public ActionResultType castOnSelf(Entity sender) {
		return ActionResultType.PASS;
	}

	public void addSpellInstance(AbstractSpellInstance instance) {
		SpellTickManager.getInstance(instance.sender.world).addSpellInstance(instance);
	}

	public boolean consume(Entity sender) {
		if (!(sender instanceof PlayerEntity) || !((PlayerEntity) sender).isCreative()) {
			int consumeAmount = getConsumeAmount();
			
			return CapabilityElementStorage.get(sender).map(holder -> holder.extractElement(consumeAmount, this.getElementType(), false) >= consumeAmount).orElse(false);
		}
		return true;
	}

	protected boolean consume(Entity sender, IItemProvider item, int count) {
		if (sender instanceof PlayerEntity && !((PlayerEntity) sender).isCreative()) {
			PlayerInventory inv = ((PlayerEntity) sender).inventory;
			int slot = ECInventoryHelper.getSlotFor(inv, new ItemStack(item));

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

	public void setProperties(SpellProperties properties) {
		this.properties = properties;

	}

	public int getCooldown() {
		return properties.getCooldown();
	}

	public int getConsumeAmount() {
		return properties.getConsumeAmount();
	}

	@Override
	public ElementType getElementType() {
		return properties.getElementType();
	}

	public Type getSpellType() {
		return properties.getSpellType();
	}

	public int getUseDuration() {
		return properties.getUseDuration();
	}

	public int getWeight() {
		return properties.getWeight();
	}

	public float getRange() {
		return properties.getRange();
	}

	public int getColor() {
		return properties.getColor();
	}

	public boolean isChannelable() {
		return getUseDuration() > 0;
	}

	public boolean isValid() {
		return getSpellType() != Type.NONE && getElementType() != ElementType.NONE;
	}

	public void addInformation(List<ITextComponent> tooltip) {
		// provided for override
	}

	@Override
	public String toString() {
		return this.getRegistryName().getPath();
	}

	public enum Type implements IStringSerializable {
		NONE("none"), COMBAT("combat"), UTILITY("utility"), MIXED("mixed");
		
		public static final Codec<Type> CODEC = IStringSerializable.createEnumCodec(Type::values, Type::byName);

		private final String name;

		private Type(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getString() {
			return this.name;
		}

		public static Type byName(String name) {
			for (Type type : values()) {
				if (type.name.equals(name)) {
					return type;
				}
			}
			return NONE;
		}
	}
}
