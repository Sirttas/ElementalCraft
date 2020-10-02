package sirttas.elementalcraft.item.spell;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.SpellTickManager;

public abstract class AbstractItemSpellHolder extends ItemEC implements ISpellHolder {

	public AbstractItemSpellHolder(Properties properties) {
		super(properties);
	}

	protected void addAttributeTooltip(List<ITextComponent> tooltip, Spell spell) {
		tooltip.add(new StringTextComponent(""));
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.consumes", spell.getElementType().getDisplayName()).applyTextStyle(TextFormatting.YELLOW));
		spell.addInformation(tooltip);
		addAttributeMultimapToTooltip(tooltip, spell.getOnUseAttributeModifiers(), new TranslationTextComponent("tooltip.elementalcraft.on_spell_use").applyTextStyle(TextFormatting.GRAY));
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		Spell spell = SpellHelper.getSpell(stack);
		Multimap<String, AttributeModifier> attributes = spell.getOnUseAttributeModifiers();

		playerIn.getAttributes().applyAttributeModifiers(attributes);
		
		ActionResultType result = castSpell(playerIn, spell, EntityHelper.rayTrace(playerIn, playerIn.getAttribute(PlayerEntity.REACH_DISTANCE).getValue()));

		if (result.isSuccessOrConsume() && !playerIn.isCreative()) {
			if (!spell.consume(playerIn)) {
				consume(stack);
				DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> playerIn.renderBrokenItemStack(stack));
			}
			if (result.isSuccess()) {
				SpellTickManager.getInstance(worldIn).setCooldown(playerIn, spell);
			}
		}
		playerIn.getAttributes().removeAttributeModifiers(attributes);
		return new ActionResult<>(result, stack);
	}

	private ActionResultType castSpell(PlayerEntity playerIn, Spell spell, final RayTraceResult ray) {
		ActionResultType result = ActionResultType.PASS;

		if (SpellTickManager.getInstance(playerIn.world).hasCooldown(playerIn, spell)) {
			return result;
		}
		
		if (ray.getType() == RayTraceResult.Type.ENTITY) {
			result = spell.castOnEntity(playerIn, ((EntityRayTraceResult) ray).getEntity());
		}
		if (ray.getType() == RayTraceResult.Type.BLOCK && !result.isSuccessOrConsume()) {
			result = spell.castOnBlock(playerIn, ((BlockRayTraceResult) ray).getPos());
		}
		if (!result.isSuccessOrConsume()) {
			result = spell.castOnSelf(playerIn);
		}
		return result;
	}

	protected abstract void consume(ItemStack stack);

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		return SpellHelper.getSpell(stack).getAttributeModifiers(equipmentSlot);
	}
}