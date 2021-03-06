package sirttas.elementalcraft.block.pureinfuser;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.renderer.ECRenderTypes;
import sirttas.elementalcraft.block.entity.renderer.SingleItemRenderer;
import sirttas.elementalcraft.block.pureinfuser.pedestal.PedestalBlock;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.event.TickHandler;

public class PureInfuserRenderer extends SingleItemRenderer<PureInfuserBlockEntity> {

	public PureInfuserRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher, new Vector3d(0.5, 0.9, 0.5));
	}

	@Override
	public void render(PureInfuserBlockEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		if (BooleanUtils.isTrue(ECConfig.CLIENT.renderPedestalShadow.get())) {
			Map<Direction, ElementType> map = getDirectionMap(te);
			List<ElementType> remaining = getRemainingElements(map);

			if (!remaining.isEmpty()) {
				map.entrySet().stream().filter(entry -> entry.getValue() == ElementType.NONE).map(Entry::getKey).forEach(direction -> {
					ElementType type = remaining.get((int) (((TickHandler.getTicksInGame() + partialTicks) / 20) % remaining.size()));
					Block pedestal = getPedestalForType(type);

					if (pedestal != null) {
						matrixStack.pushPose();
						matrixStack.translate(direction.getStepX() * 3D, 0, direction.getStepZ() * 3D);
						renderBlock(pedestal.defaultBlockState(), matrixStack, buffer.getBuffer(ECRenderTypes.GHOST), te.getLevel(), te.getBlockPos().relative(direction, 3));
						matrixStack.popPose();
						remaining.remove(type);
					}
				});
			}
		}
		super.render(te, partialTicks, matrixStack, buffer, light, overlay);
	}

	private List<ElementType> getRemainingElements(Map<Direction, ElementType> map) {
		List<ElementType> usedElements = map.values().stream().filter(elementType -> elementType != ElementType.NONE).collect(Collectors.toList());

		return ElementType.ALL_VALID.stream().filter(type -> !usedElements.contains(type)).collect(Collectors.toList());
	}

	private Map<Direction, ElementType> getDirectionMap(PureInfuserBlockEntity te) {
		Map<Direction, ElementType> map = new EnumMap<>(Direction.class);
		
		map.put(Direction.NORTH, getPedestal(te, Direction.NORTH));
		map.put(Direction.SOUTH, getPedestal(te, Direction.SOUTH));
		map.put(Direction.WEST, getPedestal(te, Direction.WEST));
		map.put(Direction.EAST, getPedestal(te, Direction.EAST));
		return map;
	}

	private ElementType getPedestal(PureInfuserBlockEntity te, Direction direction) {
		BlockState state = te.getLevel().getBlockState(te.getBlockPos().relative(direction, 3));
		Block block = state.getBlock();

		return block instanceof PedestalBlock ? ((PedestalBlock) block).getElementType() : ElementType.NONE;
	}

	private Block getPedestalForType(ElementType type) {
		switch (type) {
		case WATER:
			return ECBlocks.WATER_PEDESTAL;
		case FIRE:
			return ECBlocks.FIRE_PEDESTAL;
		case EARTH:
			return ECBlocks.EARTH_PEDESTAL;
		case AIR:
			return ECBlocks.AIR_PEDESTAL;
		default:
			return null;
		}
	}
}
