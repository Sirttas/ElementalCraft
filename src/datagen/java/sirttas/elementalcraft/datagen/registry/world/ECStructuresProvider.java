package sirttas.elementalcraft.datagen.registry.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import sirttas.elementalcraft.datagen.registry.AbstractECRegistryBootstrap;
import sirttas.elementalcraft.tag.ECTags;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

import java.util.Map;

public class ECStructuresProvider extends AbstractECRegistryBootstrap<Structure> {

    public ECStructuresProvider() {
        super(Registries.STRUCTURE);
    }

    @Override
    protected void gather() {
        add(SourceAltarStructure.NAME, new SourceAltarStructure(new Structure.StructureSettings(createHolderSet(ECTags.Biomes.HAS_SOURCE_ALTAR), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
    }

}
