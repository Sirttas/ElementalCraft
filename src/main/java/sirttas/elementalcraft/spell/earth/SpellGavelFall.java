package sirttas.elementalcraft.spell.earth;

import com.google.common.collect.Multimap;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.Spell;

public class SpellGavelFall extends Spell {

	public static final String NAME = "gravelfall";

	public SpellGavelFall() {
		super(Properties.create(Spell.Type.COMBAT).elementType(ElementType.EARTH).cooldown(ECConfig.COMMON.gravelFallCooldown.get()).consumeAmount(ECConfig.COMMON.gravelFallConsumeAmount.get()));
	}

	private void spawn(World world, BlockPos pos) {
		FallingBlockEntity entity = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, Blocks.GRAVEL.getDefaultState());

		entity.fallTime = 1;
		entity.setHurtEntities(true);
		world.addEntity(entity);
	}

	private void checkAndSpawn(World world, BlockPos pos) {
		if (world.isAirBlock(pos)) {
			spawn(world, pos);
		}
	}

	private ActionResultType spawnGravel(Entity sender, BlockPos pos) {
		World world = sender.getEntityWorld();

		checkAndSpawn(world, pos.up(4));
		checkAndSpawn(world, pos.up(5));
		checkAndSpawn(world, pos.up(6));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		return spawnGravel(sender, target);
	}

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		return spawnGravel(sender, new BlockPos(target.getPositionVec()));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(REACH_DISTANCE_MODIFIER, "Reach distance modifier", 5.0D, AttributeModifier.Operation.ADDITION));
		}
		return multimap;
	}

}
