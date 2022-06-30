package sirttas.elementalcraft.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.spell.ISpellHolder;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("resource")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class GuiHandler {

	private GuiHandler() {}
	
	@SubscribeEvent
	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.LAYER) {
			PoseStack matrixStack = event.getPoseStack();
			LocalPlayer player = Minecraft.getInstance().player;
			Spell spell = getSpell();
			int i = 0;
	
			for (var storage : getElementStorage(player)) {
				ElementType type = storage.getElementType();

				doRenderElementGauge(matrixStack, storage.getElementAmount(), storage.getElementCapacity(), type, i);
				if (spell.isValid() && spell.getElementType() == type && i == 0) {
					doRenderCanCast(matrixStack, spell.consume(player, true));
				}
				i++;
			}
		}
	}

	private static List<ISingleElementStorage> getElementStorage(Player player) {
		Minecraft minecraft = Minecraft.getInstance();
		HitResult result = minecraft.hitResult;

		if (result != null && minecraft.options.getCameraType().isFirstPerson()) {
			BlockPos pos = result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos() : null;
			BlockEntity tile = pos != null ? minecraft.player.level.getBlockEntity(pos) : null;

			if (tile != null) {
				var storages = CapabilityElementStorage.get(tile)
						.filter(storage -> storage.doesRenderGauge() || GuiHelper.showDebugInfo())
						.map(GuiHandler::splitStorage)
						.orElse(Collections.emptyList());

				if (!storages.isEmpty()) {
					return storages;
				}
			}
		}

		var holder = EntityHelper.handStream(player)
				.map(stack -> CapabilityElementStorage.get(stack).resolve())
				.<IElementStorage>mapMulti(Optional::ifPresent)
				.findFirst();

		if (holder.isPresent()) {
			return splitStorage(holder.get());
		}

		var playerStorage = CapabilityElementStorage.get(player).resolve();

		if (playerStorage.isEmpty()) {
			return Collections.emptyList();
		}

		var spellElementType = EntityHelper.handStream(player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack).getElementType();
			}
			return ElementType.NONE;
		}).filter(type -> type != ElementType.NONE).findFirst().orElse(ElementType.NONE);
		var list = new ArrayList<ElementType>(4);

		if (spellElementType != ElementType.NONE) {
			list.add(spellElementType);
		}
		player.getCapability(IJewelHandler.JEWEL_HANDLER_CAPABILITY).ifPresent(handler -> handler.getActiveJewels().stream()
				.map(Jewel::getElementType)
				.distinct()
				.filter(type -> type != ElementType.NONE && type != spellElementType)
				.forEach(list::add));
		return splitStorage(playerStorage.get(), list);
	}

	private static List<ISingleElementStorage> splitStorage(IElementStorage storage) {
		if (storage instanceof ISingleElementStorage singleElementStorage) {
			return Collections.singletonList(singleElementStorage);
		}
		return splitStorage(storage, ElementType.ALL_VALID);
	}
	private static List<ISingleElementStorage> splitStorage(IElementStorage storage, List<ElementType> elementTypes) {
		if (storage instanceof ISingleElementStorage singleElementStorage) {
			return elementTypes.contains(singleElementStorage.getElementType()) ? List.of(singleElementStorage) : Collections.emptyList();
		}
		return elementTypes.stream()
				.<ISingleElementStorage>mapMulti((type, downstream) -> downstream.accept(storage.forElement(type)))
				.filter(s -> s.getElementCapacity() > 0)
				.toList();
	}

	private static Spell getSpell() {
		return EntityHelper.handStream(Minecraft.getInstance().player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack);
			}
			return Spells.NONE.get();
		}).filter(Spell::isValid).findFirst().orElseGet(Spells.NONE);
	}

	private static void doRenderElementGauge(PoseStack matrixStack, int element, int max, ElementType type, int index) {
		GuiHelper.renderElementGauge(matrixStack, getXOffset() - 32 - (20 * index), getYOffset() - 8, element, max, type);
	}

	public static int getYOffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + ECConfig.CLIENT.gaugeOffsetX.get();
	}

	public static int getXOffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + ECConfig.CLIENT.gaugeOffsetY.get();
	}
	
	private static void doRenderCanCast(PoseStack matrixStack, boolean canCast) {
		GuiHelper.renderCanCast(matrixStack, getXOffset() - 21, getYOffset() + 3, canCast);
	}
}
