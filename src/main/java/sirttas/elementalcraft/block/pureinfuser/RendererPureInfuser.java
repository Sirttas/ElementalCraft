package sirttas.elementalcraft.block.pureinfuser;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pureinfuser.pedestal.BlockPedestal;
import sirttas.elementalcraft.block.tile.renderer.SingleItemRenderer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.event.TickHandler;

public class RendererPureInfuser extends SingleItemRenderer<TilePureInfuser> {

	@SuppressWarnings("deprecation") 
	public static final RenderType GHOST = RenderType.makeType("elementalcraft:ghost", DefaultVertexFormats.BLOCK, GL11.GL_QUADS, 256,
			RenderType.State.getBuilder().texture(new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, false)).alpha(new RenderState.AlphaState(0.5F) {
				@Override
				public void setupRenderState() {
					RenderSystem.pushMatrix();
					RenderSystem.color4f(1F, 1F, 1F, 1F);
					GlStateManager.enableBlend();
					GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.5F);
					GlStateManager.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA.param);
				}

				@Override
				public void clearRenderState() {
					GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param);
					RenderSystem.disableBlend();
					RenderSystem.popMatrix();
				}
			}).build(false));

	public RendererPureInfuser(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher, new Vector3d(0.5, 0.9, 0.5));
	}

	@Override
	public void render(TilePureInfuser te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		if (BooleanUtils.isTrue(ECConfig.CLIENT.renderPedestalShadow.get())) {
			Map<Direction, ElementType> map = getDirectionMap(te);
			List<ElementType> remaining = getRemainingElements(map);

			if (!remaining.isEmpty()) {
				map.entrySet().stream().filter(entry -> entry.getValue() == ElementType.NONE).map(Entry::getKey).forEach(direction -> {
					ElementType type = remaining.get((int) (((TickHandler.getTicksInGame() + partialTicks) / 20) % remaining.size()));
					Block pedestal = getPedestalForType(type);

					if (pedestal != null) {
						matrixStack.push();
						matrixStack.translate(direction.getXOffset() * 3, 0, direction.getZOffset() * 3);
						renderBlock(pedestal.getDefaultState(), matrixStack, buffer.getBuffer(GHOST), te.getWorld());
						matrixStack.pop();
						remaining.remove(type);
					}
				});
			}
		}
		super.render(te, partialTicks, matrixStack, buffer, light, overlay);
	}

	private List<ElementType> getRemainingElements(Map<Direction, ElementType> map) {
		List<ElementType> usedElements = map.values().stream().filter(elementType -> elementType != ElementType.NONE).collect(Collectors.toList());

		return ElementType.allValid().stream().filter(type -> !usedElements.contains(type)).collect(Collectors.toList());
	}

	private Map<Direction, ElementType> getDirectionMap(TilePureInfuser te) {
		Map<Direction, ElementType> map = new EnumMap<>(Direction.class);
		
		map.put(Direction.NORTH, getPedestal(te, Direction.NORTH));
		map.put(Direction.SOUTH, getPedestal(te, Direction.SOUTH));
		map.put(Direction.WEST, getPedestal(te, Direction.WEST));
		map.put(Direction.EAST, getPedestal(te, Direction.EAST));
		return map;
	}

	private ElementType getPedestal(TilePureInfuser te, Direction direction) {
		BlockState state = te.getWorld().getBlockState(te.getPos().offset(direction, 3));
		Block block = state.getBlock();

		return block instanceof BlockPedestal ? ((BlockPedestal) block).getElementType() : ElementType.NONE;
	}

	private Block getPedestalForType(ElementType type) {
		switch (type) {
		case WATER:
			return ECBlocks.waterPedestal;
		case FIRE:
			return ECBlocks.firePedestal;
		case EARTH:
			return ECBlocks.earthPedestal;
		case AIR:
			return ECBlocks.airPedestal;
		default:
			return null;
		}
	}
}
