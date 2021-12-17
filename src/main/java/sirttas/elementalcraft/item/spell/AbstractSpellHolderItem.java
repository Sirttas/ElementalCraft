package sirttas.elementalcraft.item.spell;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraftforge.common.ForgeMod;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.SpellTickManager;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractSpellHolderItem extends ECItem implements ISpellHolder {

	protected AbstractSpellHolderItem(Properties properties) {
		super(properties);
	}

	protected void addAttributeTooltip(List<Component> tooltip, Spell spell) {
		tooltip.add(new TextComponent(""));
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.consumes", spell.getElementType().getDisplayName()).withStyle(ChatFormatting.YELLOW));
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.cooldown", spell.getCooldown() / 20).withStyle(ChatFormatting.YELLOW));
		spell.addInformation(tooltip);
		addAttributeMultiMapToTooltip(tooltip, spell.getOnUseAttributeModifiers(), new TranslatableComponent("tooltip.elementalcraft.on_spell_use").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public int getUseDuration(@Nonnull ItemStack stack) {
		return SpellHelper.getSpell(stack).getUseDuration();
	}

	@Nonnull
    @Override
	public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
		return UseAnim.BOW;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Nonnull
    @Override
	public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		return new InteractionResultHolder<>(tick(worldIn, playerIn, handIn, stack, true), stack);
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity entity, int count) {
		if (!(entity instanceof Player) || tick(entity.getCommandSenderWorld(), (Player) entity, entity.getUsedItemHand(), stack, false) != InteractionResult.CONSUME) {
			entity.releaseUsingItem();
		}
	}

	@Override
	public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull LivingEntity entityLiving, int timeLeft) {
		finishUsingItem(stack, worldIn, entityLiving);
	}

	@Nonnull
    @Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, Level level, @Nonnull LivingEntity entityLiving) {
		if (!level.isClientSide) {
			SpellTickManager.getInstance(level).setCooldown(entityLiving, SpellHelper.getSpell(stack));
		}
		return stack;
	}

	private InteractionResult tick(Level level, Player playerIn, InteractionHand handIn, ItemStack stack, boolean doChannel) {
		Spell spell = SpellHelper.getSpell(stack);
		Multimap<Attribute, AttributeModifier> attributes = spell.getOnUseAttributeModifiers();

		playerIn.getAttributes().addTransientAttributeModifiers(attributes);
		
		InteractionResult result = Boolean.TRUE.equals(ECConfig.COMMON.spellConsumeOnFail.get()) || spell.consume(playerIn, true) ? castSpell(playerIn, spell) : InteractionResult.FAIL;

		if (result.consumesAction()) {
			if (doConsume(playerIn, handIn, stack, spell)) {
				result = InteractionResult.SUCCESS;
			}
			if (result.shouldSwing() && !playerIn.isCreative()) {
				if (!level.isClientSide) {
					SpellTickManager.getInstance(level).setCooldown(playerIn, spell);
				}
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

	private InteractionResult castSpell(Player player, Spell spell) {
		if (SpellTickManager.getInstance(player.level).hasCooldown(player, spell)) {
			return InteractionResult.PASS;
		}
		
		InteractionResult result = InteractionResult.PASS;
		HitResult ray = EntityHelper.rayTrace(player, player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue());
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
	
	private boolean doConsume(Player playerIn, InteractionHand handIn, ItemStack stack, Spell spell) {
		if (!playerIn.isCreative() && !spell.consume(playerIn, false)) {
			consume(stack);
			playerIn.broadcastBreakEvent(handIn);
			return true;
		}
		return false;
	}

	protected abstract void consume(ItemStack stack);
}
