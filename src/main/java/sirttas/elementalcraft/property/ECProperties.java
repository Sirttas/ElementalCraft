package sirttas.elementalcraft.property;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ECProperties {

	private ECProperties() {}
	
	public static class Blocks {

		private Blocks() {}

		public static BlockBehaviour.Properties defaultProperties() {
			return BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.strength(2)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops();
		}

		public static BlockBehaviour.Properties noOcclusion() {
			return BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.strength(2)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops()
					.noOcclusion();
		}

		public static BlockBehaviour.Properties whiterock() {
			return BlockBehaviour.Properties.of()
					.mapColor(MapColor.QUARTZ)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops()
					.strength(1.5F, 6.0F);
		}

		public static BlockBehaviour.Properties purerock() {
			return BlockBehaviour.Properties.of()
					.mapColor(MapColor.QUARTZ)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops()
					.strength(75.0F, 2400.0F);
		}

		public static BlockBehaviour.Properties container() {
			return BlockBehaviour.Properties.of()
					.strength(2)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops()
					.noOcclusion();
		}

		public static BlockBehaviour.Properties pipe() {
			return BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.strength(2)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops()
					.noOcclusion();
		}

		public static BlockBehaviour.Properties source() {
			return BlockBehaviour.Properties.of()
					.replaceable()
					.pushReaction(PushReaction.DESTROY)
					.strength(-1.0F, 3600000.0F)
					.lightLevel(s -> 7)
					.noOcclusion()
					.noLootTable();
		}
	}
}
