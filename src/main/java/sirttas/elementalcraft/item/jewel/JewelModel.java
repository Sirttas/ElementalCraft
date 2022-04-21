package sirttas.elementalcraft.item.jewel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.BakedModelWrapper;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.JewelHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JewelModel extends BakedModelWrapper<BakedModel> {

	private static final ItemOverrides OVERRIDE_LIST = new ItemOverrides() {
		@Override
		public BakedModel resolve(@Nonnull BakedModel original, @Nonnull ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int i) {
			Jewel jewel = JewelHelper.getJewel(stack);

			if (jewel != null) {
				return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(jewel.getModelName(), "inventory"));
			}
			return original;
		}
	};

	public JewelModel(BakedModel original) {
		super(original);
	}

	@Nonnull
	@Override
	public ItemOverrides getOverrides() {
		return OVERRIDE_LIST;
	}
}
