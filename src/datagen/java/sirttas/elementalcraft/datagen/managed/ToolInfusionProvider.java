package sirttas.elementalcraft.datagen.managed;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ToolInfusionProvider extends AbstractManagedDataBuilderProvider<ToolInfusion, ToolInfusion> {

	private static final ResourceLocation ATTACK_SEED_ID = ElementalCraftApi.createRL("tool_infusion_attack_speed");
	private static final ResourceLocation MOVEMENT_SPEED_ID = ElementalCraftApi.createRL("tool_infusion_movement_speed");

	public ToolInfusionProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries, ElementalCraftApi.TOOL_INFUSION_MANAGER, ToolInfusion.CODEC);
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		var enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);

		addEnchantment(ElementType.FIRE, enchantments.getOrThrow(Enchantments.FIRE_ASPECT));
		addEnchantment(ElementType.FIRE, enchantments.getOrThrow(Enchantments.FLAME));
		addEnchantment(ElementType.FIRE, enchantments.getOrThrow(Enchantments.FIRE_PROTECTION));
		addEnchantment(ElementType.FIRE, enchantments.getOrThrow(Enchantments.PIERCING));
		addEnchantment(ElementType.FIRE, enchantments.getOrThrow(Enchantments.IMPALING));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.FORTUNE));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.LOOTING));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.LUCK_OF_THE_SEA));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.BLAST_PROTECTION));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.RESPIRATION));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.PUNCH));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.MULTISHOT));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.LOYALTY));
		addEnchantment(ElementType.WATER, enchantments.getOrThrow(Enchantments.DEPTH_STRIDER));
		addEnchantment(ElementType.EARTH, enchantments.getOrThrow(Enchantments.UNBREAKING));
		addEnchantment(ElementType.EARTH, enchantments.getOrThrow(Enchantments.PROTECTION));
		addEnchantment(ElementType.EARTH, enchantments.getOrThrow(Enchantments.SHARPNESS));
		addEnchantment(ElementType.EARTH, enchantments.getOrThrow(Enchantments.POWER));
		addEnchantment(ElementType.AIR, enchantments.getOrThrow(Enchantments.FEATHER_FALLING));
		addEnchantment(ElementType.AIR, enchantments.getOrThrow(Enchantments.EFFICIENCY));
		addEnchantment(ElementType.AIR, enchantments.getOrThrow(Enchantments.QUICK_CHARGE));
		addEnchantment(ElementType.AIR, enchantments.getOrThrow(Enchantments.LURE));
		addEnchantment(ElementType.AIR, enchantments.getOrThrow(Enchantments.RIPTIDE));
		addEnchantment(ElementType.AIR, enchantments.getOrThrow(Enchantments.PROJECTILE_PROTECTION));

		add(ElementType.FIRE, new AutoSmeltToolInfusionEffect(), AutoSmeltToolInfusionEffect.NAME);
		add(ElementType.AIR, new DodgeToolInfusionEffect(0.1D), DodgeToolInfusionEffect.NAME);
		add(ElementType.AIR, new FastDrawToolInfusionEffect(3), FastDrawToolInfusionEffect.NAME);

		add(ElementType.AIR, new AttributeToolInfusionEffect(EquipmentSlotGroup.MAINHAND, Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SEED_ID, 0.8D, AttributeModifier.Operation.ADD_VALUE)), "attack_speed");
		add(ElementType.AIR, new AttributeToolInfusionEffect(EquipmentSlotGroup.LEGS, Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_ID, 0.01D, AttributeModifier.Operation.ADD_VALUE)), "movement_speed");
		
		add(new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.1F), "fire_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.1F), "water_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.1F), "earth_reduction");
		add(new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.1F), "air_reduction");

		add(new ToolInfusion(ElementType.FIRE, List.of(new EnchantmentToolInfusionEffect(enchantments.getOrThrow(Enchantments.FIRE_ASPECT)), new ElementCostReductionToolInfusionEffect(ElementType.FIRE, 0.15F))), "fire_staff");
		add(new ToolInfusion(ElementType.WATER, List.of(new EnchantmentToolInfusionEffect(enchantments.getOrThrow(Enchantments.LOOTING)), new ElementCostReductionToolInfusionEffect(ElementType.WATER, 0.15F))), "water_staff");
		add(new ToolInfusion(ElementType.EARTH, List.of(new EnchantmentToolInfusionEffect(enchantments.getOrThrow(Enchantments.SHARPNESS)), new ElementCostReductionToolInfusionEffect(ElementType.EARTH, 0.15F))), "earth_staff");
		add(new ToolInfusion(ElementType.AIR, List.of(new AttributeToolInfusionEffect(EquipmentSlotGroup.MAINHAND, Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SEED_ID, 0.8D, AttributeModifier.Operation.ADD_VALUE)), new ElementCostReductionToolInfusionEffect(ElementType.AIR, 0.15F))), "air_staff");
		
	}

	private void addEnchantment(ElementType type, Holder<Enchantment> enchantment) {
		addEnchantment(type, enchantment, enchantment.unwrapKey()
				.map(k -> k.location().getPath())
				.orElseThrow(() -> new IllegalArgumentException("Enchantment has no key")));
	}

	private void addEnchantment(ElementType type, Holder<Enchantment> enchantment, String name) {
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
		add(ElementalCraftApi.createRL(name), infusion);
	}

	private ToolInfusion createToolInfusion(ElementType type, IToolInfusionEffect infusion) {
		return new ToolInfusion(type, Lists.newArrayList(infusion));
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft Tool Infusions";
	}
}
