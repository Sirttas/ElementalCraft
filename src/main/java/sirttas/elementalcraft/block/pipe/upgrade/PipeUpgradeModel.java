package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.neoforged.neoforge.client.model.standalone.UnbakedStandaloneModel;
import org.jetbrains.annotations.NotNull;

public class PipeUpgradeModel {


    public static class Unbaked implements UnbakedStandaloneModel<@NotNull PipeUpgradeModel> {

        @Override
        public PipeUpgradeModel bake(@NotNull ModelBaker baker, @NotNull ModelDebugName name) {
            return null;
        }

        @Override
        public void resolveDependencies(@NotNull Resolver resolver) {

        }
    }

}
