package sirttas.elementalcraft.datagen.managed;

import com.google.common.collect.Lists;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AttributeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.ElementCostReductionToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.EnchantmentToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;

public class ToolInfusionProvider extends AbstractManagedDataBuilderProvider<ToolInfusion, ToolInfusion> {

	public ToolInfusionProvider(DataGenerator generator) {
		super(generator, ElementalCraftApi.TOOL_INFUSION_MANAGER, ToolInfusion.CODEC);
	}

	@Override
	public void collectBuilders() {
		addEnchantment(ElementType.FIRE, Enchantments.FIRE_ASPECT);
		addEnchantment(ElementType.FIRE, Enchantments.FLAMING_ARROWS);
		addEnchantment(ElementType.FIRE, Enchantments.FIRE_PROTECTION);
		addEnchantment(ElementType.FIRE, Enchantments.PIERCING);
		addEnchantment(ElementType.FIRE, Enchantments.IMPALING);
		addEnchantment(ElementType.WATER, Enchantments.BLOCK_FORTUNE);
		addEnchantment(ElementType.WATER, Enchantments.MOB_LOOTING);
		addEnchantment(ElementType.WATER, Enchantments.FISHING_LUCK);
		addEnchantment(ElementType.WATER, Enchantments.BLAST_PROTECTION);
		addEnchantment(ElementType.WATER, Enchantments.RESPIRATION);
		addEnchantment(ElementType.WATER, Enchantments.PUNCH_ARROWS);
		addEnchantment(ElementType.WATER, Enchantments.MULTISHOT);
		addEnchantment(ElementType.WATER, Enchantments.LOYALTY);
		addEnchantment(ElementType.WATER, Enchantments.DEPTH_STRIDER);
		addEnchantment(ElementType.EARTH, Enchantments.UNBREAKING);
		addEnchantment(ElementType.EARTH, Enchantments.ALL_DAMAGE_PROTECTION);
		addEnchantment(ElementType.EARTH, Enchantments.SHARPNESS);
		addEnchantment(ElementType.EARTH, Enchantments.POWER_ARROWS);
		addEnchantment(ElementType.AIR, Enchantments.FALL_PROTECTION);
		addEnchantment(ElementType.AIR, Enchantments.BLOCK_EFFICIENCY);
		addEnchantment(ElementType.AIR, Enchantments.QUICK_CHARGE);
		addEnchantment(ElementType.AIR, Enchantments.FISHING_SPEED);
		addEnchantment(ElementType.AIR, Enchantments.RIPTIDE);
		addEnchantment(ElementType.AIR, Enchantments.PROJECTILE_PROTECTION);

		addEnchantment(ElementType.FIRE, Enchantments.UNBREAKING, "fire_unbreaking");
		addEnchantment(ElementType.WATER, Enchantments.UNBREAKING, "water_unbreaking");
		addEnchantment(ElementType.AIR, Enchantments.UNBREAKING, "air_unbreaking");

		add(ElementType.FIRE, new AutoSmeltToolInfusionEffect(), AutoSmeltToolInfusionEffect.NAME);
		add(ElementType.AIR, new DodgeToolInfusionEffect(0.1D), DodgeToolInfusionEffect.NAME);
		add(ElementType.AIR, new FastDrawToolInfusionEffect(3), FastDrawToolInfusionEffect.NAME);

		add(ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.MAINHAND), Attributes.ATTACK_SPEED, new AttributeModifier("Attack Speed Infusion", 0.8D, AttributeModifier.Operation.ADDITION)), "attack_speed");
		add(ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.LEGS), Attributes.MOVEMENT_SPEED, new AttributeModifier("Movement Speed Infusion", 0.01D, AttributeModifier.Operation.ADDITION)), "movement_speed");
		
		add(new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.1F), "fire_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.1F), "water_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.1F), "earth_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.1F), "air_reduction");

		add(new ToolInfusion(ElementType.FIRE, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.FIRE_ASPECT), new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.15F))), "fire_staff");
		add(new ToolInfusion(ElementType.WATER, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.MOB_LOOTING), new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.15F))), "water_staff");
		add(new ToolInfusion(ElementType.EARTH, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.SHARPNESS), new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.15F))), "earth_staff");
		add(new ToolInfusion(ElementType.AIR, Lists.newArrayList(new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.MAINHAND), Attributes.ATTACK_SPEED, new AttributeModifier("Attack Speed Infusion", 0.8D, AttributeModifier.Operation.ADDITION)), new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.15F))), "air_staff");
		
	}

	private void addEnchantment(ElementType type, Enchantment enchantment) {
		addEnchantment(type, enchantment, ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath());
	}

	private void addEnchantment(ElementType type, Enchantment enchantment, String name) {
		var infusion =  new EnchantmentToolInfusionEffect(enchantment);

		add(createToolInfusion(type, infusion), name);
	}

	protected void add(ElementType type, IToolInfusionEffect infusion, String name) {
		add(createToolInfusion(type, infusion), name);
	}

	private void add(ElementCostReductionToolInfusionEffect infusion, String name) {
		add(createToolInfusion(infusion.getElementType(), infusion), name);
	}

	protected void add(ToolInfusion infusion, String name) {
		add(ElementalCraft.createRL(name), infusion);
	}

	private ToolInfusion createToolInfusion(ElementType type, IToolInfusionEffect infusion) {
		return new ToolInfusion(type, Lists.newArrayList(infusion));
	}

	@Override
	public @NotNull String getName() {
		return "ElementalCraft Tool infusion";
	}
}
