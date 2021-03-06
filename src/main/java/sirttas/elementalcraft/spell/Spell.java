package sirttas.elementalcraft.spell;

import java.util.List;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.properties.SpellProperties;

public class Spell extends ForgeRegistryEntry<Spell> implements IElementTypeProvider {

	public static final IForgeRegistry<Spell> REGISTRY = RegistryManager.ACTIVE.getRegistry(Spell.class);

	private String translationKey;
	protected SpellProperties properties = SpellProperties.NONE;

	public String getTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.makeDescriptionId("spell", REGISTRY.getKey(this));
		}

		return this.translationKey;
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(getTranslationKey());
	}

	public Multimap<Attribute, AttributeModifier> getOnUseAttributeModifiers() {
		return properties.getAttributes();
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
		SpellTickManager.getInstance(instance.sender.level).addSpellInstance(instance);
	}

	public boolean consume(Entity sender, boolean simulate) {
		if (!(sender instanceof PlayerEntity) || !((PlayerEntity) sender).isCreative()) {
			int consumeAmount = Math.round(getConsumeAmount() * ToolInfusionHelper.getElementCostReduction(sender));
			
			return CapabilityElementStorage.get(sender).map(holder -> holder.extractElement(consumeAmount, this.getElementType(), simulate) >= consumeAmount).orElse(false);
		}
		return true;
	}

	protected boolean consume(Entity sender, IItemProvider item, int count, boolean simulate) {
		if (sender instanceof PlayerEntity && !((PlayerEntity) sender).isCreative()) {
			PlayerInventory inv = ((PlayerEntity) sender).inventory;
			int slot = ECInventoryHelper.getSlotFor(inv, new ItemStack(item));

			if (slot >= 0) {
				ItemStack stack = inv.getItem(slot);
				int size = Math.min(count, stack.getCount());

				if (!simulate) {
					stack.shrink(size);
					if (stack.isEmpty()) {
						inv.setItem(slot, ItemStack.EMPTY);
					}
				}
				if (size < count) {
					return consume(sender, item, count - size, simulate);
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
	
	public float getRange(Entity sender) {
		int bonus = 0;
		
		if (StreamSupport.stream(sender.getHandSlots().spliterator(), false).anyMatch(s -> !s.isEmpty() && s.getItem() == ECItems.STAFF)) {
			bonus++;
		}
		return properties.getRange() + bonus;
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
		
		public static final Codec<Type> CODEC = IStringSerializable.fromEnum(Type::values, Type::byName);

		private final String name;

		private Type(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
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
