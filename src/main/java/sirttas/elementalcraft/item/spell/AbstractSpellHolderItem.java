package sirttas.elementalcraft.item.spell;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
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
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.SpellTickManager;

public abstract class AbstractSpellHolderItem extends ECItem implements ISpellHolder {

	protected AbstractSpellHolderItem(Properties properties) {
		super(properties);
	}

	protected void addAttributeTooltip(List<ITextComponent> tooltip, Spell spell) {
		tooltip.add(new StringTextComponent(""));
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.consumes", spell.getElementType().getDisplayName()).withStyle(TextFormatting.YELLOW));
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.cooldown", spell.getCooldown() / 20).withStyle(TextFormatting.YELLOW));
		spell.addInformation(tooltip);
		addAttributeMultimapToTooltip(tooltip, spell.getOnUseAttributeModifiers(), new TranslationTextComponent("tooltip.elementalcraft.on_spell_use").withStyle(TextFormatting.GRAY));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return SpellHelper.getSpell(stack).getUseDuration();
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		return new ActionResult<>(tick(worldIn, playerIn, handIn, stack, true), stack);
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity entity, int count) {
		if (!(entity instanceof PlayerEntity) || tick(entity.getCommandSenderWorld(), (PlayerEntity) entity, entity.getUsedItemHand(), stack, false) != ActionResultType.CONSUME) {
			entity.releaseUsingItem();
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		finishUsingItem(stack, worldIn, entityLiving);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		SpellTickManager.getInstance(worldIn).setCooldown(entityLiving, SpellHelper.getSpell(stack));
		return stack;
	}

	private ActionResultType tick(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stack, boolean doChannel) {
		Spell spell = SpellHelper.getSpell(stack);
		Multimap<Attribute, AttributeModifier> attributes = spell.getOnUseAttributeModifiers();

		playerIn.getAttributes().addTransientAttributeModifiers(attributes);
		
		ActionResultType result = Boolean.TRUE.equals(ECConfig.COMMON.spellConsumeOnFail.get()) || spell.consume(playerIn, true) ? castSpell(playerIn, spell) : ActionResultType.FAIL;

		if (result.consumesAction()) {
			if (doConsume(playerIn, handIn, stack, spell)) {
				result = ActionResultType.SUCCESS;
			}
			if (result.shouldSwing() && !playerIn.isCreative()) {
				SpellTickManager.getInstance(worldIn).setCooldown(playerIn, spell);
				playerIn.releaseUsingItem();
			} else if (doChannel && spell.isChannelable()) {
				playerIn.startUsingItem(handIn);
			}
		} else {
			playerIn.releaseUsingItem();
		}
		playerIn.getAttributes().removeAttributeModifiers(attributes);
		return result;
	}

	private ActionResultType castSpell(PlayerEntity player, Spell spell) {
		if (SpellTickManager.getInstance(player.level).hasCooldown(player, spell)) {
			return ActionResultType.PASS;
		}
		
		ActionResultType result = ActionResultType.PASS;
		RayTraceResult ray = EntityHelper.rayTrace(player, player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue());
		RayTraceResult.Type rayType = ray.getType();
		
		if (rayType == RayTraceResult.Type.ENTITY) {
			result = spell.castOnEntity(player, ((EntityRayTraceResult) ray).getEntity());
		}
		if (rayType == RayTraceResult.Type.BLOCK && !result.consumesAction()) {
			result = spell.castOnBlock(player, ((BlockRayTraceResult) ray).getBlockPos());
		}
		if (!result.consumesAction()) {
			result = spell.castOnSelf(player);
		}
		return result;
	}
	
	private boolean doConsume(PlayerEntity playerIn, Hand handIn, ItemStack stack, Spell spell) {
		if (!playerIn.isCreative() && !spell.consume(playerIn, false)) {
			consume(stack);
			playerIn.broadcastBreakEvent(handIn);
			return true;
		}
		return false;
	}

	protected abstract void consume(ItemStack stack);
}