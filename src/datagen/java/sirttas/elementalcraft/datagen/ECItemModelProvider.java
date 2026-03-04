package sirttas.elementalcraft.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.ElementPipeBlock;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.item.chisel.ChiselItem;
import sirttas.elementalcraft.item.holder.ElementHolderItem;
import sirttas.elementalcraft.item.jewel.JewelItem;
import sirttas.elementalcraft.item.pipe.PipeUpgradeItem;
import sirttas.elementalcraft.item.source.analysis.SourceAnalysisGlassItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleItem;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ECItemModelProvider extends ItemModelProvider {

	private static final String ITEM_PREFIX = "item/";
	private static final String BLOCK_PREFIX = "block/";

	public ECItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, ElementalCraftApi.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (var entry : BuiltInRegistries.ITEM.entrySet()) {
			var item = entry.getValue();
			var key = entry.getKey().identifier();

			if (ElementalCraft.owns(key) && !exists(key)) {
				String name = key.getPath();

				if (item instanceof ReceptacleItem) {
					singleTexture(name);
				} else if (item instanceof BlockItem blockItem) {
					var block = blockItem.getBlock();
					
					if (block instanceof AmethystClusterBlock) {
						singleTextureInBlock(name);
					} else if (block instanceof WallBlock) {
						wallInventory(name, ElementalCraftApi.createRL(BLOCK_PREFIX + StringUtils.removeEnd(name, "_wall")));
					} else if (block instanceof IronBarsBlock) {
						singleTexture(name, ElementalCraftApi.createRL(BLOCK_PREFIX + StringUtils.removeEnd(name, "_pane")))
								.renderType("minecraft:translucent");
					} else if (block instanceof ElementPipeBlock pipe) {
						pipeInventory(pipe, name);
					} else {
						fromBlock(name);
					}
				} else if (item instanceof PipeUpgradeItem) {
					withExistingParent(name, ElementalCraftApi.createRL(PipeUpgrade.FOLDER + name));
				}else if (item instanceof JewelItem) {
					singleJewelTexture(key.getPath());
				} else if (item instanceof FocusItem || item instanceof SourceAnalysisGlassItem || item instanceof ChiselItem) {
					singleTexture(name, Identifier.withDefaultNamespace(ITEM_PREFIX + "handheld"), "layer0", ElementalCraftApi.createRL(ITEM_PREFIX + name));
				} else if (item instanceof ElementHolderItem) {
					withExistingParent(name, ElementalCraftApi.createRL(ITEM_PREFIX + "template_element_holder"));
				} else {
					singleTexture(name);
				}
			}
		}
		fromBlock("air_mill_synthesizer_broken");
		fromBlock("air_mill_grindstone_broken");
		fromBlock("air_mill_wood_saw_broken");
	}

	private void fromBlock(String name) {
		withExistingParent(name, ElementalCraftApi.createRL(BLOCK_PREFIX + name));
	}

	private void pipeInventory(ElementPipeBlock pipe, String name) {
		withExistingParent(name, ElementalCraftApi.createRL(ITEM_PREFIX + "template_elementpipe"))
				.texture("texture", ElementalCraftApi.createRL(BLOCK_PREFIX + ECDataGenerators.getPipeTexture(pipe.getType())));
	}

	public ItemModelBuilder singleTexture(String name) {
		return singleTexture(name, ElementalCraftApi.createRL(ITEM_PREFIX + name));
	}
	
	public ItemModelBuilder singleTextureInBlock(String name) {
		return singleTexture(name, ElementalCraftApi.createRL(BLOCK_PREFIX + name));
	}

	public ItemModelBuilder singleTexture(String name, Identifier texture) {
		return singleTexture(name, Identifier.withDefaultNamespace(ITEM_PREFIX + "generated"), "layer0", texture);
	}

	public ItemModelBuilder singleJewelTexture(String name) {
		return singleTexture(name, ElementalCraftApi.createRL("elementalcraft/jewels/" + name));
	}

	public ItemModelBuilder runeTexture(String name, Identifier slate, Identifier rune) {
		return singleTexture(name, slate).texture("layer1", rune);
	}

	private boolean exists(Identifier key) {
		return existingFileHelper.exists(key, PackType.CLIENT_RESOURCES, ".json", "models/item");
	}

	private boolean exists(Jewel jewel) {
		return existingFileHelper.exists(jewel.getKey(), PackType.CLIENT_RESOURCES, ".json", "models/elementalcraft/jewels");
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft item models";
	}

	@Override
	public void clear() {
		// we don't clean it to keep the runes models
	}

	@Override
	public @NotNull CompletableFuture<?> generateAll(@NotNull CachedOutput cache) {
		return super.generateAll(cache);
	}
}
