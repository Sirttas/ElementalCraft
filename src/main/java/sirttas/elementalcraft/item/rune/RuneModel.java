package sirttas.elementalcraft.item.rune;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.BakedModelWrapper;
import sirttas.elementalcraft.rune.Rune;

public class RuneModel extends BakedModelWrapper<IBakedModel> {

	private static final ItemOverrideList OVERRIDE_LIST = new ItemOverrideList() {
		@Override
		public IBakedModel getOverrideModel(@Nonnull IBakedModel original, @Nonnull ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity) {
			Rune rune = ItemRune.getRune(stack);

			if (rune != null) {
				return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(rune.getModelName(), "inventory"));
			}
			return original;
		}
	};

	public RuneModel(IBakedModel original) {
		super(original);
	}

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return OVERRIDE_LIST;
	}
}