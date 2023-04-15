package sirttas.elementalcraft.spell;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class Spell implements IElementTypeProvider {

	private String translationKey;
	protected final Holder<SpellProperties> properties;
	private final ResourceKey<Spell> key;

	protected Spell(ResourceKey<Spell> key) {
		properties = ElementalCraft.SPELL_PROPERTIES_MANAGER.getOrCreateHolder(SpellProperties.getKey(key));
		this.key = key;
	}

	public String getTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.makeDescriptionId("spell", getKey());
		}

		return this.translationKey;
	}

	public ResourceLocation getKey() {
		return key.location();
	}

	public Component getDisplayName() {
		return Component.translatable(getTranslationKey());
	}

	public Multimap<Attribute, AttributeModifier> getOnUseAttributeModifiers() {
		return getProperties().getAttributes();
	}

	@Nonnull
	public InteractionResult castOnEntity(@Nonnull Entity caster, @Nonnull Entity target) {
		return InteractionResult.PASS;
	}

	@Nonnull
	public InteractionResult castOnBlock(@Nonnull Entity caster, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
		return InteractionResult.PASS;
	}

	@Nonnull
	public InteractionResult castOnSelf(@Nonnull Entity caster) {
		return InteractionResult.PASS;
	}

	public void addSpellInstance(AbstractSpellInstance instance) {
		SpellTickHelper.get(instance.getCaster()).ifPresent(m -> m.addSpellInstance(instance));
	}

	public void delay(Entity caster, int delay, Runnable cast) {
		addSpellInstance(AbstractSpellInstance.delay(caster, this, delay, cast));
	}

	public void effect(Entity caster, int duration, Consumer<AbstractSpellInstance> tick) {
		addSpellInstance(AbstractSpellInstance.effect(caster, this, duration, tick));
	}

	public boolean consume(Entity caster, boolean simulate) {
		if (!(caster instanceof Player) || !((Player) caster).isCreative()) {
			int consumeAmount = Math.max(1, Math.round(getConsumeAmount() * ToolInfusionHelper.getElementCostReduction(caster)));
			
			return ElementStorageHelper.get(caster).map(holder -> holder.extractElement(consumeAmount, this.getElementType(), simulate) >= consumeAmount).orElse(false);
		}
		return true;
	}

	protected boolean consume(Entity caster, ItemLike item, int count, boolean simulate) {
		if (caster instanceof Player player && !player.isCreative()) {
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
					return consume(caster, item, count - size, simulate);
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
	
	public float getRange(Entity caster) {
		int bonus = 0;
		
		if (StreamSupport.stream(caster.getHandSlots().spliterator(), false).anyMatch(s -> !s.isEmpty() && s.is(ECItems.STAFF.get()))) {
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
		return this.key.toString();
	}

	public boolean isVisible() {
		return isValid() && !getProperties().hidden();
	}

    public UseAnim getUseAnimation() {
		return UseAnim.BOW;
    }

    public enum Type implements StringRepresentable {
		NONE("none"), COMBAT("combat"), UTILITY("utility"), MIXED("mixed");
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

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
