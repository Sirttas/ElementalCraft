package sirttas.elementalcraft.datagen.managed;

import java.io.IOException;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import sirttas.dpanvil.api.data.AbstractManagedDataProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.air.DashSpell;
import sirttas.elementalcraft.spell.air.EnderStrikeSpell;
import sirttas.elementalcraft.spell.air.ItemPullSpell;
import sirttas.elementalcraft.spell.air.TranslocationSpell;
import sirttas.elementalcraft.spell.earth.GavelFallSpell;
import sirttas.elementalcraft.spell.earth.SilkVeinSpell;
import sirttas.elementalcraft.spell.earth.StoneWallSpell;
import sirttas.elementalcraft.spell.earth.TreeFallSpell;
import sirttas.elementalcraft.spell.fire.FireBallSpell;
import sirttas.elementalcraft.spell.fire.FlameCleaveSpell;
import sirttas.elementalcraft.spell.fire.InfernoSpell;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.spell.water.AnimalGrowthSpell;
import sirttas.elementalcraft.spell.water.PurificationSpell;
import sirttas.elementalcraft.spell.water.RipeningSpell;

import javax.annotation.Nonnull;

public class SpellPropertiesProvider extends AbstractManagedDataProvider<SpellProperties> {

	public SpellPropertiesProvider(DataGenerator generator) {
		super(generator, ElementalCraft.SPELL_PROPERTIES_MANAGER);
	}

	@Override
	public void run(@Nonnull HashCache cache) throws IOException {
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.EARTH).color(175, 179, 179).consumeAmount(250).cooldown(40).weight(20)
				.addAttribute(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier("Reach distance modifier", 5.0D, AttributeModifier.Operation.ADDITION)), GavelFallSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.EARTH).color(207, 212, 212).consumeAmount(500).cooldown(100).weight(20), StoneWallSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).color(245, 174, 22).consumeAmount(500).cooldown(100).weight(30), FireBallSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.AIR).color(250, 252, 222).consumeAmount(1000).cooldown(200).weight(5).range(10), ItemPullSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.AIR).color(103, 15, 105).consumeAmount(1000).cooldown(60).weight(15).range(20)
				.addAttribute(Attributes.ATTACK_DAMAGE, new AttributeModifier("Weapon modifier", 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL))
				.addAttribute(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier("Reach distance modifier", 5.0D, AttributeModifier.Operation.ADDITION)), EnderStrikeSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.WATER).color(0, 134, 161).consumeAmount(2000).cooldown(200).weight(10), AnimalGrowthSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.EARTH).color(0, 128, 34).consumeAmount(3000).cooldown(600).weight(5).range(15), TreeFallSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.WATER).color(5, 207, 247).consumeAmount(1000).cooldown(200).weight(10), PurificationSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.WATER).color(0, 189, 126).consumeAmount(200).cooldown(20).weight(20), RipeningSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).color(156, 45, 11).consumeAmount(1000).cooldown(60).weight(15).range(3)
				.addAttribute(Attributes.ATTACK_DAMAGE, new AttributeModifier("Weapon modifier", 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL)), FlameCleaveSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).color(255, 128, 48).consumeAmount(10).cooldown(120).weight(20).range(5).useDuration(200),
				InfernoSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.AIR).color(190, 206, 237).consumeAmount(400).cooldown(40).weight(5).range(6), DashSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.EARTH).color(4, 77, 60).consumeAmount(5000).cooldown(700).weight(2).range(15), SilkVeinSpell.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.AIR).color(218, 184, 255).consumeAmount(2000).cooldown(1200).weight(2).range(100), TranslocationSpell.NAME);

		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.WATER).color(MobEffects.HEAL.getColor()).consumeAmount(1000).cooldown(600).weight(5), "heal");
		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.AIR).color(MobEffects.MOVEMENT_SPEED.getColor()).consumeAmount(4000).cooldown(2400).weight(2), "speed");
	}

	protected void save(HashCache cache, SpellProperties.Builder builder, String name) throws IOException {
		save(cache, builder.toJson(), ElementalCraft.createRL(name));
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
