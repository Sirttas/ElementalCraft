package sirttas.elementalcraft.block.pureinfuser;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.BooleanUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.renderer.SingleItemRenderer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.event.TickHandler;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class PureInfuserRenderer extends SingleItemRenderer<PureInfuserBlockEntity> {

	public PureInfuserRenderer() {
		super(new Vec3(0.5, 0.9, 0.5));
	}

	@Override
	public void render(@Nonnull PureInfuserBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		renderPedestalShadow(te, partialTicks, matrixStack, buffer);
		super.render(te, partialTicks, matrixStack, buffer, light, overlay);
	}

	private void renderPedestalShadow(@Nonnull PureInfuserBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer) {
		if (BooleanUtils.isTrue(ECConfig.CLIENT.renderPedestalShadow.get()) && !te.isRunning()) {
			Map<Direction, ElementType> map = getDirectionMap(te);
			List<ElementType> remaining = getRemainingElements(map);

			if (!remaining.isEmpty()) {
				map.entrySet().stream().filter(entry -> entry.getValue() == ElementType.NONE).map(Entry::getKey).forEach(direction -> {
					ElementType type = remaining.get((int) (((TickHandler.getTicksInGame() + partialTicks) / 20) % remaining.size()));
					Block pedestal = getPedestalForType(type);

					if (pedestal != null) {
						matrixStack.pushPose();
						matrixStack.translate(direction.getStepX() * 3D, 0, direction.getStepZ() * 3D);
						ECRendererHelper.renderGhost(pedestal.defaultBlockState(), matrixStack, buffer, te.getLevel(), te.getBlockPos().relative(direction, 3));
						matrixStack.popPose();
						remaining.remove(type);
					}
				});
			}
		}
	}

	private List<ElementType> getRemainingElements(Map<Direction, ElementType> map) {
		List<ElementType> usedElements = map.values().stream().filter(elementType -> elementType != ElementType.NONE).toList();

		return ElementType.ALL_VALID.stream().filter(type -> !usedElements.contains(type)).collect(Collectors.toList());
	}

	private Map<Direction, ElementType> getDirectionMap(PureInfuserBlockEntity te) {
		Map<Direction, ElementType> map = new EnumMap<>(Direction.class);
		
		map.put(Direction.NORTH, te.getPedestalElementType(Direction.NORTH));
		map.put(Direction.SOUTH, te.getPedestalElementType(Direction.SOUTH));
		map.put(Direction.WEST, te.getPedestalElementType(Direction.WEST));
		map.put(Direction.EAST, te.getPedestalElementType(Direction.EAST));
		return map;
	}

	private Block getPedestalForType(ElementType type) {
		return switch (type) {
			case WATER -> ECBlocks.WATER_PEDESTAL.get();
			case FIRE -> ECBlocks.FIRE_PEDESTAL.get();
			case EARTH -> ECBlocks.EARTH_PEDESTAL.get();
			case AIR -> ECBlocks.AIR_PEDESTAL.get();
			default -> null;
		};
	}
}
