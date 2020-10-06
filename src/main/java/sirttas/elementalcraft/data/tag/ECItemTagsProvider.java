package sirttas.elementalcraft.data.tag;

import java.util.Comparator;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.spell.AbstractItemSpellHolder;
import sirttas.elementalcraft.tag.ECTags;

public class ECItemTagsProvider extends ItemTagsProvider {

	public ECItemTagsProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	private Item[] getItemsForClass(Class<?> clazz) {
		return registry.stream().filter(i -> ElementalCraft.MODID.equals(i.getRegistryName().getNamespace()) && clazz.isInstance(i)).sorted(Comparator.comparing(Item::getRegistryName))
				.toArray(Item[]::new);
	}

	@Override
	protected void registerTags() {
		this.copy(BlockTags.SLABS, ItemTags.SLABS);
		this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
		this.copy(BlockTags.WALLS, ItemTags.WALLS);
		this.copy(BlockTags.FENCES, ItemTags.FENCES);
		this.copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
		this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
		this.copy(ECTags.Blocks.PUREROCKS, ECTags.Items.PUREROCKS);
		this.copy(ECTags.Blocks.PIPES, ECTags.Items.PIPES);
		this.copy(ECTags.Blocks.SHRINES, ECTags.Items.SHRINES);

		getBuilder(ECTags.Items.INFUSABLE_SWORDS).add(Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD);
		getBuilder(ECTags.Items.INFUSABLE_PICKAXES).add(Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE);
		getBuilder(ECTags.Items.INFUSABLE_AXES).add(Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE);
		getBuilder(ECTags.Items.INFUSABLE_BOWS).add(Items.BOW);
		getBuilder(ECTags.Items.INFUSABLE_CROSSBOWS).add(Items.CROSSBOW);

		getBuilder(ECTags.Items.INFUSABLE_HELMETS).add(Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET);
		getBuilder(ECTags.Items.INFUSABLE_CHESTPLATES).add(Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
		getBuilder(ECTags.Items.INFUSABLE_LEGGINGS).add(Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS);
		getBuilder(ECTags.Items.INFUSABLE_BOOTS).add(Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS);

		getBuilder(ECTags.Items.SPELL_HOLDERS).add(getItemsForClass(AbstractItemSpellHolder.class));
	}
}
