package sirttas.elementalcraft.datagen;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.holder.ElementHolderItem;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;

import javax.annotation.Nonnull;

public class ECItemModelProvider extends ItemModelProvider {


	public ECItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, ElementalCraftApi.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (var entry : ForgeRegistries.ITEMS.getEntries()) {
			var item = entry.getValue();
			var key = entry.getKey().location();

			if (ElementalCraftApi.MODID.equals(key.getNamespace()) && !exists(key)) {
				String name = key.getPath();

				if (item instanceof BlockItem blockItem) {
					var block = blockItem.getBlock();
					
					if (block instanceof AmethystClusterBlock) {
						singleTextureInBlock(name);
					} else {
						withExistingParent(name, ElementalCraft.createRL("block/" + name));
					}
				} else if (item instanceof ElementHolderItem) {
					withExistingParent(name, ElementalCraft.createRL("item/template_element_holder"));
				} else {
					singleTexture(name);
				}
			}
		}
		for (Jewel jewel : Jewels.REGISTRY.get()) {
			if (ElementalCraftApi.MODID.equals(jewel.getKey().getNamespace()) && !exists(jewel)) {
				singleJewelTexture(jewel.getKey().getPath());
			}
		}
	}

	public ItemModelBuilder singleTexture(String name) {
		return singleTexture(name, ElementalCraft.createRL("item/" + name));
	}
	
	public ItemModelBuilder singleTextureInBlock(String name) {
		return singleTexture(name, ElementalCraft.createRL("block/" + name));
	}

	public ItemModelBuilder singleTexture(String name, ResourceLocation texture) {
		return singleTexture(name, new ResourceLocation("minecraft", "item/generated"), "layer0", texture);
	}

	public ItemModelBuilder singleJewelTexture(String name) {
		return singleTexture("item/elementalcraft_jewels/" + name, ElementalCraft.createRL("elementalcraft/jewels/" + name));
	}

	public ItemModelBuilder runeTexture(String name, ResourceLocation slate, ResourceLocation rune) {
		return singleTexture(name, slate).texture("layer1", rune);
	}

	private boolean exists(ResourceLocation key) {
		return existingFileHelper.exists(key, PackType.CLIENT_RESOURCES, ".json", "models/item");
	}

	private boolean exists(Jewel jewel) {
		return existingFileHelper.exists(jewel.getKey(), PackType.CLIENT_RESOURCES, ".json", "models/item/elementalcraft/jewels");
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
	public void generateAll(CachedOutput cache) {
		super.generateAll(cache);
	}
}
