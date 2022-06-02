package sirttas.elementalcraft.datagen.managed;

import com.google.common.collect.Lists;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantments;
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
		add(ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FIRE_ASPECT));
		add(ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FLAMING_ARROWS));
		add(ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.FIRE_PROTECTION));
		add(ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.PIERCING));
		add(ElementType.FIRE, new EnchantmentToolInfusionEffect(Enchantments.IMPALING));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.BLOCK_FORTUNE));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.MOB_LOOTING));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.FISHING_LUCK));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.BLAST_PROTECTION));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.RESPIRATION));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.PUNCH_ARROWS));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.MULTISHOT));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.LOYALTY));
		add(ElementType.WATER, new EnchantmentToolInfusionEffect(Enchantments.DEPTH_STRIDER));
		add(ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.UNBREAKING));
		add(ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.ALL_DAMAGE_PROTECTION));
		add(ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.SHARPNESS));
		add(ElementType.EARTH, new EnchantmentToolInfusionEffect(Enchantments.POWER_ARROWS));
		add(ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.FALL_PROTECTION));
		add(ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.BLOCK_EFFICIENCY));
		add(ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.QUICK_CHARGE));
		add(ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.FISHING_SPEED));
		add(ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.RIPTIDE));
		add(ElementType.AIR, new EnchantmentToolInfusionEffect(Enchantments.PROJECTILE_PROTECTION));

		add(ElementType.FIRE, new AutoSmeltToolInfusionEffect(), AutoSmeltToolInfusionEffect.NAME);
		add(ElementType.AIR, new DodgeToolInfusionEffect(0.1D), DodgeToolInfusionEffect.NAME);
		add(ElementType.AIR, new FastDrawToolInfusionEffect(3), FastDrawToolInfusionEffect.NAME);

		add(ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.MAINHAND), Attributes.ATTACK_SPEED,
				new AttributeModifier("Attack Speed Infusion", 0.8D, AttributeModifier.Operation.ADDITION)), "attack_speed");
		add(ElementType.AIR, new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.LEGS), Attributes.MOVEMENT_SPEED,
				new AttributeModifier("Movement Speed Infusion", 0.01D, AttributeModifier.Operation.ADDITION)), "movement_speed");
		
		add(new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.1F), "fire_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.1F), "water_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.1F), "earth_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.1F), "air_reduction");

		add(new ToolInfusion(ElementType.FIRE, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.FIRE_ASPECT), 
				new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.15F))), "fire_staff");
		add(new ToolInfusion(ElementType.WATER, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.MOB_LOOTING), 
				new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.15F))), "water_staff");
		add(new ToolInfusion(ElementType.EARTH, Lists.newArrayList(new EnchantmentToolInfusionEffect(Enchantments.SHARPNESS), 
				new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.15F))), "earth_staff");
		add(new ToolInfusion(ElementType.AIR, Lists.newArrayList(new AttributeToolInfusionEffect(Lists.newArrayList(EquipmentSlot.MAINHAND), Attributes.ATTACK_SPEED,
				new AttributeModifier("Attack Speed Infusion", 0.8D, AttributeModifier.Operation.ADDITION)), new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.15F))), "air_staff");
		
	}

	protected void add(ElementType type, IToolInfusionEffect infusion, String name) {
		add(createToolInfusion(type, infusion), name);
	}

	private void add(ElementCostReductionToolInfusionEffect infusion, String name) {
		add(createToolInfusion(infusion.getElementType(), infusion), name);
	}
	
	protected void add(ElementType type, EnchantmentToolInfusionEffect infusion) {
		add(createToolInfusion(type, infusion), infusion.getEnchantment().getRegistryName().getPath());
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
