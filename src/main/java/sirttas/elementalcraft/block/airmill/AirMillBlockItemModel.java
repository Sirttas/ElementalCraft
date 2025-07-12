package sirttas.elementalcraft.block.airmill;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import sirttas.elementalcraft.component.ECDataComponents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AirMillBlockItemModel extends BakedModelWrapper<BakedModel> {

    private final ItemOverrides overrides;

    public AirMillBlockItemModel(ModelResourceLocation originalName, BakedModel original) {
        super(original);
        ModelResourceLocation brokenModel = ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(originalName.id().getNamespace(), "item/" + originalName.id().getPath() + "_broken"));
        overrides = new ItemOverrides() {
            @Override
            public BakedModel resolve(@Nonnull BakedModel original, @Nonnull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i) {
                var damage = stack.getComponents().getOrDefault(ECDataComponents.AIR_MILL_DAMAGE.get(), 0);

                if (damage < AirMill.getMaxDamage()) {
                    return original;
                }
                return Minecraft.getInstance().getModelManager().getModel(brokenModel);
            }
        };
    }

    @Nonnull
    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }
}
