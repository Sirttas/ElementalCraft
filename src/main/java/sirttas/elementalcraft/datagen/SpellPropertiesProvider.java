package sirttas.elementalcraft.datagen;

import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.air.SpellDash;
import sirttas.elementalcraft.spell.air.SpellEnderStrike;
import sirttas.elementalcraft.spell.air.SpellItemPull;
import sirttas.elementalcraft.spell.earth.SpellGavelFall;
import sirttas.elementalcraft.spell.earth.SpellSilkVein;
import sirttas.elementalcraft.spell.earth.SpellStoneWall;
import sirttas.elementalcraft.spell.earth.SpellTreeFall;
import sirttas.elementalcraft.spell.fire.SpellFireBall;
import sirttas.elementalcraft.spell.fire.SpellFlameCleave;
import sirttas.elementalcraft.spell.fire.SpellInferno;
import sirttas.elementalcraft.spell.properties.SpellProperties;
import sirttas.elementalcraft.spell.water.SpellAnimalGrowth;
import sirttas.elementalcraft.spell.water.SpellPurification;
import sirttas.elementalcraft.spell.water.SpellRipening;

public class SpellPropertiesProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public SpellPropertiesProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.EARTH).color(175, 179, 179).consumeAmount(250).cooldown(40).weight(20), SpellGavelFall.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.EARTH).color(207, 212, 212).consumeAmount(500).cooldown(100).weight(20), SpellStoneWall.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).color(245, 174, 22).consumeAmount(500).cooldown(100).weight(30), SpellFireBall.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.AIR).color(250, 252, 222).consumeAmount(1000).cooldown(200).weight(5).range(10), SpellItemPull.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.AIR).color(103, 15, 105).consumeAmount(1000).cooldown(60).weight(15).range(20), SpellEnderStrike.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.WATER).color(0, 134, 161).consumeAmount(2000).cooldown(200).weight(10), SpellAnimalGrowth.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.EARTH).color(0, 128, 34).consumeAmount(3000).cooldown(600).weight(5).range(15), SpellTreeFall.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.WATER).color(5, 207, 247).consumeAmount(1000).cooldown(200).weight(10), SpellPurification.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.WATER).color(0, 189, 126).consumeAmount(200).cooldown(20).weight(20), SpellRipening.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).color(156, 45, 11).consumeAmount(1000).cooldown(60).weight(15).range(3), SpellFlameCleave.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.COMBAT).elementType(ElementType.FIRE).color(255, 128, 48).consumeAmount(10).cooldown(120).weight(20).range(5).useDuration(200),
				SpellInferno.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.AIR).color(190, 206, 237).consumeAmount(400).cooldown(40).weight(5).range(6), SpellDash.NAME);
		save(cache, SpellProperties.Builder.create(Spell.Type.UTILITY).elementType(ElementType.EARTH).color(4, 77, 60).consumeAmount(5000).cooldown(700).weight(2).range(15), SpellSilkVein.NAME);

		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.WATER).color(Effects.INSTANT_HEALTH.getLiquidColor()).consumeAmount(1000).cooldown(600).weight(5), "heal");
		save(cache, SpellProperties.Builder.create(Spell.Type.MIXED).elementType(ElementType.AIR).color(Effects.SPEED.getLiquidColor()).consumeAmount(4000).cooldown(2400).weight(2), "speed");
	}

	protected void save(DirectoryCache cache, SpellProperties.Builder builder, String name) throws IOException {
		IDataProvider.save(GSON, cache, builder.toJson(), getPath(ElementalCraft.createRL(name)));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/elementalcraft_spell_properties/" + id.getPath() + ".json");
	}


	@Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
