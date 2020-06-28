package sirttas.elementalcraft.world.dimension.boss;

import java.util.function.Function;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NetherGenSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ITeleporter;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.world.biome.ECBiomes;
import sirttas.elementalcraft.world.dimension.ECDimensions;

public class BossDimension extends Dimension {

	public static final String NAME = "bossdimension";
	private static final int SPAWN_HEIGHT = 70; // TODO CONFIG ?
	public static final ITeleporter TELEPORTER = new ITeleporter() {
		@Override
		public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
			Entity repositionedEntity = repositionEntity.apply(false);
			BlockPos newPos = BlockPos.ZERO;

			if (destWorld.getDimension().getType() == BossDimension.getDimensionType()) {
				ECBossFightManager fightManager = ((BossDimension) destWorld.getDimension()).getBossFightManager();

				newPos = destWorld.getDimension().findSpawn(0, 0, false);
				if (repositionedEntity instanceof ServerPlayerEntity) {
					fightManager.addPlayer((ServerPlayerEntity) repositionedEntity);
				}
				fightManager.init(ElementType.EARTH); // TODO cange type for all 4 boss
			} else if (destWorld.getDimension().getType() == DimensionType.OVERWORLD) {
				newPos = destWorld.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, destWorld.getSpawnPoint()); // TODO use starting point
			}
			repositionedEntity.setPositionAndUpdate(newPos.getX(), newPos.getY(), newPos.getZ());
			return repositionedEntity;
		}
	};

	private final ECBossFightManager bossFightManager;

	public static DimensionType getDimensionType() {
		return DimensionManager.registerOrGetDimension(new ResourceLocation(ElementalCraft.MODID, NAME), ECDimensions.bossDimension, null, true);
	}

	public BossDimension(World world, DimensionType type) {
		super(world, type, 0);
		bossFightManager = world instanceof ServerWorld ? new ECBossFightManager((ServerWorld) world) : null;
	}

	@Override
	public ChunkGenerator<?> createChunkGenerator() {
		NetherGenSettings settings = ChunkGeneratorType.CAVES.createSettings();

		settings.setDefaultBlock(Blocks.STONE.getDefaultState());
		settings.setDefaultFluid(Blocks.LAVA.getDefaultState());
		return ChunkGeneratorType.CAVES.create(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings(this.world.getWorldInfo()).setBiome(ECBiomes.EARTH)), settings);
	}

	@Override
	public void tick() {
		if (bossFightManager != null) {
			bossFightManager.tick();
		}
	}

	@Override
	public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid) {
		return this.findSpawn(chunkPosIn.x * 16, chunkPosIn.z * 16, checkValid);
	}

	@Override
	public BlockPos findSpawn(int x, int z, boolean checkValid) {
		BlockPos.Mutable pos = new BlockPos.Mutable(x, SPAWN_HEIGHT, z);

		for (int i = 0; i < SPAWN_HEIGHT; i++) {
			pos.setPos(x, SPAWN_HEIGHT - i, z);
			if (world.isAirBlock(pos) && !world.isAirBlock(pos.add(x, -1, z))) {
				return pos;
			}
		}
		return pos;
	}

	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return 0;
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Vec3d getFogColor(float celestialAngle, float partialTicks) {
		float f = MathHelper.cos(celestialAngle * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		float f1 = 0.7529412F;
		float f2 = 0.84705883F;
		float f3 = 1.0F;
		f1 = f1 * (f * 0.94F + 0.06F);
		f2 = f2 * (f * 0.94F + 0.06F);
		f3 = f3 * (f * 0.91F + 0.09F);
		return new Vec3d(f1, f2, f3);
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean doesXZShowFog(int x, int z) {
		return false;
	}

	public ECBossFightManager getBossFightManager() {
		return bossFightManager;
	}
}
