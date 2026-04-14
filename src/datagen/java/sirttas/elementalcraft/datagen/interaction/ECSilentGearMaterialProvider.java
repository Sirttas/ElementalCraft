package sirttas.elementalcraft.datagen.interaction;

import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.silentchaos512.gear.api.data.material.MaterialBuilder;
import net.silentchaos512.gear.api.material.IMaterialCategory;
import net.silentchaos512.gear.api.material.TextureType;
import net.silentchaos512.gear.api.property.HarvestTier;
import net.silentchaos512.gear.api.property.NumberProperty;
import net.silentchaos512.gear.api.util.DataResource;
import net.silentchaos512.gear.api.util.PartGearKey;
import net.silentchaos512.gear.data.MaterialsProvider;
import net.silentchaos512.gear.gear.material.MaterialCategories;
import net.silentchaos512.gear.gear.material.SimpleMaterial;
import net.silentchaos512.gear.gear.trait.condition.MaterialRatioTraitCondition;
import net.silentchaos512.gear.setup.gear.GearProperties;
import net.silentchaos512.gear.setup.gear.GearTypes;
import net.silentchaos512.gear.setup.gear.PartTypes;
import net.silentchaos512.gear.util.Const;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ECSilentGearMaterialProvider extends MaterialsProvider {

    public ECSilentGearMaterialProvider(DataGenerator generator) {
        super(generator, ElementalCraftApi.MODID);
    }

    @Nonnull
    @Override
    protected Collection<MaterialBuilder<?>> getMaterials() {
        List<MaterialBuilder<?>> values = new ArrayList<>();

        createBuilder(values, ECBlocks.WHITE_ROCK, MaterialCategories.ROCK, MaterialCategories.BASIC)
                .displayWithDefaultName(0xb1a69b, TextureType.LOW_CONTRAST)
                //main
                .mainStatsCommon(151, 5, 5, 4, 0.5f)
                .mainStatsHarvest( 4)
                .mainStatsMelee(1, 0, 0.0f)
                .stat(PartGearKey.ofMain(GearTypes.AXE), GearProperties.ATTACK_SPEED, -0.2f)
                .stat(PartGearKey.ofMain(GearTypes.HOE), GearProperties.ATTACK_SPEED, -1f)
                .mainStatsRanged(0, -0.2f, 1f, 0.8f)
                .mainStatsArmor(1, 2, 1, 1, 0, 0) //5
                .trait(PartTypes.MAIN, Const.Traits.ANCIENT, 1)
                .stat(PartTypes.MAIN, GearProperties.HARVEST_TIER, HarvestTier.STONE)
                //rod
                .trait(PartTypes.ROD, Const.Traits.BRITTLE, 1)
                .trait(PartTypes.ROD, Const.Traits.CRUSHING, 2)
                .stat(PartTypes.ROD, GearProperties.DURABILITY, -0.1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.ROD, GearProperties.HARVEST_SPEED, 0.2f, NumberProperty.Operation.MULTIPLY_TOTAL);
        createBuilder(values, ECTags.Items.INGOTS_DRENCHED_IRON, MaterialCategories.METAL, MaterialCategories.BASIC)
                .displayWithDefaultName(0xcddff2, TextureType.LOW_CONTRAST)
                //main
                .mainStatsCommon(285, 15, 15, 22, 0.7f)
                .mainStatsHarvest(6)
                .mainStatsMelee(2, 1, 0.0f)
                .stat(PartGearKey.ofMain(GearTypes.AXE), GearProperties.ATTACK_SPEED, -0.1f)
                .stat(PartGearKey.ofMain(GearTypes.HOE), GearProperties.ATTACK_SPEED, 0f)
                .mainStatsRanged(1, 0.1f)
                .mainStatsProjectile(1.0f, 1.1f)
                .mainStatsArmor(2, 6, 5, 2, 0, 7) //15
                .trait(PartTypes.MAIN, Const.Traits.MALLEABLE, 3)
                .trait(PartTypes.MAIN, Const.Traits.AQUATIC, 1)
                .stat(PartTypes.MAIN, GearProperties.HARVEST_TIER, HarvestTier.IRON)
                //rod
                .trait(PartTypes.ROD, Const.Traits.FLEXIBLE, 2)
                .stat(PartTypes.ROD, GearProperties.DURABILITY, 0.10f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.ROD, GearProperties.ENCHANTMENT_VALUE, -1, NumberProperty.Operation.ADD)
                .stat(PartTypes.ROD, GearProperties.ENCHANTMENT_VALUE, -0.1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.ROD, GearProperties.ATTACK_DAMAGE, 0.1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                //tip
                .trait(PartTypes.TIP, Const.Traits.MALLEABLE, 2)
                .trait(PartTypes.TIP, Const.Traits.AQUATIC, 1)
                .stat(PartTypes.TIP, GearProperties.DURABILITY, 158, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ARMOR_DURABILITY, 4, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.HARVEST_TIER, HarvestTier.STONE)
                .stat(PartTypes.TIP, GearProperties.HARVEST_SPEED, 1, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ATTACK_DAMAGE, 1, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.DRAW_SPEED, 0.2f, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.RARITY, 10, NumberProperty.Operation.ADD);
        createBuilder(values, ECTags.Items.INGOTS_SWIFT_ALLOY, MaterialCategories.METAL, MaterialCategories.ADVANCED)
                .displayWithDefaultName(0xeeb961, TextureType.LOW_CONTRAST)
                //main
                .mainStatsCommon(390, 25, 22, 50, 1.3f)
                .mainStatsHarvest(12)
                .mainStatsMelee(2, 4, 0.0f)
                .stat(PartGearKey.ofMain(GearTypes.HOE), GearProperties.ATTACK_SPEED, -2f)
                .mainStatsRanged(2, 0.3f)
                .mainStatsProjectile(1.1f, 1.0f)
                .mainStatsArmor(3, 7, 5, 3, 2, 8) //18
                .trait(PartTypes.MAIN, Const.Traits.LIGHT, 2, new MaterialRatioTraitCondition(0.5f))
                .trait(PartTypes.MAIN, Const.Traits.BRILLIANT, 1, new MaterialRatioTraitCondition(0.7f))
                .trait(PartTypes.MAIN, Const.Traits.MALLEABLE, 4)
                .trait(PartTypes.MAIN, Const.Traits.ACCELERATE, 2)
                .stat(PartTypes.MAIN, GearProperties.REPAIR_VALUE, 0.75f)
                .stat(PartTypes.MAIN, GearProperties.HARVEST_TIER, HarvestTier.DIAMOND)
                //rod
                .trait(PartTypes.ROD, Const.Traits.MALLEABLE, 4, new MaterialRatioTraitCondition(0.5f))
                .stat(PartTypes.ROD, GearProperties.HARVEST_SPEED, 3, NumberProperty.Operation.ADD)
                .stat(PartTypes.ROD, GearProperties.DURABILITY, 0.20f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.ROD, GearProperties.ENCHANTMENT_VALUE, 3, NumberProperty.Operation.ADD)
                .stat(PartTypes.ROD, GearProperties.ENCHANTMENT_VALUE, 0.1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.ROD, GearProperties.ATTACK_DAMAGE, 0.1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.ROD, GearProperties.RARITY, 40)
                //tip
                .trait(PartTypes.TIP, Const.Traits.MALLEABLE, 1)
                .trait(PartTypes.TIP, Const.Traits.ACCELERATE, 1)
                .stat(PartTypes.TIP, GearProperties.DURABILITY, 200, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ARMOR_DURABILITY, 8, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.HARVEST_SPEED, 6, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.MAGIC_DAMAGE, 2, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.DRAW_SPEED, 0.2f, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.RARITY, 30, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ATTACK_DAMAGE, 2, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ATTACK_SPEED, 0.3f, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.RANGED_DAMAGE, 2, NumberProperty.Operation.ADD)
                //coating
                .trait(PartTypes.COATING, Const.Traits.BRILLIANT, 1)
                .stat(PartTypes.COATING, GearProperties.DURABILITY, -0.1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.ARMOR_DURABILITY, -0.1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.RARITY, 10, NumberProperty.Operation.ADD);
        createBuilder(values, ECTags.Items.INGOTS_FIREITE, MaterialCategories.METAL, MaterialCategories.ENDGAME)
                .display(Component.translatable("material.elementalcraft.fireite"), 0x5a383b, TextureType.HIGH_CONTRAST)
                // tip
                .trait(PartTypes.TIP, Const.Traits.MALLEABLE, 2)
                .trait(PartTypes.TIP, Const.Traits.FIERY, 2)
                .trait(PartTypes.TIP, Const.Traits.JAGGED, 1)
                .stat(PartTypes.TIP, GearProperties.DURABILITY, 350, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ARMOR_DURABILITY, 10, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.HARVEST_TIER, HarvestTier.DIAMOND)
                .stat(PartTypes.TIP, GearProperties.HARVEST_SPEED, 3, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ATTACK_DAMAGE, 2, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.MAGIC_DAMAGE, 3, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.RANGED_DAMAGE, 0.5f, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.RARITY, 40, NumberProperty.Operation.ADD)
                //coating
                .trait(PartTypes.COATING, Const.Traits.FIREPROOF, 1)
                .stat(PartTypes.COATING, GearProperties.DURABILITY, 0.4f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.DURABILITY, 3, NumberProperty.Operation.ADD)
                .stat(PartTypes.COATING, GearProperties.ARMOR_DURABILITY, 4f / 3f - 1f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.HARVEST_TIER, HarvestTier.NETHERITE)
                .stat(PartTypes.COATING, GearProperties.HARVEST_SPEED, 0.15f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.ATTACK_DAMAGE, 0.5f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.MAGIC_DAMAGE, 0.5f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.RANGED_DAMAGE, 0.5f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.COATING, GearProperties.ARMOR_TOUGHNESS, 4, NumberProperty.Operation.ADD)
                .stat(PartTypes.COATING, GearProperties.KNOCKBACK_RESISTANCE, 1f, NumberProperty.Operation.ADD)
                .stat(PartTypes.COATING, GearProperties.ENCHANTMENT_VALUE, 7, NumberProperty.Operation.ADD)
                // adornment
                .trait(PartTypes.SETTING, Const.Traits.BASTION, 1);
        createBuilder(values, ECItems.AIR_SILK, MaterialCategories.ORGANIC, MaterialCategories.FIBER, MaterialCategories.BASIC)
                .displayWithDefaultName(0xB3804B, TextureType.LOW_CONTRAST)
                //binding
                .trait(PartTypes.BINDING, Const.Traits.FLEXIBLE, 1)
                .stat(PartTypes.BINDING, GearProperties.REPAIR_EFFICIENCY, 0.05f, NumberProperty.Operation.MULTIPLY_BASE)
                .stat(PartTypes.BINDING, GearProperties.HARVEST_SPEED, 0.1f, NumberProperty.Operation.MULTIPLY_BASE)
                //cord
                .trait(PartTypes.CORD, Const.Traits.ACCELERATE, 1)
                .stat(PartTypes.CORD, GearProperties.DRAW_SPEED, 0.2f, NumberProperty.Operation.MULTIPLY_BASE)
                .stat(PartTypes.CORD, GearProperties.DURABILITY, 0.05f, NumberProperty.Operation.MULTIPLY_BASE)
                .stat(PartTypes.CORD, GearProperties.RARITY, 10, NumberProperty.Operation.ADD);
        createBuilder(values, ECTags.Items.HARDENED_RODS, MaterialCategories.ROCK, MaterialCategories.METAL, MaterialCategories.ADVANCED)
                .displayWithDefaultName(0xb1a69b, TextureType.LOW_CONTRAST)
                .trait(PartTypes.ROD, Const.Traits.HARD, 2)
                .trait(PartTypes.ROD, Const.Traits.BRITTLE, 2)
                .stat(PartTypes.ROD, GearProperties.DURABILITY, 0.20f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.ROD, GearProperties.HARVEST_SPEED, 1, NumberProperty.Operation.ADD)
                .stat(PartTypes.ROD, GearProperties.ATTACK_DAMAGE, 0.5f, NumberProperty.Operation.ADD)
                .stat(PartTypes.ROD, GearProperties.RARITY, 20);
        createBuilder(values, ECItems.SPRINGALINE_SHARD, MaterialCategories.GEM, MaterialCategories.INTERMEDIATE)
                .displayWithDefaultName(0x9accfc, TextureType.HIGH_CONTRAST)
                // main
                .mainStatsCommon(300, 14, 16, 41, 1.4f)
                .mainStatsHarvest(8)
                .mainStatsMelee(2, 3, 0.1f)
                .mainStatsRanged(1, 0.1f)
                .mainStatsProjectile(1, 1)
                .mainStatsArmor(3, 6, 4, 3, 0, 10) //16
                .trait(PartTypes.MAIN, Const.Traits.CRUSHING, 3)
                .trait(PartTypes.MAIN, Const.Traits.JAGGED, 2)
                .trait(PartTypes.MAIN, Const.Traits.RENEW, 1, new MaterialRatioTraitCondition(0.7f))
                // tip
                .trait(PartTypes.TIP, Const.Traits.CHIPPING, 1)
                .trait(PartTypes.TIP, Const.Traits.JAGGED, 3)
                .trait(PartTypes.TIP, Const.Traits.SILKY, 1, new MaterialRatioTraitCondition(0.66f))
                .stat(PartTypes.TIP, GearProperties.DURABILITY, -0.12f, NumberProperty.Operation.MULTIPLY_TOTAL)
                .stat(PartTypes.TIP, GearProperties.DURABILITY, 64, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.ARMOR_DURABILITY, 64, NumberProperty.Operation.ADD)
                .stat(PartTypes.TIP, GearProperties.HARVEST_TIER, HarvestTier.IRON)
                //adornment
                .trait(PartTypes.SETTING, Const.Traits.CURSED, 4)
                .trait(PartTypes.SETTING, Const.Traits.MIGHTY, 2, new MaterialRatioTraitCondition(0.5f));
        return values;
    }

    public static MaterialBuilder<SimpleMaterial> createBuilder(List<MaterialBuilder<?>> list, Holder<? extends @NotNull ItemLike> provider, IMaterialCategory... categories) {
        var builder = MaterialBuilder.simple(DataResource.material(provider.getKey().identifier()))
                .crafting(provider.value().asItem(), categories);

        list.add(builder);
        return builder;
    }

    public static MaterialBuilder<SimpleMaterial> createBuilder(List<MaterialBuilder<?>> list, TagKey<@NotNull Item> tag, IMaterialCategory... categories) {
        var builder = MaterialBuilder.simple(DataResource.material(ElementalCraftApi.createRL(tag.location().getPath())))
                .crafting(tag, categories);

        list.add(builder);
        return builder;
    }

}
