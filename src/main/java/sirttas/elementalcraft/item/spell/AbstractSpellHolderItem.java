package sirttas.elementalcraft.item.spell;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolAction;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.SpellTickManager;
import sirttas.elementalcraft.spell.ToolActionSpell;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public abstract class AbstractSpellHolderItem extends ECItem implements ISpellHolder {

	protected AbstractSpellHolderItem(Properties properties) {
		super(properties);
	}

	protected void addAttributeTooltip(List<Component> tooltip, Spell spell) {
		tooltip.add(Component.empty());
		tooltip.add(Component.translatable("tooltip.elementalcraft.consumes", spell.getElementType().getDisplayName()).withStyle(ChatFormatting.YELLOW));
		tooltip.add(Component.translatable("tooltip.elementalcraft.cooldown", spell.getCooldown() / 20).withStyle(ChatFormatting.YELLOW));
		spell.addInformation(tooltip);
		addAttributeMultiMapToTooltip(tooltip, spell.getOnUseAttributeModifiers(), Component.translatable("tooltip.elementalcraft.on_spell_use").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public int getUseDuration(@Nonnull ItemStack stack) {
		return SpellHelper.getSpell(stack).getUseDuration();
	}

	@Nonnull
    @Override
	public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
		return SpellHelper.getSpell(stack).getUseAnimation();
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Nonnull
    @Override
	public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		return new InteractionResultHolder<>(tick(level, player, hand, stack, true), stack);
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity entity, int count) {
		if (!(entity instanceof Player player) || tick(entity.getLevel(), player, entity.getUsedItemHand(), stack, false) != InteractionResult.CONSUME) {
			entity.releaseUsingItem();
		}
	}

	@Override
	public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entityLiving, int timeLeft) {
		finishUsingItem(stack, level, entityLiving);
	}

	@Nonnull
    @Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, Level level, @Nonnull LivingEntity entityLiving) {
		if (!level.isClientSide && !(entityLiving instanceof Player player && player.isCreative())) {
			SpellTickManager.getInstance(level).setCooldown(entityLiving, SpellHelper.getSpell(stack));
		}
		return stack;
	}

	private InteractionResult tick(Level level, Player player, InteractionHand hand, ItemStack stack, boolean doChannel) {
		Spell spell = SpellHelper.getSpell(stack);
		Multimap<Attribute, AttributeModifier> attributes = spell.getOnUseAttributeModifiers();

		player.getAttributes().addTransientAttributeModifiers(attributes);
		
		InteractionResult result = Boolean.TRUE.equals(ECConfig.COMMON.spellConsumeOnFail.get()) || spell.consume(player, true) ? castSpell(player, spell) : InteractionResult.FAIL;

		if (result.consumesAction()) {
			if (doConsume(player, hand, stack, spell)) {
				result = InteractionResult.SUCCESS;
			}
			if (result.shouldSwing() && !player.isCreative()) {
				if (!level.isClientSide) {
					SpellTickManager.getInstance(level).setCooldown(player, spell);
				}
				player.releaseUsingItem();
			} else if (doChannel && spell.isChannelable()) {
				player.startUsingItem(hand);
			}
		} else {
			player.releaseUsingItem();
		}
		player.getAttributes().removeAttributeModifiers(attributes);
		return result;
	}

	private InteractionResult castSpell(Player player, Spell spell) {
		if (SpellTickManager.getInstance(player.level).hasCooldown(player, spell)) {
			return InteractionResult.PASS;
		}
		
		InteractionResult result = InteractionResult.PASS;
		HitResult ray = EntityHelper.rayTrace(player);
		HitResult.Type rayType = ray.getType();
		
		if (rayType == HitResult.Type.ENTITY) {
			result = spell.castOnEntity(player, ((EntityHitResult) ray).getEntity());
		}
		if (rayType == HitResult.Type.BLOCK && !result.consumesAction()) {
			result = spell.castOnBlock(player, ((BlockHitResult) ray).getBlockPos());
		}
		if (!result.consumesAction()) {
			result = spell.castOnSelf(player);
		}
		return result;
	}
	
	private boolean doConsume(Player player, InteractionHand hand, ItemStack stack, Spell spell) {
		if (!player.isCreative() && !spell.consume(player, false)) {
			consume(stack);
			player.broadcastBreakEvent(hand);
			return true;
		}
		return false;
	}

	protected abstract void consume(ItemStack stack);

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return SpellHelper.getSpell(stack) instanceof ToolActionSpell toolActionSpell && toolActionSpell.getActions().contains(toolAction);
	}
}
