package sirttas.elementalcraft.item.spell;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.attributes.AttributesHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.TooltipHelper;
import sirttas.elementalcraft.spell.ItemAbilitySpell;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class AbstractSpellHolderItem extends Item implements ISpellHolder {

	protected AbstractSpellHolderItem(Properties properties) {
		super(properties);
	}

	protected void addAttributeTooltip(Consumer<Component> builder, Spell spell) {
        builder.accept(Component.empty());
        builder.accept(Component.translatable("tooltip.elementalcraft.consumes", spell.getElementType().getDisplayName()).withStyle(ChatFormatting.YELLOW));
        builder.accept(Component.translatable("tooltip.elementalcraft.cooldown", spell.getCooldown() / 20).withStyle(ChatFormatting.YELLOW));
		spell.addInformation(builder);
		TooltipHelper.addAttributeMultiMapToTooltip(builder, spell.getOnUseAttributeModifiers(), Component.translatable("tooltip.elementalcraft.on_spell_use").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public int getUseDuration(@Nonnull ItemStack stack, @NotNull LivingEntity entity) {
		return SpellHelper.getSpell(stack).value().getUseDuration();
	}

	@Nonnull
    @Override
	public ItemUseAnimation getUseAnimation(@Nonnull ItemStack stack) {
		return SpellHelper.getSpell(stack).value().getUseAnimation();
	}

	/**
     * Called when the equipped item is right clicked.
     */
	@Nonnull
    @Override
	public InteractionResult use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		return tick(level, player, hand, stack, true);
	}

	@Override
	public void onUseTick(@Nonnull Level level, @Nonnull LivingEntity entity, @Nonnull ItemStack stack, int count) {
		if (!(entity instanceof Player player) || tick(entity.level(), player, entity.getUsedItemHand(), stack, false) != InteractionResult.CONSUME) {
			entity.releaseUsingItem();
		}
	}

	@Override
	public boolean releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entityLiving, int timeLeft) {
		finishUsingItem(stack, level, entityLiving);
        return super.releaseUsing(stack, level, entityLiving, timeLeft);
	}

	@Nonnull
    @Override
	public ItemStack finishUsingItem(@Nonnull ItemStack stack, Level level, @Nonnull LivingEntity entityLiving) {
		if (!level.isClientSide() && !(entityLiving instanceof Player player && player.getAbilities().instabuild)) {
			SpellTickHelper.startCooldown(entityLiving, SpellHelper.getSpell(stack).value());
		}
		return stack;
	}

	private InteractionResult tick(Level level, Player player, InteractionHand hand, ItemStack stack, boolean doChannel) {
		var spell = SpellHelper.getSpell(stack).value();
		var attributes = spell.getOnUseAttributeModifiers();
		var attributeMap = player.getAttributes();

		AttributesHelper.addAttributes(attributeMap, attributes);

        SpellCastResult result = ECConfig.SERVER.spellConsumeOnFail.get() || spell.consume(player, true) ? castSpell(level, player, spell) : SpellCastResult.PASS;

		if (result.success()) {
			if (result.consume()) {
                doConsume(player, hand, stack, spell);
			}
			if (result.startCooldown() && !player.getAbilities().instabuild) {
				if (!level.isClientSide()) {
					SpellTickHelper.startCooldown(player, spell);
				}
				player.releaseUsingItem();
			} else if (doChannel && spell.isChannelable()) {
				player.startUsingItem(hand);
			}
		} else {
			player.releaseUsingItem();
		}
		AttributesHelper.removeAttributes(attributeMap, attributes);
		return result.interactionResult();
	}

	private SpellCastResult castSpell(Level level, Player player, Spell spell) {
		if (SpellTickHelper.hasCooldown(player, spell)) {
			return SpellCastResult.PASS;
		}

        SpellCastResult result = SpellCastResult.PASS;
		HitResult ray = EntityHelper.rayTrace(player);
		HitResult.Type rayType = ray.getType();
		
		if (rayType == HitResult.Type.ENTITY && ray instanceof EntityHitResult entityRay) {
			result = spell.castOnEntity(level, player, entityRay.getEntity());
		}
		if (rayType == HitResult.Type.BLOCK && !result.success() && ray instanceof BlockHitResult blockRay) {
			result = spell.castOnBlock(level, player, blockRay.getBlockPos(), blockRay);
		}
		if (!result.success()) {
			result = spell.castOnSelf(level, player);
		}
		return result;
	}
	
	private void doConsume(Player player, InteractionHand hand, ItemStack stack, Spell spell) {
		if (!player.getAbilities().instabuild && !spell.consume(player, false)) {
			consume(stack);
			player.onEquippedItemBroken(this, hand.asEquipmentSlot());
        }
    }

	protected abstract void consume(ItemStack stack);

	@Override
	public boolean canPerformAction(@NotNull ItemInstance stack, @NotNull ItemAbility itemAbility) {
		return SpellHelper.getSpell(stack) instanceof ItemAbilitySpell toolActionSpell && toolActionSpell.getItemAbilities().contains(itemAbility);
	}
}
