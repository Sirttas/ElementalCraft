package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrinePlateModelResolver;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlock;

public class ECModelTemplates {

    public static final ModelTemplate VIBRATION_SYNTHESIZER = create(VibrationSynthesizerBlock.NAME, ECTextureSlots.TENDRIL);
    public static final ModelTemplate PIPE_CORE = create("template_elementpipe_core", TextureSlot.TEXTURE);
    public static final ModelTemplate PIPE_ITEM = create("template_elementpipe", TextureSlot.TEXTURE);
    public static final ModelTemplate BUDDING_SHRINE_PLATE = create(BuddingShrinePlateModelResolver.PLATE_MODEL_FOLDER + "/budding_shrine_plate", TextureSlot.TEXTURE);

    private ECModelTemplates() {}

    public static ModelTemplate create(String id, TextureSlot... slots) {
        return ModelTemplates.create(ElementalCraftApi.createRL(id).toString(), slots);
    }
}
