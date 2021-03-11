package sirttas.elementalcraft.datagen;

import javax.annotation.Nonnull;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.elemental.ItemElementHolder;

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
					withExistingParent(name, ElementalCraft.createRL("block/" + name));
				} else if (item instanceof ItemElementHolder) {
					withExistingParent(name, ElementalCraft.createRL("item/template_element_holder"));
				} else {
					singleTexture(name);
				}
			}
		}
	}

	public ItemModelBuilder singleTexture(String name) {
		return singleTexture(name, ElementalCraft.createRL("item/" + name));
	}

	public ItemModelBuilder singleTexture(String name, ResourceLocation texture) {
		return singleTexture(name, new ResourceLocation("minecraft", "item/generated"), "layer0", texture);
	}

	public ItemModelBuilder runeTexture(String name, ResourceLocation slate, ResourceLocation rune) {
		return singleTexture(name, slate).texture("layer1", rune);
	}

	private boolean exists(Item item) {
		return existingFileHelper.exists(item.getRegistryName(), ResourcePackType.CLIENT_RESOURCES, ".json", "models/item");
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft item models";
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public void generateAll(DirectoryCache cache) {
		super.generateAll(cache);
	}
}