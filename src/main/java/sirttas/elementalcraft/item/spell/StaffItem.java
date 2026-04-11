package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class StaffItem extends FocusItem {

	public static final String NAME = "staff";

	public static final Identifier BASE_ATTACK_RANGE_ID = ElementalCraftApi.createRL("staff_attack_range");

	public static final ItemAttributeModifiers ATTRIBUTE_MODIFIERS = ItemAttributeModifiers.builder()
			.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(BASE_ATTACK_RANGE_ID, 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
			.build();


	public StaffItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		}
		return state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
	}

	@Override
	public void hurtEnemy(ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
		stack.hurtAndBreak(1, target, EquipmentSlot.MAINHAND);
	}

    @Override
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("tooltip.elementalcraft.staff.ranges").withStyle(ChatFormatting.BLUE));
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
}
