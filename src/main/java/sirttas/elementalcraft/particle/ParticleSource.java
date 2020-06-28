package sirttas.elementalcraft.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;

@OnlyIn(Dist.CLIENT)
public class ParticleSource extends SpriteTexturedParticle {

	public static final String NAME = "source";
	@ObjectHolder(ElementalCraft.MODID + ":" + ParticleSource.NAME) public static ParticleType<Data> TYPE;

	private final double coordX;
	private final double coordY;
	private final double coordZ;

	private ParticleSource(World worldIn, Vec3d coord, IAnimatedSprite sprite, ElementType type) {
		super(worldIn, coord.getX(), coord.getY(), coord.getZ());
		this.coordX = coord.getX();
		this.coordY = coord.getY();
		this.coordZ = coord.getZ();
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.prevPosX = coordX;
		this.prevPosY = coordY;
		this.prevPosZ = coordZ;
		this.posX = this.prevPosX;
		this.posY = this.prevPosY;
		this.posZ = this.prevPosZ;
		this.particleScale = 0.5F * (this.rand.nextFloat() * 0.2F + 0.5F);
		float f = this.rand.nextFloat() * 0.4F + 0.6F;
		this.particleRed = f * type.getRed();
		this.particleGreen = f * type.getGreen();
		this.particleBlue = f * type.getBlue();
		this.canCollide = false;
		this.maxAge = 300;
		this.selectSpriteRandomly(sprite);
	}

	@Override
	public IParticleRenderType getRenderType() {
		return ECParticles.EC_RENDER;
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	@Override
	public int getBrightnessForRender(float partialTick) {
		int i = super.getBrightnessForRender(partialTick);
		float f = (float) this.age / (float) this.maxAge;
		f = f * f;
		f = f * f;
		int j = i & 255;
		int k = i >> 16 & 255;
		k = k + (int) (f * 15.0F * 16.0F);
		if (k > 240) {
			k = 240;
		}

		return j | k << 16;
	}

	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.age++ >= this.maxAge) {
			this.setExpired();
		} else {
			float f = (float) this.age / (float) this.maxAge;
			f = 1.0F - f;
			float f1 = 1.0F - f;
			f1 = f1 * f1;
			f1 = f1 * f1;
			this.posX = this.coordX + this.motionX * f;
			this.posY = this.coordY + this.motionY * f;
			this.posZ = this.coordZ + this.motionZ * f;
			this.particleScale *= f - f1 * 1.2F;
		}
	}

	public static IParticleData createData(ElementType elementType) {
		return new Data(elementType);
	}

	static class Data implements IParticleData {

		private ElementType type;

		public Data(ElementType type) {
			this.type = type;
		}

		@Override
		public ParticleType<Data> getType() {
			return TYPE;
		}

		@Override
		public void write(PacketBuffer buffer) {
			// nothing to do
		}

		@Override
		public String getParameters() {
			return getType().getRegistryName().toString() + " " + getElementType().getName();
		}

		public ElementType getElementType() {
			return this.type;
		}
	}

	static final IParticleData.IDeserializer<Data> DESERIALIZER = new IParticleData.IDeserializer<Data>() {
		@Override
		public Data deserialize(ParticleType<Data> particleTypeIn, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new Data(ElementType.byName(reader.readString()));
		}

		@Override
		public Data read(ParticleType<Data> particleTypeIn, PacketBuffer buffer) {
			return new Data(ElementType.byName(buffer.readString()));
		}
	};

	@OnlyIn(Dist.CLIENT)
	static class Factory implements IParticleFactory<Data> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(Data data, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ParticleSource(worldIn, new Vec3d(x, y, z), this.spriteSet, data.getElementType());
		}
	}

}