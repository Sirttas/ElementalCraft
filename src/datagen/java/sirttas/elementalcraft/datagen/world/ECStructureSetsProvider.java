package sirttas.elementalcraft.datagen.world;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.datagen.AbstractECJsonCodecProvider;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

public class ECStructureSetsProvider extends AbstractECJsonCodecProvider<StructureSet> {

    public ECStructureSetsProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, existingFileHelper, Registry.STRUCTURE_SET_REGISTRY);
    }

    @Override
    protected void gather() {
        addSingle(SourceAltarStructure.NAME, new RandomSpreadStructurePlacement(64, 8, RandomSpreadType.LINEAR, 4847339));
    }

    private StructureSet addSingle(String name, StructurePlacement placement) {
        return add(name, new StructureSet(getStructureReference(name), placement));
    }

    private Holder<Structure> getStructureReference(String name) {
        return getReference(Registry.STRUCTURE_REGISTRY, name);
    }
}
