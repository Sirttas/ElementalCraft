package sirttas.elementalcraft.spell.properties;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.gui.GuiHelper;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.spell.Spell;

public class SpellProperties {

	public static final SpellProperties NONE = new SpellProperties();
	public static final Serializer SEZRIALIZER = new Serializer();

	private int cooldown;
	private int consumeAmount;
	private int useDuration;
	private int weight;
	private int color;
	private float range;
	private ElementType elementType;
	private Spell.Type spellType;

	public SpellProperties() {
		spellType = Spell.Type.NONE;
		elementType = ElementType.NONE;
		weight = 0;
		useDuration = 0;
		consumeAmount = 0;
		cooldown = 0;
		color = -1;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getConsumeAmount() {
		return consumeAmount;
	}

	public int getUseDuration() {
		return useDuration;
	}

	public int getWeight() {
		return weight;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public Spell.Type getSpellType() {
		return spellType;
	}

	public float getRange() {
		return range;
	}

	public int getColor() {
		return color;
	}

	public static class Serializer implements JsonDeserializer<SpellProperties> {

		private Serializer() {
		}

		@Override
		public SpellProperties deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
			SpellProperties properties = new SpellProperties();
			JsonObject jsonObject = (JsonObject) json;

			properties.spellType = Spell.Type.byName(JSONUtils.getString(jsonObject, ECNames.SPELL_TYPE));
			properties.elementType = ElementType.byName(JSONUtils.getString(jsonObject, ECNames.ELEMENT_TYPE));
			properties.cooldown = jsonObject.has(ECNames.COOLDOWN) ? JSONUtils.getInt(jsonObject, ECNames.COOLDOWN) : 0;
			properties.consumeAmount = jsonObject.has(ECNames.ELEMENT_CONSUMPTION) ? JSONUtils.getInt(jsonObject, ECNames.ELEMENT_CONSUMPTION) : 0;
			properties.useDuration = jsonObject.has(ECNames.USE_DURATION) ? JSONUtils.getInt(jsonObject, ECNames.USE_DURATION) : 0;
			properties.weight = jsonObject.has(ECNames.WEIGHT) ? JSONUtils.getInt(jsonObject, ECNames.WEIGHT) : 1;
			properties.range = jsonObject.has(ECNames.RANGE) ? JSONUtils.getFloat(jsonObject, ECNames.RANGE) : 0;
			properties.color = jsonObject.has(ECNames.COLOR) ? JSONUtils.getInt(jsonObject, ECNames.COLOR) : -1;
			return properties;
		}

		public SpellProperties read(PacketBuffer buf) {
			SpellProperties properties = new SpellProperties();

			properties.spellType = Spell.Type.byName(buf.readString());
			properties.elementType = ElementType.byName(buf.readString());
			properties.cooldown = buf.readInt();
			properties.consumeAmount = buf.readInt();
			properties.useDuration = buf.readInt();
			properties.weight = buf.readInt();
			properties.range = buf.readFloat();
			properties.color = buf.readInt();
			return properties;
		}

		public void write(SpellProperties properties, PacketBuffer buf) {
			buf.writeString(properties.spellType.getString());
			buf.writeString(properties.elementType.getString());
			buf.writeInt(properties.cooldown);
			buf.writeInt(properties.consumeAmount);
			buf.writeInt(properties.useDuration);
			buf.writeInt(properties.weight);
			buf.writeFloat(properties.range);
			buf.writeInt(properties.color);
		}
	}
	
	public static final class Builder {
		private int cooldown;
		private int consumeAmount;
		private int useDuration;
		private int weight;
		private int color;
		private double range;
		private ElementType elementType;
		private final Spell.Type type;

		private Builder(Spell.Type type) {
			this.type = type;
			this.elementType = ElementType.NONE;
			cooldown = 0;
			consumeAmount = 0;
			useDuration = 0;
			weight = 0;
		}

		public static Builder create(Spell.Type type) {
			return new Builder(type);
		}

		public Builder cooldown(int cooldown) {
			this.cooldown = cooldown;
			return this;
		}

		public Builder consumeAmount(int consumeAmount) {
			this.consumeAmount = consumeAmount;
			return this;
		}

		public Builder useDuration(int useDuration) {
			this.useDuration = useDuration;
			return this;
		}


		public Builder elementType(ElementType elementType) {
			this.elementType = elementType;
			return this;
		}

		public Builder weight(int weight) {
			this.weight = weight;
			return this;
		}

		public Builder range(double range) {
			this.range = range;
			return this;
		}

		public Builder color(int color) {
			this.color = color;
			return this;
		}

		public Builder color(int r, int g, int b) {
			return color(GuiHelper.colorFromRGB(r, g, b));
		}

		public JsonElement toJson() {
			JsonObject json = new JsonObject();

			json.addProperty(ECNames.SPELL_TYPE, type.getString());
			json.addProperty(ECNames.ELEMENT_TYPE, elementType.getString());
			if (cooldown > 0) {
				json.addProperty(ECNames.COOLDOWN, cooldown);
			}
			if (consumeAmount > 0) {
				json.addProperty(ECNames.ELEMENT_CONSUMPTION, consumeAmount);
			}
			if (useDuration > 0) {
				json.addProperty(ECNames.USE_DURATION, useDuration);
			}
			if (weight > 0) {
				json.addProperty(ECNames.WEIGHT, weight);
			}
			if (range > 0) {
				json.addProperty(ECNames.RANGE, range);
			}
			if (color > 0) {
				json.addProperty(ECNames.COLOR, color);
			}
			return json;
		}

	}
}
