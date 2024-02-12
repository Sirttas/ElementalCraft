package sirttas.elementalcraft.property;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ECProperties {

	private ECProperties() {}
	
	public static class Blocks {

		public static final BlockBehaviour.Properties DEFAULT_BLOCK_PROPERTIES = BlockBehaviour.Properties.of()
				.mapColor(MapColor.METAL)
				.strength(2)
				.sound(SoundType.METAL)
				.requiresCorrectToolForDrops();
		public static final BlockBehaviour.Properties DEFAULT_NO_OCCLUSION = BlockBehaviour.Properties.of()
				.mapColor(MapColor.METAL)
				.strength(2)
				.sound(SoundType.METAL)
				.requiresCorrectToolForDrops()
				.noOcclusion();
		public static final BlockBehaviour.Properties WHITEROCK = BlockBehaviour.Properties.of()
				.mapColor(MapColor.QUARTZ)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties PUREROCK = BlockBehaviour.Properties.of()
				.mapColor(MapColor.QUARTZ)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.strength(75.0F, 2400.0F);

		public static final BlockBehaviour.Properties CONTAINER = BlockBehaviour.Properties.of()
				.strength(2)
				.sound(SoundType.METAL)
				.requiresCorrectToolForDrops()
				.noOcclusion();

		public static final BlockBehaviour.Properties PIPE = BlockBehaviour.Properties.of()
				.mapColor(MapColor.METAL)
				.strength(2)
				.sound(SoundType.METAL)
				.requiresCorrectToolForDrops()
				.noOcclusion()
				.randomTicks();

		public static final BlockBehaviour.Properties SOURCE = BlockBehaviour.Properties.of()
				.replaceable()
				.pushReaction(PushReaction.DESTROY)
				.strength(-1.0F, 3600000.0F)
				.lightLevel(s -> 7)
				.noOcclusion()
				.noLootTable();

		private Blocks() {}
	}

	public static class Items {
		public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties();
		public static final Item.Properties ITEM_UNSTACKABLE = new Item.Properties().stacksTo(1);
		public static final Item.Properties FIREITE = new Item.Properties().fireResistant();
		
		private Items() {}
	}
}
