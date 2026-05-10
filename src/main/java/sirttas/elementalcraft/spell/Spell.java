package sirttas.elementalcraft.spell;

import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import java.util.function.Consumer;

public class Spell implements IElementTypeProvider {

	private String descriptionId = "";
	protected final Holder<SpellProperties> properties;

	protected Spell(Holder<SpellProperties> properties) {
		this.properties = properties;
	}

	public String getDescriptionId() {
		if (descriptionId.isBlank()) {
			descriptionId = properties.value().descriptionId();
		}
		if (descriptionId.isBlank()) {
			descriptionId = Util.makeDescriptionId("elementalcraft_spell", Spells.REGISTRY.getKey(this));
		}
		return descriptionId;
	}

	public Component getDisplayName() {
		return Component.translatable(getDescriptionId());
	}

	public ItemStackTemplate createItemStackTemplate() {
		var patchBuilder = DataComponentPatch.builder();

		patchBuilder.set(ECDataComponents.SPELL.get(), Spells.REGISTRY.wrapAsHolder(this));

		getDataComponents().forEach(patchBuilder::set);
		return new ItemStackTemplate(ECItems.SCROLL, patchBuilder.build());
	}

	protected DataComponentMap getDataComponents() {
		return DataComponentMap.EMPTY;
	}

    public InteractionHand getHand(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return InteractionHand.MAIN_HAND;
        }

        var mainHand = livingEntity.getMainHandItem();
        var offHand = livingEntity.getOffhandItem();

        if (this == SpellHelper.getSpell(mainHand).value()) {
            return InteractionHand.MAIN_HAND;
        } else if (this == SpellHelper.getSpell(offHand).value()) {
            return InteractionHand.OFF_HAND;
        }
        return InteractionHand.MAIN_HAND;
    }

    public ItemStack getItemInOtherHand(LivingEntity entity) {
        var hand = this.getHand(entity);

        return hand == InteractionHand.MAIN_HAND ? entity.getOffhandItem() : entity.getMainHandItem();
    }

	public Multimap<Holder<Attribute>, AttributeModifier> getOnUseAttributeModifiers() {
		return getProperties().getAttributes();
	}

	public SpellCastResult castOnEntity(Level level, Entity caster, Entity target) {
		return SpellCastResult.PASS;
	}

	public SpellCastResult castOnBlock(Level level, Entity caster, BlockPos target, BlockHitResult hitResult) {
		return SpellCastResult.PASS;
	}

	public SpellCastResult castOnSelf(Level level, Entity caster) {
		return SpellCastResult.PASS;
	}

	public void addSpellInstance(AbstractSpellInstance instance) {
		var manager = SpellTickHelper.get(instance.getCaster());

		if (manager != null) {
			manager.addSpellInstance(instance);
		}
	}

	public void delay(Entity caster, int delay, Runnable cast) {
		addSpellInstance(AbstractSpellInstance.delay(caster, this, delay, cast));
	}

	public void effect(Entity caster, int duration, Consumer<AbstractSpellInstance> tick) {
		addSpellInstance(AbstractSpellInstance.effect(caster, this, duration, tick));
	}

	public boolean consume(Entity caster, boolean simulate) {
		if (!(caster instanceof Player player) || !player.getAbilities().instabuild) {
			int consumeAmount = Math.max(1, Math.round(getConsumeAmount() * ToolInfusionHelper.getElementCostReduction(caster)));
			var storage = caster.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

			if (storage == null) {
				return false;
			}
			return storage.extractElement(consumeAmount, this.getElementType(), simulate) >= consumeAmount;
		}
		return true;
	}

	protected boolean consume(Entity caster, ItemLike item, int count, boolean simulate) {
		if (caster instanceof Player player && !player.getAbilities().instabuild) {
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

	public float getStrength() {
		return getProperties().strength();
	}
	
	public float getRange(@Nullable Entity caster) {
		int bonus = 0;
		
		if (caster instanceof LivingEntity livingEntity && EntityHelper.handStream(livingEntity).anyMatch(s -> !s.isEmpty() && s.is(ECItems.STAFF.get()))) {
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

	public void addInformation(Consumer<Component> builder) {
		// provided for override
	}

	@Override
	public String toString() {
		return Spells.REGISTRY.wrapAsHolder(this).getRegisteredName();
	}

	public boolean isVisible() {
		return isValid() && !getProperties().hidden();
	}

    public ItemUseAnimation getUseAnimation() {
		return ItemUseAnimation.BOW;
    }

    public enum Type implements StringRepresentable {
		NONE("none"),
        COMBAT("combat"),
        UTILITY("utility"),
        MIXED("mixed");
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

		private final String name;

		Type(String name) {
			this.name = name;
		}

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
