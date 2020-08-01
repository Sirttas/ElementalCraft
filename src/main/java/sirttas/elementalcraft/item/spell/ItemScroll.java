package sirttas.elementalcraft.item.spell;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.spell.IBlockCastedSpell;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.ISelfCastedSpell;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;

public class ItemScroll extends ItemEC {

	public static final String NAME = "scroll";

	public ItemScroll() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
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
		Spell spell = SpellHelper.getSpell(stack);

		if (spell instanceof IEntityCastedSpell && ray.getType() == RayTraceResult.Type.ENTITY) {
			result = ((IEntityCastedSpell) spell).castOnEntity(playerIn, ((EntityRayTraceResult) ray).getEntity());
		} else if (spell instanceof IBlockCastedSpell && ray.getType() == RayTraceResult.Type.BLOCK) {
			result = ((IBlockCastedSpell) spell).castOnBlock(playerIn, ((BlockRayTraceResult) ray).getPos());
		} else if (spell instanceof ISelfCastedSpell) {
			result = ((ISelfCastedSpell) spell).castOnSelf(playerIn);
		}
		return new ActionResult<>(result, stack);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell != null) {
			tooltip.add(spell.getDisplayName());
		}
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		Spell spell = SpellHelper.getSpell(stack);

		if (spell != null) {
			return new TranslationTextComponent("tooltip.elementalcraft.scroll_of", spell.getDisplayName());
		}
		return super.getDisplayName(stack);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			Spell.REGISTRY.forEach(s -> {
				ItemStack stack = new ItemStack(this);

				SpellHelper.setSpell(stack, s);
				items.add(stack);
			});
		}
	}
}
