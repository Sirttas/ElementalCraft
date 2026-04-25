package sirttas.elementalcraft.datagen.interaction;

import sirttas.elementalcraft.datagen.definition.BudTypeDataDefinition;

import java.util.List;

public interface DatagenInteraction {

    default List<BudTypeDataDefinition>  getBudTypeDataDefinitions() {
        return List.of();
    }
}
