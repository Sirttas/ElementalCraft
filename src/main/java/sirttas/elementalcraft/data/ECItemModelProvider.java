package sirttas.elementalcraft.data;

import javax.annotation.Nonnull;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
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
					ResourceLocation parent = new ResourceLocation(ElementalCraft.MODID, "block/" + name);

					if (this.exists(parent)) {
						withExistingParent(name, parent);
					} else {
						getBuilder(name).parent(new UncheckedModelFile(parent));
					}
				} else {
					singleTexture(name, new ResourceLocation("minecraft", "item/generated"), "layer0", new ResourceLocation(ElementalCraft.MODID, "item/" + name));
				}
			}
		}
	}

	private boolean exists(Item item) {
		return existingFileHelper.exists(item.getRegistryName(), ResourcePackType.CLIENT_RESOURCES, ".json", "models/item");
	}

	private boolean exists(ResourceLocation model) {
		return existingFileHelper.exists(model, ResourcePackType.CLIENT_RESOURCES, ".json", "models");
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft item models";
	}
}