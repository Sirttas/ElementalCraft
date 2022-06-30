package sirttas.elementalcraft.datagen.world;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.datagen.AbstractECJsonCodecProvider;
import sirttas.elementalcraft.tag.ECTags;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

import java.util.Map;

public class ECStructuresProvider extends AbstractECJsonCodecProvider<Structure> {

    public ECStructuresProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, existingFileHelper, Registry.STRUCTURE_REGISTRY);
    }

    @Override
    protected void gather() {
        add(SourceAltarStructure.NAME, new SourceAltarStructure(new Structure.StructureSettings(createHolderSet(ECTags.Biomes.HAS_SOURCE_ALTAR), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
    }

}
