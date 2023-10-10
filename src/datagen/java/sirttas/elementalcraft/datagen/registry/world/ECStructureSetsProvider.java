package sirttas.elementalcraft.datagen.registry.world;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import sirttas.elementalcraft.datagen.registry.AbstractECRegistryBootstrap;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

public class ECStructureSetsProvider extends AbstractECRegistryBootstrap<StructureSet> {

    public ECStructureSetsProvider() {
        super(Registries.STRUCTURE_SET);
    }

    @Override
    protected void gather() {
        addSingle(SourceAltarStructure.NAME, new RandomSpreadStructurePlacement(20, 8, RandomSpreadType.LINEAR, 4847339));
    }

    private Holder.Reference<StructureSet> addSingle(String name, StructurePlacement placement) {
        return add(name, new StructureSet(getStructureReference(name), placement));
    }

    private Holder<Structure> getStructureReference(String name) {
        return getReference(Registries.STRUCTURE, name);
    }
}
