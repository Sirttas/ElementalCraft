package sirttas.elementalcraft.datagen.managed;

import java.io.IOException;
import java.nio.file.Path;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.infusion.tool.effect.AttributeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.EnchantmentToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.IToolInfusionEffect;

public class ToolInfusionProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;

	public ToolInfusionProvider(DataGenerator generatorIn) {
		this.generator = generatorIn;
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FIRE_ASPECT));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FLAME));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FIRE_PROTECTION));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.PIERCING));
		save(cache, ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.IMPALING));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.FORTUNE));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.LOOTING));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.LUCK_OF_THE_SEA));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.BLAST_PROTECTION));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.RESPIRATION));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.PUNCH));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.MULTISHOT));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.LOYALTY));
		save(cache, ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.DEPTH_STRIDER));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.UNBREAKING));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.PROTECTION));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.SHARPNESS));
		save(cache, ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.POWER));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.FEATHER_FALLING));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.EFFICIENCY));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.QUICK_CHARGE));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.LURE));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.RIPTIDE));
		save(cache, ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.PROJECTILE_PROTECTION));

		save(cache, ElementType.FIRE, new AutoSmeltToolInfusionEffect(), AutoSmeltToolInfusionEffect.NAME);
		save(cache, ElementType.AIR, new DodgeToolInfusionEffect(0.1D), DodgeToolInfusionEffect.NAME);
		save(cache, ElementType.AIR, new FastDrawToolInfusionEffect(3), FastDrawToolInfusionEffect.NAME);

		save(cache, ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlotType.MAINHAND), Attributes.ATTACK_SPEED,
				new AttributeModifier("Attack Speed Infusion", 0.8D, AttributeModifier.Operation.ADDITION)), "attack_speed");
		save(cache, ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlotType.LEGS), Attributes.MOVEMENT_SPEED,
				new AttributeModifier("Movement Speed Infusion", 0.01D, AttributeModifier.Operation.ADDITION)), "movement_speed");

	}

	protected void save(DirectoryCache cache, ElementType type, IToolInfusionEffect infusion, String name) throws IOException {
		save(cache, createToolInfusion(type, infusion), name);
	}

	protected void save(DirectoryCache cache, ElementType type, EnchantmentToolInfusionEffect infusion) throws IOException {
		save(cache, createToolInfusion(type, infusion), infusion.getEnchantment().getRegistryName().getPath());
	}

	protected void save(DirectoryCache cache, ToolInfusion infusion, String name) throws IOException {
		IDataProvider.save(GSON, cache, CodecHelper.encode(ToolInfusion.CODEC, infusion), getPath(ElementalCraft.createRL(name)));
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
