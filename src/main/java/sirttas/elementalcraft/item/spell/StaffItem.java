package sirttas.elementalcraft.item.spell;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraftTab;

public class StaffItem extends FocusItem {

	public static final String NAME = "staff";
	
	private static final Multimap<Attribute, AttributeModifier> ATTRIBUTE_MODIFIERS;
	
	static {
	    Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
	    
	    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 8, AttributeModifier.Operation.ADDITION));
	    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.2, AttributeModifier.Operation.ADDITION));
	    ATTRIBUTE_MODIFIERS = builder.build();
	}
	
	public StaffItem() {
		super(new Item.Properties().tab(ElementalCraftTab.TAB).durability(2252).fireResistant());
	}

	@Override
	public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return !player.isCreative();
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (state.getDestroySpeed(worldIn, pos) > 0.0F) {
			stack.hurtAndBreak(2, entityLiving, entity -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
		}

		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState blockIn) {
		return blockIn.is(Blocks.COBWEB);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
		
				map.putAll(super.getAttributeModifiers(equipmentSlot, stack));
		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			map.putAll(ATTRIBUTE_MODIFIERS);
		}
		return map;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.staff.range").withStyle(TextFormatting.BLUE));
	}

}
