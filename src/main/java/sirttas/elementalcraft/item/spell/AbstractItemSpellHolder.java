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
import sirttas.elementalcraft.spell.IBlockCastedSpell;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.ISelfCastedSpell;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

public abstract class AbstractItemSpellHolder extends ItemEC implements ISpellHolder {

	public AbstractItemSpellHolder(Properties properties) {
		super(properties);
	}

	protected void addAttributeTooltip(List<ITextComponent> tooltip, Spell spell) {
		tooltip.add(new StringTextComponent(""));
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.spell_consume", spell.getElementType().getDisplayName()).applyTextStyle(TextFormatting.YELLOW));
		spell.addInformation(tooltip);
		addAttributeMultimapToTooltip(tooltip, spell.getOnUseAttributeModifiers(), new TranslationTextComponent("tooltip.elementalcraft.on_spell_use").applyTextStyle(TextFormatting.GRAY));
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ActionResultType result = ActionResultType.PASS;
		ItemStack stack = playerIn.getHeldItem(handIn);
		Spell spell = SpellHelper.getSpell(stack);
		Multimap<String, AttributeModifier> attributes = spell.getOnUseAttributeModifiers();

		playerIn.getAttributes().applyAttributeModifiers(attributes);

		final RayTraceResult ray = EntityHelper.rayTrace(playerIn, playerIn.getAttribute(PlayerEntity.REACH_DISTANCE).getValue());

		if (spell instanceof IEntityCastedSpell && ray.getType() == RayTraceResult.Type.ENTITY) {
			result = ((IEntityCastedSpell) spell).castOnEntity(playerIn, ((EntityRayTraceResult) ray).getEntity());
		} else if (spell instanceof IBlockCastedSpell && ray.getType() == RayTraceResult.Type.BLOCK) {
			result = ((IBlockCastedSpell) spell).castOnBlock(playerIn, ((BlockRayTraceResult) ray).getPos());
		} else if (spell instanceof ISelfCastedSpell) {
			result = ((ISelfCastedSpell) spell).castOnSelf(playerIn);
		}
		if (!result.equals(ActionResultType.PASS) && !playerIn.isCreative()) {
			if (!spell.consume(playerIn)) {
				consume(stack);
				DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> playerIn.renderBrokenItemStack(stack));
			}
			playerIn.getCooldownTracker().setCooldown(this, spell.getCooldown());
			ECTags.Items.SPELL_HOLDERS.getAllElements().forEach(i -> playerIn.getCooldownTracker().setCooldown(i, spell.getCooldown()));
		}
		playerIn.getAttributes().removeAttributeModifiers(attributes);
		return new ActionResult<>(result, stack);
	}

	protected abstract void consume(ItemStack stack);

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		return SpellHelper.getSpell(stack).getAttributeModifiers(equipmentSlot);
	}
}