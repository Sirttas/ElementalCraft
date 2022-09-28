package sirttas.elementalcraft.item.spell;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.Lazy;
import sirttas.elementalcraft.ElementalCraftTab;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class StaffItem extends FocusItem {

	public static final String NAME = "staff";

	protected static final UUID BASE_ATTACK_RANGE_UUID = UUID.fromString("f413e701-0c60-4333-a9bb-a9dc0d73901e");

	private static final Lazy<Multimap<Attribute, AttributeModifier>> ATTRIBUTE_MODIFIERS = Lazy.of(() -> {
		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 8, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.2, AttributeModifier.Operation.ADDITION));
		builder.put(ForgeMod.ATTACK_RANGE.get(), new AttributeModifier(BASE_ATTACK_RANGE_UUID, "Attack range", 1, AttributeModifier.Operation.ADDITION));
		return builder.build();
	});


	public StaffItem() {
		super(new Item.Properties().tab(ElementalCraftTab.TAB).durability(2252).fireResistant());
	}

	@Override
	public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();

			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean mineBlock(@Nonnull ItemStack stack, @Nonnull Level level, BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entity) {
		if (state.getDestroySpeed(level, pos) > 0.0F) {
			stack.hurtAndBreak(2, entity, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}
		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		return state.is(Blocks.COBWEB);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
		
		map.putAll(super.getAttributeModifiers(equipmentSlot, stack));
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			map.putAll(ATTRIBUTE_MODIFIERS.get());
		}
		return map;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.staff.range").withStyle(ChatFormatting.BLUE));
	}
	
	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction) || super.canPerformAction(stack, toolAction);
	}
	
	@Override
    @Nonnull
	public AABB getSweepHitBox(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull Entity target) {
		var playerAABB = player.getBoundingBox().inflate(2, 0.25, 2);
		var targetAABB = super.getSweepHitBox(stack, player, target);

		return new AABB(
				Math.min(playerAABB.minX, targetAABB.minX),
				Math.min(playerAABB.minY, targetAABB.minY),
				Math.min(playerAABB.minZ, targetAABB.minZ),
				Math.max(playerAABB.maxX, targetAABB.maxX),
				Math.max(playerAABB.maxY, targetAABB.maxY),
				Math.max(playerAABB.maxZ, targetAABB.maxZ)
		);
	}

	@Override
	public boolean isValidRepairItem(@Nonnull ItemStack toRepair, ItemStack repair) {
		return repair.is(ECTags.Items.INGOTS_FIREITE);
	}

	@Override
	@Deprecated
	public int getEnchantmentValue() {
		return 16;
	}

	@Override
	@Deprecated
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment.category == EnchantmentCategory.WEAPON || super.canApplyAtEnchantingTable(stack, enchantment);
	}
}
