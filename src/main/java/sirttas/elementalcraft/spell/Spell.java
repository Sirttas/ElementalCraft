package sirttas.elementalcraft.spell;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class Spell extends net.minecraftforge.registries.ForgeRegistryEntry<Spell> {

	protected static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("1a6a2857-a598-40e4-9161-5b58bb14e9bc");

	public static final IForgeRegistry<Spell> REGISTRY = RegistryManager.ACTIVE.getRegistry(Spell.class);

	private String translationKey;

	public String getTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.makeTranslationKey("spell", REGISTRY.getKey(this));
		}

		return this.translationKey;
	}

	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(getTranslationKey());
	}

	public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
		return HashMultimap.create();
	}

}
