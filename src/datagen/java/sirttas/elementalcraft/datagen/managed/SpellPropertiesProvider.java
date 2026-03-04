package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class SpellPropertiesProvider extends AbstractManagedDataBuilderProvider<SpellProperties, SpellProperties.Builder> {

	private static final Identifier REACH_DISTANCE_ID = ElementalCraftApi.createRL("spell_reach_distance");
	private static final Identifier ATTACK_DAMAGE_ID = ElementalCraftApi.createRL("spell_attack_damage");
	private static final Identifier ATTACK_KNOCKBACK_ID = ElementalCraftApi.createRL("spell_attack_knockback");

	public SpellPropertiesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries, ElementalCraft.SPELL_PROPERTIES_MANAGER, SpellProperties.Builder.ENCODER);
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		builder(Spells.GRAVEL_FALL, Spell.Type.COMBAT, ElementType.EARTH)
				.color(175, 179, 179)
				.consumeAmount(250)
				.cooldown(40)
				.weight(20)
				.strength(1)
				.addAttribute(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(REACH_DISTANCE_ID, 5.0D, AttributeModifier.Operation.ADD_VALUE));
		builder(Spells.STONE_WALL, Spell.Type.COMBAT, ElementType.EARTH)
				.color(207, 212, 212)
				.consumeAmount(500)
				.cooldown(100)
				.weight(20);
		builder(Spells.FIRE_BALL, Spell.Type.COMBAT, ElementType.FIRE)
				.color(245, 174, 22)
				.consumeAmount(500)
				.cooldown(100)
				.weight(30)
				.strength(1);
		builder(Spells.ITEM_PULL, Spell.Type.UTILITY, ElementType.AIR)
				.color(250, 252, 222)
				.consumeAmount(1000)
				.cooldown(200)
				.weight(5)
				.range(10);
		builder(Spells.ENDER_STRIKE, Spell.Type.COMBAT, ElementType.AIR)
				.color(103, 15, 105)
				.consumeAmount(1000)
				.cooldown(60)
				.weight(15)
				.range(20)
				.addAttribute(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_ID, 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
				.addAttribute(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(REACH_DISTANCE_ID, 5.0D, AttributeModifier.Operation.ADD_VALUE));
		builder(Spells.ANIMAL_GROWTH, Spell.Type.UTILITY, ElementType.WATER)
				.color(0, 134, 161)
				.consumeAmount(2000)
				.cooldown(200)
				.weight(10);
		builder(Spells.TREE_FALL, Spell.Type.UTILITY, ElementType.EARTH)
				.color(0, 128, 34)
				.consumeAmount(3000)
				.cooldown(600)
				.weight(5)
				.range(15);
		builder(Spells.PURIFICATION, Spell.Type.MIXED, ElementType.WATER)
				.color(5, 207, 247)
				.consumeAmount(1000)
				.cooldown(200)
				.weight(10);
		builder(Spells.RIPENING, Spell.Type.UTILITY, ElementType.WATER)
				.color(0, 189, 126)
				.consumeAmount(200)
				.cooldown(20)
				.weight(20);
		builder(Spells.FLAME_CLEAVE, Spell.Type.COMBAT, ElementType.FIRE)
				.color(156, 45, 11)
				.consumeAmount(1000)
				.cooldown(60)
				.weight(15)
				.range(3)
				.strength(3)
				.addAttribute(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_ID, 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		builder(Spells.INFERNO, Spell.Type.COMBAT, ElementType.FIRE)
				.color(255, 128, 48)
				.consumeAmount(10)
				.useDuration(200)
				.cooldown(120)
				.weight(20)
				.range(5)
				.strength(2);
		builder(Spells.DASH, Spell.Type.MIXED, ElementType.AIR)
				.color(190, 206, 237)
				.consumeAmount(400)
				.cooldown(40)
				.weight(5)
				.range(6);
		builder(Spells.SILK_VEIN, Spell.Type.UTILITY, ElementType.EARTH)
				.color(4, 77, 60)
				.consumeAmount(5000)
				.cooldown(700)
				.weight(2)
				.range(15);
		builder(Spells.TRANSLOCATION, Spell.Type.UTILITY, ElementType.AIR)
				.color(218, 184, 255)
				.consumeAmount(500)
				.cooldown(300)
				.range(100);
		builder(Spells.FEATHER_SPIKES, Spell.Type.COMBAT, ElementType.AIR)
				.color(180, 150, 71)
				.consumeAmount(500)
				.cooldown(200)
				.weight(5)
				.strength(4);
		builder(Spells.HEAL, Spell.Type.MIXED, ElementType.WATER)
				.color(MobEffects.HEAL.value().getColor())
				.consumeAmount(1000)
				.cooldown(600)
				.weight(5);
		builder(Spells.SPEED, Spell.Type.MIXED, ElementType.AIR)
				.color(MobEffects.MOVEMENT_SPEED.value().getColor())
				.consumeAmount(4000)
				.cooldown(2400)
				.weight(2);
		builder(Spells.SHOCKWAVE, Spell.Type.COMBAT, ElementType.AIR)
				.color(189, 188, 182)
				.consumeAmount(250)
				.cooldown(120)
				.weight(10)
				.range(5)
				.addAttribute(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ATTACK_KNOCKBACK_ID, 1, AttributeModifier.Operation.ADD_VALUE));
		builder(Spells.AIR_SHIELD, Spell.Type.COMBAT, ElementType.AIR)
				.color(215, 227, 245)
				.consumeAmount(25)
				.useDuration(400)
				.cooldown(60)
				.weight(5);
		builder(Spells.REPAIR, Spell.Type.UTILITY, ElementType.FIRE)
				.color(51, 45, 30)
				.consumeAmount(100)
				.useDuration(401)
				.cooldown(2400)
				.strength(1);
		builder(Spells.LIGHT, Spell.Type.UTILITY, ElementType.FIRE)
				.color(242, 255, 64)
				.consumeAmount(100)
				.cooldown(10)
				.weight(20)
				.strength(14)
				.addAttribute(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(REACH_DISTANCE_ID, 15.0D, AttributeModifier.Operation.ADD_VALUE));
	}

	private SpellProperties.Builder builder(DeferredHolder<Spell, ? extends Spell> spell, Spell.Type type, ElementType elementType) {
		var builder = SpellProperties.Builder.create(type).elementType(elementType);

		this.add(spell.getId(), builder);
		return builder;
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
