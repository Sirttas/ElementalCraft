package sirttas.elementalcraft.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.tag.ECTags;

public class ECItemTagsProvider extends ItemTagsProvider {

	public ECItemTagsProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerTags() {
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.FENCES, ItemTags.FENCES);
		this.copy(Tags.Blocks.ORES, Tags.Items.ORES);

		getBuilder(ECTags.Items.LAVASHRINE_LIQUIFIABLES).add(Tags.Items.STONE, Tags.Items.COBBLESTONE);

		getBuilder(ECTags.Items.INFUSABLE_SWORDS).add(Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD);
		getBuilder(ECTags.Items.INFUSABLE_PICKAXES).add(Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE);
		getBuilder(ECTags.Items.INFUSABLE_AXES).add(Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE);
		getBuilder(ECTags.Items.INFUSABLE_BOWS).add(Items.BOW);
		getBuilder(ECTags.Items.INFUSABLE_CROSSBOWS).add(Items.CROSSBOW);

		getBuilder(ECTags.Items.INFUSABLE_HELMETS).add(Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET);
		getBuilder(ECTags.Items.INFUSABLE_CHESTPLATES).add(Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
		getBuilder(ECTags.Items.INFUSABLE_LEGGINGS).add(Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS);
		getBuilder(ECTags.Items.INFUSABLE_BOOTS).add(Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS);
	}
}
