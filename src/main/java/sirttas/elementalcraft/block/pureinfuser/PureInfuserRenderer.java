package sirttas.elementalcraft.block.pureinfuser;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.renderer.SingleItemBlockEntityRenderer;
import sirttas.elementalcraft.client.renderer.state.GhostBlockRenderState;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.event.TickHandler;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class PureInfuserRenderer extends SingleItemBlockEntityRenderer<PureInfuserBlockEntity, PureInfuserRenderState> {

    private final BlockModelResolver blockModelResolver;

	public PureInfuserRenderer(BlockEntityRendererProvider.Context context) {
		super(context, new Vec3(0.5, 0.9, 0.5));
        blockModelResolver = context.blockModelResolver();
	}

    @Override
    public @NotNull PureInfuserRenderState createRenderState() {
        return new PureInfuserRenderState();
    }

    @Override
    public void extractRenderState(PureInfuserBlockEntity blockEntity, PureInfuserRenderState renderState, float partialTick, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        if (BooleanUtils.isTrue(ECConfig.CLIENT.renderPedestalShadow.get()) && !blockEntity.isRunning()) {
            Map<Direction, ElementType> map = getDirectionMap(blockEntity);
            List<ElementType> remaining = getRemainingElements(map);

            if (!remaining.isEmpty()) {
                map.entrySet().stream().filter(entry -> entry.getValue() == ElementType.NONE).map(Entry::getKey).forEach(direction -> {
                    ElementType type = remaining.get((int) (((TickHandler.getTicksInGame() + partialTick) / 20) % remaining.size()));
                    Block pedestal = getPedestalForType(type);

                    if (pedestal != null) {
                        var ghostState = new GhostBlockRenderState();

                        ghostState.update(blockModelResolver, pedestal.defaultBlockState(), direction.step().mul(3));
                        renderState.ghostPedestals.add(ghostState);
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

    @Override
    public void submit(PureInfuserRenderState renderState, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector nodeCollector, @NotNull CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        renderState.ghostPedestals.forEach(ghostState -> ghostState.submit(poseStack, nodeCollector, renderState.lightCoords));
    }

}
