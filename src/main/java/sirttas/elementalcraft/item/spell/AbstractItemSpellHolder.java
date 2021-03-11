package sirttas.elementalcraft.item.spell;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.SpellTickManager;

public abstract class AbstractItemSpellHolder extends ItemEC implements ISpellHolder {

	protected AbstractItemSpellHolder(Properties properties) {
		super(properties);
	}

	protected void addAttributeTooltip(List<ITextComponent> tooltip, Spell spell) {
		tooltip.add(new StringTextComponent(""));
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.consumes", spell.getElementType().getDisplayName()).mergeStyle(TextFormatting.YELLOW));
		spell.addInformation(tooltip);
		addAttributeMultimapToTooltip(tooltip, spell.getOnUseAttributeModifiers(), new TranslationTextComponent("tooltip.elementalcraft.on_spell_use").mergeStyle(TextFormatting.GRAY));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return SpellHelper.getSpell(stack).getUseDuration();
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		return new ActionResult<>(tick(worldIn, playerIn, handIn, stack, true), stack);
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity entity, int count) {
		if (!(entity instanceof PlayerEntity) || tick(entity.getEntityWorld(), (PlayerEntity) entity, entity.getActiveHand(), stack, false) != ActionResultType.CONSUME) {
			entity.stopActiveHand();
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		SpellTickManager.getInstance(worldIn).setCooldown(entityLiving, SpellHelper.getSpell(stack));
		return stack;
	}

	private ActionResultType tick(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stack, boolean doChannel) {
		Spell spell = SpellHelper.getSpell(stack);
		Multimap<Attribute, AttributeModifier> attributes = spell.getOnUseAttributeModifiers();

		playerIn.getAttributeManager().reapplyModifiers(attributes);

		ActionResultType result = castSpell(playerIn, spell);


		if (result.isSuccessOrConsume()) {
			if (doConsume(playerIn, handIn, stack, spell)) {
				result = ActionResultType.SUCCESS;
			}
			if (result.isSuccess() && !playerIn.isCreative()) {
				SpellTickManager.getInstance(worldIn).setCooldown(playerIn, spell);
				playerIn.stopActiveHand();
			} else if (doChannel && spell.isChannelable()) {
				playerIn.setActiveHand(handIn);
			}
		} else {
			playerIn.stopActiveHand();
		}
		playerIn.getAttributeManager().removeModifiers(attributes);
		return result;
	}

	private ActionResultType castSpell(PlayerEntity player, Spell spell) {
		ActionResultType result = ActionResultType.PASS;
		RayTraceResult ray = EntityHelper.rayTrace(player, player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue());

		if (SpellTickManager.getInstance(player.world).hasCooldown(player, spell)) {
			return result;
		}
		
		if (ray.getType() == RayTraceResult.Type.ENTITY) {
			result = spell.castOnEntity(player, ((EntityRayTraceResult) ray).getEntity());
		}
		if (ray.getType() == RayTraceResult.Type.BLOCK && !result.isSuccessOrConsume()) {
			result = spell.castOnBlock(player, ((BlockRayTraceResult) ray).getPos());
		}
		if (!result.isSuccessOrConsume()) {
			result = spell.castOnSelf(player);
		}
		return result;
	}
	
	private boolean doConsume(PlayerEntity playerIn, Hand handIn, ItemStack stack, Spell spell) {
		if (!playerIn.isCreative() && !spell.consume(playerIn)) {
			consume(stack);
			playerIn.sendBreakAnimation(handIn);
			return true;
		}
		return false;
	}

	protected abstract void consume(ItemStack stack);

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		return SpellHelper.getSpell(stack).getAttributeModifiers(equipmentSlot);
	}
}