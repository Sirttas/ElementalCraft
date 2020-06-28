package sirttas.elementalcraft.data;

import javax.annotation.Nonnull;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;

public class ECItemModelProvider extends ItemModelProvider {

	public ECItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, ElementalCraft.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (Item item : ForgeRegistries.ITEMS) {
			if (ElementalCraft.MODID.equals(item.getRegistryName().getNamespace()) && !exists(item)) {
				String name = item.getRegistryName().getPath();

				if (item instanceof BlockItem) {
					withExistingParent(name, new ResourceLocation(ElementalCraft.MODID, "block/" + name));
				} else {
					// FIXME (forge bug net.minecraftforge.client.model.generators.serializeLoc)
					singleTexture(name, new ResourceLocation("minecraft", "item/generated"), new ResourceLocation(ElementalCraft.MODID, "item/" + name));
				}
			}
		}
	}

	private boolean exists(Item item) {
		return existingFileHelper.exists(item.getRegistryName(), ResourcePackType.CLIENT_RESOURCES, ".json", "models/item");
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft item models";
	}
}