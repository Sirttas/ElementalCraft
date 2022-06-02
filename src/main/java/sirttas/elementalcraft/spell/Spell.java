package sirttas.elementalcraft.spell;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistryEntry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.StreamSupport;

public class Spell extends ForgeRegistryEntry<Spell> implements IElementTypeProvider {

	private String translationKey;
	protected final Holder<SpellProperties> properties;

	protected Spell(ResourceKey<Spell> key) {
		properties = ElementalCraft.SPELL_PROPERTIES_MANAGER.getOrCreateHolder(SpellProperties.getKey(key));
	}

	public String getTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.makeDescriptionId("spell", Spells.REGISTRY.get().getKey(this));
		}

		return this.translationKey;
	}

	public Component getDisplayName() {
		return new TranslatableComponent(getTranslationKey());
	}

	public Multimap<Attribute, AttributeModifier> getOnUseAttributeModifiers() {
		return getProperties().getAttributes();
	}

	public InteractionResult castOnEntity(Entity sender, Entity target) {
		return InteractionResult.PASS;
	}

	public InteractionResult castOnBlock(Entity sender, BlockPos target) {
		return InteractionResult.PASS;
	}

	public InteractionResult castOnSelf(Entity sender) {
		return InteractionResult.PASS;
	}

	public void addSpellInstance(AbstractSpellInstance instance) {
		SpellTickManager.getInstance(instance.sender.level).addSpellInstance(instance);
	}

	public boolean consume(Entity sender, boolean simulate) {
		if (!(sender instanceof Player) || !((Player) sender).isCreative()) {
			int consumeAmount = Math.round(getConsumeAmount() * ToolInfusionHelper.getElementCostReduction(sender));
			
			return CapabilityElementStorage.get(sender).map(holder -> holder.extractElement(consumeAmount, this.getElementType(), simulate) >= consumeAmount).orElse(false);
		}
		return true;
	}

	protected boolean consume(Entity sender, ItemLike item, int count, boolean simulate) {
		if (sender instanceof Player player && !player.isCreative()) {
			Inventory inv = player.getInventory();
			int slot = ECContainerHelper.getSlotFor(inv, new ItemStack(item));

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

	public int getCooldown() {
		return getProperties().cooldown();
	}

	public int getConsumeAmount() {
		return getProperties().consumeAmount();
	}

	@Override
	public ElementType getElementType() {
		return getProperties().getElementType();
	}

	public Type getSpellType() {
		return getProperties().spellType();
	}

	public int getUseDuration() {
		return getProperties().useDuration();
	}

	public int getWeight() {
		return getProperties().weight();
	}
	
	public float getRange(Entity sender) {
		int bonus = 0;
		
		if (StreamSupport.stream(sender.getHandSlots().spliterator(), false).anyMatch(s -> !s.isEmpty() && s.getItem() == ECItems.STAFF)) {
			bonus++;
		}
		return getProperties().range() + bonus;
	}
	
	public int getColor() {
		return getProperties().color();
	}

	private SpellProperties getProperties() {
		if (properties.isBound()) {
			return properties.value();
		}
		return SpellProperties.NONE;
	}

	public boolean isChannelable() {
		return getUseDuration() > 0;
	}

	public boolean isValid() {
		return getSpellType() != Type.NONE && getElementType() != ElementType.NONE;
	}

	public void addInformation(List<Component> tooltip) {
		// provided for override
	}

	@Override
	public String toString() {
		return this.getRegistryName().toString();
	}

	public boolean isVisible() {
		return isValid() && !getProperties().hidden();
	}

	public enum Type implements StringRepresentable {
		NONE("none"), COMBAT("combat"), UTILITY("utility"), MIXED("mixed");
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values, Type::byName);

		private final String name;

		Type(String name) {
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
