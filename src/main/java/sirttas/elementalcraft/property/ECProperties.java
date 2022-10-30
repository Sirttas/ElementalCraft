package sirttas.elementalcraft.property;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import sirttas.elementalcraft.ElementalCraftTab;

public class ECProperties {

	private ECProperties() {}
	
	public static class Blocks {

		public static final BlockBehaviour.StatePredicate ALWAYS_FALSE = (a, b, c) -> false;

		public static final BlockBehaviour.Properties DEFAULT_BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL).requiresCorrectToolForDrops();
		public static final BlockBehaviour.Properties BLOCK_NOT_SOLID = BlockBehaviour.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion();
		public static final BlockBehaviour.Properties WHITEROCK = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(1.5F, 6.0F);
		public static final BlockBehaviour.Properties PUREROCK = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).requiresCorrectToolForDrops().strength(75.0F, 2400.0F);

		public static final BlockBehaviour.Properties GLASS_PANE = BlockBehaviour.Properties.of(Material.GLASS).strength(0.7F).sound(SoundType.GLASS).noOcclusion();
		public static final BlockBehaviour.Properties GLASS = BlockBehaviour.Properties.of(Material.GLASS).strength(0.7F).sound(SoundType.GLASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isRedstoneConductor(ALWAYS_FALSE).isSuffocating(ALWAYS_FALSE).isViewBlocking(ALWAYS_FALSE);

		private Blocks() {}
	}

	public static class Items {
		public static final Item.Properties DEFAULT_ITEM_PROPERTIES = new Item.Properties().tab(ElementalCraftTab.TAB);
		public static final Item.Properties ITEM_UNSTACKABLE = new Item.Properties().tab(ElementalCraftTab.TAB).stacksTo(1);
		public static final Item.Properties FIREITE = new Item.Properties().tab(ElementalCraftTab.TAB).fireResistant();
		public static final Item.Properties LENSE = new Item.Properties().tab(ElementalCraftTab.TAB).durability(1500);
		
		private Items() {}
	}
}
