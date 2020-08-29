package sirttas.elementalcraft.spell.earth;

import com.google.common.collect.Multimap;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.spell.IBlockCastedSpell;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.Spell;

public class SpellGavelFall extends Spell implements IEntityCastedSpell, IBlockCastedSpell {

	public static final String NAME = "gravelfall";

	private void spawn(World world, BlockPos pos) {
		FallingBlockEntity entity = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, Blocks.GRAVEL.getDefaultState());

		entity.fallTime = 1;
		entity.setHurtEntities(true);
		world.addEntity(entity);
	}

	private ActionResultType checkAndSpawn(Entity sender, World world, BlockPos pos) {
		if (world.isAirBlock(pos)) {
			if (sender instanceof PlayerEntity && !((PlayerEntity) sender).isCreative()) {
				PlayerInventory inv = ((PlayerEntity) sender).inventory;
				int slot = inv.getSlotFor(new ItemStack(Items.GRAVEL));

				if (slot >= 0) {
					inv.getStackInSlot(slot).shrink(1);
					spawn(world, pos);
					return ActionResultType.SUCCESS;
				}
			} else {
				spawn(world, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	private ActionResultType spawnGravel(Entity sender, BlockPos pos) {
		World world = sender.getEntityWorld();

		checkAndSpawn(sender, world, pos.up(4));
		checkAndSpawn(sender, world, pos.up(5));
		return checkAndSpawn(sender, world, pos.up(6));
	}

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		return spawnGravel(sender, target);
	}

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		return spawnGravel(sender, target.getPosition());
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers() {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers();

		multimap.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(REACH_DISTANCE_MODIFIER, "Reach distance modifier", 5.0D, AttributeModifier.Operation.ADDITION));
		return multimap;
	}

}
