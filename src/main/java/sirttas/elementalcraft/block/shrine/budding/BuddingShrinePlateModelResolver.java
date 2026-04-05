package sirttas.elementalcraft.block.shrine.budding;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.client.model.ECModelResolver;

public class BuddingShrinePlateModelResolver extends ECModelResolver<BuddingShrinePlateModel> {

    public static final String PLATE_MODEL_FOLDER = "elementalcraft/budding_shrine_plates/";
    public static final Identifier IDENTIFIER = ElementalCraftApi.BUD_TYPE_MANAGER_KEY.identifier();
    private static final FileToIdConverter LISTER = FileToIdConverter.json(PLATE_MODEL_FOLDER);

    public BuddingShrinePlateModelResolver(ModelManager modelManager) {
        super(modelManager, LISTER, BuddingShrinePlateModel.Unbaked.CODEC);
    }

    public BuddingShrinePlateModel getModel(Holder<@NotNull BuddingShrineBudType> holder) {
        return getModel(holder.getKey().identifier());
    }

}
