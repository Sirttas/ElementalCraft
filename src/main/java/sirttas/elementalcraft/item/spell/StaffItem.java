package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class StaffItem extends FocusItem {

	public static final String NAME = "staff";

	public static final ResourceLocation BASE_ATTACK_RANGE_ID = ElementalCraftApi.createRL("staff_attack_range");

	public static final ItemAttributeModifiers ATTRIBUTE_MODIFIERS = ItemAttributeModifiers.builder()
			.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(BASE_ATTACK_RANGE_ID, 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.build();


	public StaffItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		}
		return state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
		stack.hurtAndBreak(1, target, EquipmentSlot.MAINHAND);
		return true;
	}
	
	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		super.appendHoverText(stack, tooltipContext, tooltip, flag);
		tooltip.add(Component.translatable("tooltip.elementalcraft.staff.ranges").withStyle(ChatFormatting.BLUE));
	}
	
	@Override
	public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
		return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility) || super.canPerformAction(stack, itemAbility);
	}

	@Nonnull
	@Override
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

}
