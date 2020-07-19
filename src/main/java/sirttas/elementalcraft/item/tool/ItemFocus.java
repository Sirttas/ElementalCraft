package sirttas.elementalcraft.item.tool;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.material.ECMaterials;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.spell.IBlockCastedSpell;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.ISelfCastedSpell;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;

public class ItemFocus extends ItemECTool {

	public static final String NAME = "focus";

	public ItemFocus() {
		super(ECProperties.Items.ITEM_UNSTACKABLE, ECMaterials.TOOL_PURE);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@SuppressWarnings("resource")
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		final RayTraceResult ray = Minecraft.getInstance().objectMouseOver;
		ActionResultType result = ActionResultType.PASS;
		ItemStack stack = playerIn.getHeldItem(handIn);
		setSpell(stack, Spells.gravelFall); // TODO remove
		Spell spell = getSpell(stack);

		if (spell instanceof IEntityCastedSpell && ray.getType() == RayTraceResult.Type.ENTITY) {
			result = ((IEntityCastedSpell) spell).castOnEntity(playerIn, ((EntityRayTraceResult) ray).getEntity());
		} else if (spell instanceof IBlockCastedSpell && ray.getType() == RayTraceResult.Type.BLOCK) {
			result = ((IBlockCastedSpell) spell).castOnBlock(playerIn, ((BlockRayTraceResult) ray).getPos());
		} else if (spell instanceof ISelfCastedSpell) {
			result = ((ISelfCastedSpell) spell).castOnSelf(playerIn);
		}
		return new ActionResult<>(result, stack);
	}

	public Spell getSpell(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		if (nbt.contains(ECNBTTags.SPELL)) {
			return Spell.REGISTRY.getValue(new ResourceLocation(nbt.getString(ECNBTTags.SPELL)));
		}
		return null;
	}

	public void setSpell(ItemStack stack, Spell spell) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		nbt.putString(ECNBTTags.SPELL, spell.getRegistryName().toString());
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		Spell spell = getSpell(stack);

		if (spell != null) {
			tooltip.add(spell.getDisplayName());
		}

	}

}
