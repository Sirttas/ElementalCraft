package sirttas.elementalcraft.interaction.botania;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.renderer.ECRenderers;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlock;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerRenderer;

public class BotaniaInteractions {

    private BotaniaInteractions() {}

    public static void registerBlockEntities(IForgeRegistry<BlockEntityType<?>> r) {
        ECBlocks.register(r, BlockEntityType.Builder.of(ManaSynthesizerBlockEntity::new, ECBlocks.MANA_SYNTHESIZER), ManaSynthesizerBlock.NAME);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerModels() {
        ECRenderers.register(ManaSynthesizerBlockEntity.TYPE, ManaSynthesizerRenderer::new);
    }
}
