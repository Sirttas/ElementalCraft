package sirttas.elementalcraft.gui;

import java.util.Optional;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.item.spell.ISpellHolder;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

@SuppressWarnings("resource")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class GuiHandler {

	private GuiHandler() {}
	
	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
			MatrixStack matrixStack = event.getMatrixStack();
			ClientPlayerEntity player = Minecraft.getInstance().player;
	
			getElementStorage(player).ifPresent(storage -> {
				if (storage instanceof IElementTypeProvider) {
					ElementType type = ((IElementTypeProvider) storage).getElementType();
					Spell spell = getSpell();
		
					doRenderElementGauge(matrixStack, storage.getElementAmount(type), storage.getElementCapacity(type), type);
					if (spell.isValid() && storage instanceof PlayerElementStorage) {
						doRenderCanCast(matrixStack, spell.consume(player, true));
					}
				}
			});
		}
	}

	private static Optional<IElementStorage> getElementStorage(PlayerEntity player) {
		Minecraft minecraft = Minecraft.getInstance();
		RayTraceResult result = minecraft.hitResult;

		if (result != null && minecraft.options.getCameraType().isFirstPerson()) {
			BlockPos pos = result.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) result).getBlockPos() : null;
			TileEntity tile = pos != null ? minecraft.player.level.getBlockEntity(pos) : null;
			if (tile != null) {
				return CapabilityElementStorage.get(tile).filter(storage -> storage.doesRenderGauge() || GuiHelper.showDebugInfo());
			}
		}
		return EntityHelper.handStream(player).map(stack -> {
			Optional<IElementStorage> opt = CapabilityElementStorage.get(stack).resolve();

			if (opt.isPresent()) {
				return opt;
			}
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return CapabilityElementStorage.get(player).resolve().map(storage -> storage.forElement(SpellHelper.getSpell(stack).getElementType()))
						.map(IElementStorage.class::cast);
			}
			return Optional.<IElementStorage>empty();
		}).filter(Optional::isPresent).map(Optional::get).findFirst();
	}

	private static Spell getSpell() {
		return EntityHelper.handStream(Minecraft.getInstance().player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack);
			}
			return Spells.NONE;
		}).filter(Spell::isValid).findFirst().orElse(Spells.NONE);
	}

	private static void doRenderElementGauge(MatrixStack matrixStack, int element, int max, sirttas.elementalcraft.api.element.ElementType type) {
		GuiHelper.renderElementGauge(matrixStack, getXoffset() - 32, getYOffset() - 8, element, max, type);
	}

	public static int getYOffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + ECConfig.CLIENT.gaugeOffsetX.get();
	}

	public static int getXoffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + ECConfig.CLIENT.gaugeOffsetY.get();
	}
	
	private static void doRenderCanCast(MatrixStack matrixStack, boolean canCast) {
		GuiHelper.renderCanCast(matrixStack, getXoffset() - 21, getYOffset() + 3, canCast);
	}
}
