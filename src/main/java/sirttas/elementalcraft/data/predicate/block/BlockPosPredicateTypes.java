package sirttas.elementalcraft.data.predicate.block;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.data.predicate.block.rune.HasRunePredicate;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockPosPredicateTypes {

	private BlockPosPredicateTypes() {}

	@SubscribeEvent
	public static void registerBlockPosPredicateSerializers(RegistryEvent.Register<BlockPosPredicateType<?>> event) {
		IForgeRegistry<BlockPosPredicateType<?>> registry = event.getRegistry();

		RegistryHelper.register(registry, new BlockPosPredicateType<>(HasShrineUpgradePredicate.CODEC), HasShrineUpgradePredicate.NAME);
		RegistryHelper.register(registry, new BlockPosPredicateType<>(HasRunePredicate.CODEC), HasRunePredicate.NAME);
		RegistryHelper.register(registry, new BlockPosPredicateType<>(RangeFromSpawnPredicate.CODEC), RangeFromSpawnPredicate.NAME);
	}

}
