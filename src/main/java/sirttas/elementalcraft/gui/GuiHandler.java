package sirttas.elementalcraft.gui;

import java.util.Optional;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.capability.CapabilityElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.holder.ItemElementHolder;
import sirttas.elementalcraft.item.spell.ISpellHolder;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

@SuppressWarnings("resource")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class GuiHandler {

	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();
		RayTraceResult result = minecraft.objectMouseOver;

		if (event.getType() == ElementType.ALL) {
			if (result != null && minecraft.gameSettings.getPointOfView().func_243192_a()) {
				BlockPos pos = result.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) result).getPos() : null;
				TileEntity tile = pos != null ? minecraft.player.world.getTileEntity(pos) : null;
				if (tile != null) {
					Optional<IElementStorage> opt = CapabilityElementStorage.get(tile).filter(storage -> storage.doesRenderGauge() || GuiHelper.showDebugInfo());

					if (opt.isPresent()) {
						IElementStorage storage = opt.get();

						doRenderElementGauge(event.getMatrixStack(), storage.getElementAmount(), storage.getElementCapacity(), storage.getElementType());
						return;
					}
				}
			}
			ItemStack stack = getElementHolder();

			if (!stack.isEmpty() && stack.getItem() instanceof ItemElementHolder) {
				ItemElementHolder holder = (ItemElementHolder) stack.getItem();
				int amount = holder.getElementAmount(stack);
				Spell spell = getSpell();

				doRenderElementGauge(event.getMatrixStack(), amount, holder.getElementCapacity(), holder.getElementType());
				if (spell.isValid()) {
					doRenderCanCast(event.getMatrixStack(), amount >= spell.getConsumeAmount());
				}
			}
		}
	}

	private static ItemStack getElementHolder() {
		ClientPlayerEntity player = Minecraft.getInstance().player;

		return EntityHelper.handStream(player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ItemElementHolder) {
				return stack;
			} else if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return ItemElementHolder.find(player, SpellHelper.getSpell(stack).getElementType());
			}
			return ItemStack.EMPTY;
		}).filter(s -> !s.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
	}

	private static Spell getSpell() {
		return EntityHelper.handStream(Minecraft.getInstance().player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack);
			}
			return Spells.none;
		}).filter(Spell::isValid).findFirst().orElse(Spells.none);
	}

	private static void doRenderElementGauge(MatrixStack matrixStack, int element, int max, sirttas.elementalcraft.api.element.ElementType type) {
		GuiHelper.renderElementGauge(matrixStack, getXoffset() - 32, getYOffset() - 8, element, max,
				type);
	}

	public static int getYOffset() {
		return Minecraft.getInstance().getMainWindow().getScaledHeight() / 2 + ECConfig.CLIENT.gaugeOffsetX.get();
	}

	public static int getXoffset() {
		return Minecraft.getInstance().getMainWindow().getScaledWidth() / 2 + ECConfig.CLIENT.gaugeOffsetY.get();
	}
	
	private static void doRenderCanCast(MatrixStack matrixStack, boolean canCast) {
		GuiHelper.renderCanCast(matrixStack, getXoffset() - 21, getYOffset() + 3, canCast);
	}
}
