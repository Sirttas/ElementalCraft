package sirttas.elementalcraft.block.spelldesk;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ItemElemental;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.tag.ECTags;

public class BlockSpellDesk extends HorizontalBlock {

	public static final String NAME = "spell_desk";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(4D, 0D, 4D, 12D, 2D, 12D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(5D, 2D, 5D, 11D, 3D, 11D);
	private static final VoxelShape PILAR = Block.makeCuboidShape(6D, 3D, 6D, 10D, 11D, 10D);

	private static final VoxelShape PLATE_WEST_1 = Block.makeCuboidShape(1D, 8D, 2D, 6D, 10D, 14D);
	private static final VoxelShape PLATE_WEST_2 = Block.makeCuboidShape(4D, 10D, 2D, 11D, 12D, 14D);
	private static final VoxelShape PLATE_WEST_3 = Block.makeCuboidShape(9D, 12D, 2D, 15D, 14D, 14D);

	private static final VoxelShape PLATE_EAST_1 = Block.makeCuboidShape(10D, 8D, 2D, 15D, 10D, 14D);
	private static final VoxelShape PLATE_EAST_2 = Block.makeCuboidShape(5D, 10D, 2D, 12D, 12D, 14D);
	private static final VoxelShape PLATE_EAST_3 = Block.makeCuboidShape(1D, 12D, 2D, 7D, 14D, 14D);

	private static final VoxelShape PLATE_NORTH_1 = Block.makeCuboidShape(2D, 8D, 1D, 14D, 10D, 6D);
	private static final VoxelShape PLATE_NORTH_2 = Block.makeCuboidShape(2D, 10D, 4D, 14D, 12D, 11D);
	private static final VoxelShape PLATE_NORTH_3 = Block.makeCuboidShape(2D, 12D, 9D, 14D, 14D, 15D);

	private static final VoxelShape PLATE_SOUTH_1 = Block.makeCuboidShape(2D, 8D, 10D, 14D, 10D, 15D);
	private static final VoxelShape PLATE_SOUTH_2 = Block.makeCuboidShape(2D, 10D, 5D, 14D, 12D, 12D);
	private static final VoxelShape PLATE_SOUTH_3 = Block.makeCuboidShape(2D, 12D, 1D, 14D, 14D, 7D);

	private static final VoxelShape MAIN_SHAPE = VoxelShapes.or(BASE_1, BASE_2, PILAR);

	private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_NORTH_1, PLATE_NORTH_2, PLATE_NORTH_3);
	private static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_SOUTH_1, PLATE_SOUTH_2, PLATE_SOUTH_3);
	private static final VoxelShape WEST_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_WEST_1, PLATE_WEST_2, PLATE_WEST_3);
	private static final VoxelShape EAST_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_EAST_1, PLATE_EAST_2, PLATE_EAST_3);

	public static final BooleanProperty HAS_PAPER = BooleanProperty.create("has_paper");
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public BlockSpellDesk() {
		super(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(HAS_PAPER, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(FACING, HAS_PAPER);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		case NORTH:
			return NORTH_SHAPE;
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		default:
			return MAIN_SHAPE;
		}
	}

	private boolean hasPaper(BlockState state) {
		return Boolean.TRUE.equals(state.get(HAS_PAPER));
	}

	private void createSpell(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ElementType type) {
		Random rand = world.rand;
		int count = Math.min(10, stack.getCount());
		Vector3d position = Vector3d.copy(pos).add(0.5, 0.7, 0.5);

		if (rand.nextDouble() < count * 0.06D + 0.2D) {
			Spell spell = SpellHelper.randomSpell(type, rand);
			ItemStack scroll = new ItemStack(ECItems.SCROLL);
			
			SpellHelper.setSpell(scroll, spell);
			if (!world.isRemote()) {
				world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), scroll));
			} else {
				ParticleHelper.createCraftingParticle(type, world, Vector3d.copyCentered(pos).add(0, 0.7, 0), rand);
			}
		} else {
			world.playSound(position.getX(), position.getY(), position.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + rand.nextFloat() * 0.4F, false);
			ParticleHelper.createItemBreakParticle(world, position, rand, new ItemStack(Items.PAPER), count);
		}
		if (!player.isCreative()) {
			stack.shrink(count);
			world.setBlockState(pos, state.with(HAS_PAPER, false));
		}
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.getItem() == ECItems.SCROLL_PAPER && !hasPaper(state)) {
			world.setBlockState(pos, state.with(HAS_PAPER, true));
			if (!player.isCreative()) {
				stack.shrink(1);
			}
		} else if (hasPaper(state)) {
			ElementType type = getCrystalElementType(stack.getItem());

			if (type != ElementType.NONE) {
				createSpell(state, world, pos, player, stack, type);
			}
		}
		return ActionResultType.PASS;
	}

	private ElementType getCrystalElementType(Item item) {
		if (ECTags.Items.ELEMENTAL_CRYSTALS.contains(item) && item instanceof ItemElemental) {
			return ((ItemElemental) item).getElementType();
		}
		return ElementType.NONE;
	}

}
