package sirttas.elementalcraft.datagen.managed;

import java.io.IOException;
import java.nio.file.Path;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AttributeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.ElementCostReductionToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.EnchantmentToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;

public class ToolInfusionProvider implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public ToolInfusionProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void run(HashCache cache) throws IOException {
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FIRE_ASPECT));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FLAMING_ARROWS));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FIRE_PROTECTION));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.PIERCING));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.IMPALING));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.BLOCK_FORTUNE));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.MOB_LOOTING));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.FISHING_LUCK));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.BLAST_PROTECTION));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.RESPIRATION));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.PUNCH_ARROWS));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.MULTISHOT));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.LOYALTY));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.DEPTH_STRIDER));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.UNBREAKING));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.ALL_DAMAGE_PROTECTION));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.SHARPNESS));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.POWER_ARROWS));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.FALL_PROTECTION));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.BLOCK_EFFICIENCY));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.QUICK_CHARGE));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.FISHING_SPEED));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.RIPTIDE));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.PROJECTILE_PROTECTION));

		save(cache, ElementType.FIRE, new AutoSmeltToolInfusionEffect(), AutoSmeltToolInfusionEffect.NAME);
		save(cache, ElementType.AIR, new DodgeToolInfusionEffect(0.1D), DodgeToolInfusionEffect.NAME);
		save(cache, ElementType.AIR, new FastDrawToolInfusionEffect(3), FastDrawToolInfusionEffect.NAME);

		save(cache, ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.MAINHAND), Attributes.ATTACK_SPEED,
				new AttributeModifier("Attack Speed Infusion", 0.8D, AttributeModifier.Operation.ADDITION)), "attack_speed");
		save(cache, ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.LEGS), Attributes.MOVEMENT_SPEED,
				new AttributeModifier("Movement Speed Infusion", 0.01D, AttributeModifier.Operation.ADDITION)), "movement_speed");
		
		save(cache, new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.1F), "fire_reduction");
		save(cache, new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.1F), "water_reduction");
		save(cache, new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.1F), "earth_reduction");
		save(cache, new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.1F), "air_reduction");

		save(cache, new ToolInfusion(ElementType.FIRE, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.FIRE_ASPECT), 
				new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.15F))), "fire_staff");
		save(cache, new ToolInfusion(ElementType.WATER, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.MOB_LOOTING), 
				new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.15F))), "water_staff");
		save(cache, new ToolInfusion(ElementType.EARTH, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.SHARPNESS), 
				new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.15F))), "earth_staff");
		save(cache, new ToolInfusion(ElementType.AIR, Lists.newArrayList(new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.MAINHAND), Attributes.ATTACK_SPEED,
				new AttributeModifier("Attack Speed Infusion", 0.8D, AttributeModifier.Operation.ADDITION)), new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.15F))), "air_staff");
		
	}

	protected void save(HashCache cache, ElementType type, IToolInfusionEffect infusion, String name) throws IOException {
		save(cache, createToolInfusion(type, infusion), name);
	}


	private void save(HashCache cache, ElementCostReductionToolInfusionEffect infusion, String name) throws IOException {
		save(cache, createToolInfusion(infusion.getElementType(), infusion), name);
	}
	
	protected void save(HashCache cache, ElementType type, EnchantmentToolInfusionEffect infusion) throws IOException {
		save(cache, createToolInfusion(type, infusion), infusion.getEnchantment().getRegistryName().getPath());
	}

	protected void save(HashCache cache, ToolInfusion infusion, String name) throws IOException {
		DataProvider.save(GSON, cache, CodecHelper.encode(ToolInfusion.CODEC, infusion), getPath(ElementalCraft.createRL(name)));
	}

	private ToolInfusion createToolInfusion(ElementType type, IToolInfusionEffect infusion) {
		return new ToolInfusion(type, Lists.newArrayList(infusion));
	}

	private Path getPath(ResourceLocation id) {
		return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/" + ToolInfusion.FOLDER + "/" + id.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "ElementalCraft Tool infusion";
	}
}
