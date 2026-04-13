package sirttas.elementalcraft.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.spell.StaffItem;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.SculkCrackingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BindingRecipe;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.SimpleInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.SimpleGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;
import sirttas.elementalcraft.recipe.melting.MeltingRecipe;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;
import sirttas.elementalcraft.recipe.spell.SpellCraftRecipe;

public class ECRecipeSerializers {

	private static final DeferredRegister<@NotNull RecipeSerializer<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ElementalCraftApi.MODID);

	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull SimpleInfusionRecipe>> INFUSION = register(InfusionRecipe.NAME, SimpleInfusionRecipe.CODEC, SimpleInfusionRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull ToolInfusionRecipe>> TOOL_INFUSION = register(ToolInfusionRecipe.NAME, ToolInfusionRecipe.CODEC, ToolInfusionRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull BindingRecipe>> BINDING = register(AbstractBindingRecipe.NAME, BindingRecipe.CODEC, BindingRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull CrystallizationRecipe>> CRYSTALLIZATION = register(CrystallizationRecipe.NAME, CrystallizationRecipe.CODEC, CrystallizationRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull InscriptionRecipe>> INSCRIPTION = register(InscriptionRecipe.NAME, InscriptionRecipe.CODEC, InscriptionRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull OrePurificationRecipe>> ORE_PURIFICATION = register(OrePurificationRecipe.NAME, OrePurificationRecipe.CODEC, OrePurificationRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull SimpleGrindingRecipe>> GRINDING = register(GrindingRecipe.NAME, SimpleGrindingRecipe.CODEC, SimpleGrindingRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull SawingRecipe>> SAWING = register(SawingRecipe.NAME, SawingRecipe.CODEC, SawingRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull PureInfusionRecipe>> PURE_INFUSION = register(PureInfusionRecipe.NAME, PureInfusionRecipe.CODEC, PureInfusionRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull SpellCraftRecipe>> SPELL_CRAFT = register(SpellCraftRecipe.NAME, SpellCraftRecipe.CODEC, SpellCraftRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull StaffRecipe>> STAFF = register(StaffItem.NAME, StaffRecipe.CODEC, StaffRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull CrackingRecipe>> CRACKING = register(CrackingRecipe.NAME, CrackingRecipe.CODEC, CrackingRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull SculkCrackingRecipe>> SCULK_CRACKING = register(SculkCrackingRecipe.NAME, SculkCrackingRecipe.CODEC, SculkCrackingRecipe.STREAM_CODEC);
	public static final DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull MeltingRecipe>> MELTING = register(MeltingRecipe.NAME, MeltingRecipe.CODEC, MeltingRecipe.STREAM_CODEC);

	private ECRecipeSerializers() {}

	private static <T extends Recipe<?>> DeferredHolder<@NotNull RecipeSerializer<?>, @NotNull RecipeSerializer<@NotNull T>> register(String name, MapCodec<T> codec, StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull T> streamCodec) {
		return DEFERRED_REGISTER.register(name, () -> new RecipeSerializer<>(codec, streamCodec));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
