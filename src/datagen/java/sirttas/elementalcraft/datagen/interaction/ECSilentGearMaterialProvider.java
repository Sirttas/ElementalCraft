package sirttas.elementalcraft.datagen.interaction;

import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.api.stats.StatInstance;
import net.silentchaos512.gear.client.model.PartTextures;
import net.silentchaos512.gear.data.material.MaterialBuilder;
import net.silentchaos512.gear.data.material.MaterialsProvider;
import net.silentchaos512.gear.gear.material.MaterialCategories;
import net.silentchaos512.gear.gear.part.PartTextureSet;
import net.silentchaos512.gear.gear.trait.condition.MaterialRatioTraitCondition;
import net.silentchaos512.gear.util.Const;
import sirttas.elementalcraft.ElementalCraft;
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
    protected Collection<MaterialBuilder> getMaterials() {
        ResourceLocation chargeability = new ResourceLocation("silentgems", "chargeability");
        List<MaterialBuilder> values = new ArrayList<>();

        createBuilder(values, ECBlocks.WHITE_ROCK, 1)
                .categories(MaterialCategories.ROCK)
                .stat(PartType.MAIN, ItemStats.DURABILITY, 151)
                .stat(PartType.MAIN, ItemStats.ARMOR_DURABILITY, 5)
                .stat(PartType.MAIN, ItemStats.ENCHANTMENT_VALUE, 5)
                .stat(PartType.MAIN, ItemStats.HARVEST_LEVEL, 1)
                .stat(PartType.MAIN, ItemStats.HARVEST_SPEED, 4)
                .stat(PartType.MAIN, ItemStats.MELEE_DAMAGE, 1)
                .stat(PartType.MAIN, ItemStats.MAGIC_DAMAGE, 0)
                .stat(PartType.MAIN, ItemStats.ATTACK_SPEED, 0.0f)
                .stat(PartType.MAIN, ItemStats.ARMOR, 5)
                .stat(PartType.MAIN, ItemStats.ARMOR_TOUGHNESS, 0)
                .stat(PartType.MAIN, ItemStats.MAGIC_ARMOR, 0)
                .stat(PartType.MAIN, ItemStats.RANGED_DAMAGE, 0)
                .stat(PartType.MAIN, ItemStats.RANGED_SPEED, -0.2f)
                .stat(PartType.MAIN, ItemStats.RARITY, 7)
                .stat(PartType.MAIN, chargeability, 0.5f)
                .stat(PartType.ROD, ItemStats.DURABILITY, -0.1f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.HARVEST_SPEED, 0.2f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.RARITY, 4)
                .trait(PartType.MAIN, Const.Traits.ANCIENT, 2)
                .trait(PartType.ROD, Const.Traits.BRITTLE, 1)
                .trait(PartType.ROD, Const.Traits.CRUSHING, 2)
                .display(PartType.MAIN, PartTextureSet.LOW_CONTRAST, 0xb1a69b)
                .display(PartType.ROD, PartTextureSet.LOW_CONTRAST, 0xb1a69b);
        createBuilder(values, ECTags.Items.INGOTS_DRENCHED_IRON, 2)
                .categories(MaterialCategories.METAL)
                .stat(PartType.MAIN, ItemStats.DURABILITY, 235)
                .stat(PartType.MAIN, ItemStats.ARMOR_DURABILITY, 15)
                .stat(PartType.MAIN, ItemStats.ENCHANTMENT_VALUE, 15)
                .stat(PartType.MAIN, ItemStats.HARVEST_LEVEL, 2)
                .stat(PartType.MAIN, ItemStats.HARVEST_SPEED, 6)
                .stat(PartType.MAIN, ItemStats.MELEE_DAMAGE, 2)
                .stat(PartType.MAIN, ItemStats.MAGIC_DAMAGE, 1)
                .stat(PartType.MAIN, ItemStats.ATTACK_SPEED, -0.1f)
                .stat(PartType.MAIN, ItemStats.ARMOR, 15)
                .stat(PartType.MAIN, ItemStats.ARMOR_TOUGHNESS, 0)
                .stat(PartType.MAIN, ItemStats.MAGIC_ARMOR, 7)
                .stat(PartType.MAIN, ItemStats.RANGED_DAMAGE, 1)
                .stat(PartType.MAIN, ItemStats.RANGED_SPEED, 0.1f)
                .stat(PartType.MAIN, ItemStats.RARITY, 22)
                .stat(PartType.MAIN, chargeability, 0.7f)
                .stat(PartType.ROD, ItemStats.DURABILITY, 0.10f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.ENCHANTMENT_VALUE, -1, StatInstance.Operation.ADD)
                .stat(PartType.ROD, ItemStats.ENCHANTMENT_VALUE, -0.1f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.MELEE_DAMAGE, 0.1f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.RARITY, 22)
                .stat(PartType.TIP, ItemStats.DURABILITY, 108, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.ARMOR_DURABILITY, 4, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.HARVEST_LEVEL, 2, StatInstance.Operation.MAX)
                .stat(PartType.TIP, ItemStats.HARVEST_SPEED, 1, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.MELEE_DAMAGE, 1, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RANGED_SPEED, 0.2f, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RARITY, 10, StatInstance.Operation.ADD)
                .trait(PartType.MAIN, Const.Traits.MALLEABLE, 3)
                .trait(PartType.MAIN, Const.Traits.AQUATIC, 1)
                .trait(PartType.MAIN, Const.Traits.MAGNETIC, 1, new MaterialRatioTraitCondition(0.66f))
                .trait(PartType.ROD, Const.Traits.MAGNETIC, 3, new MaterialRatioTraitCondition(0.5f))
                .trait(PartType.TIP, Const.Traits.MALLEABLE, 2)
                .trait(PartType.TIP, Const.Traits.AQUATIC, 1)
                .display(PartType.MAIN, PartTextureSet.HIGH_CONTRAST_WITH_HIGHLIGHT, 0xcddff2)
                .display(PartType.ROD, PartTextureSet.LOW_CONTRAST, 0xcddff2)
                .displayTip(PartTextures.TIP_SHARP, 0xcddff2);
        createBuilder(values, ECTags.Items.INGOTS_SWIFT_ALLOY, 3)
                .categories(MaterialCategories.METAL)
                .stat(PartType.MAIN, ItemStats.DURABILITY, 390)
                .stat(PartType.MAIN, ItemStats.ARMOR_DURABILITY, 25)
                .stat(PartType.MAIN, ItemStats.REPAIR_VALUE, 0.75f)
                .stat(PartType.MAIN, ItemStats.ENCHANTMENT_VALUE, 22)
                .stat(PartType.MAIN, ItemStats.HARVEST_LEVEL, 3)
                .stat(PartType.MAIN, ItemStats.HARVEST_SPEED, 12)
                .stat(PartType.MAIN, ItemStats.MELEE_DAMAGE, 3)
                .stat(PartType.MAIN, ItemStats.MAGIC_DAMAGE, 3)
                .stat(PartType.MAIN, ItemStats.ATTACK_SPEED, -0.1f)
                .stat(PartType.MAIN, ItemStats.ARMOR, 18)
                .stat(PartType.MAIN, ItemStats.ARMOR_TOUGHNESS, 2)
                .stat(PartType.MAIN, ItemStats.MAGIC_ARMOR, 6)
                .stat(PartType.MAIN, ItemStats.RANGED_DAMAGE, 2)
                .stat(PartType.MAIN, ItemStats.RANGED_SPEED, -0.1f)
                .stat(PartType.MAIN, ItemStats.RARITY, 50)
                .stat(PartType.MAIN, chargeability, 1.1F)
                .stat(PartType.ROD, ItemStats.DURABILITY, 0.20f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.ENCHANTMENT_VALUE, 3, StatInstance.Operation.ADD)
                .stat(PartType.ROD, ItemStats.ENCHANTMENT_VALUE, 0.1f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.MELEE_DAMAGE, 0.1f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.RARITY, 40)
                .stat(PartType.TIP, ItemStats.DURABILITY, 200, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.ARMOR_DURABILITY, 8, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.HARVEST_LEVEL, 3, StatInstance.Operation.MAX)
                .stat(PartType.TIP, ItemStats.HARVEST_SPEED, 3, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.HARVEST_SPEED, 0.2f, StatInstance.Operation.MUL2)
                .stat(PartType.TIP, ItemStats.MELEE_DAMAGE, 2, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.ATTACK_SPEED, 0.3f, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RANGED_DAMAGE, 2, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RARITY, 15, StatInstance.Operation.ADD)
                .trait(PartType.MAIN, Const.Traits.LIGHT, 2)
                .trait(PartType.MAIN, Const.Traits.MALLEABLE, 4)
                .trait(PartType.MAIN, Const.Traits.ACCELERATE, 2)
                .trait(PartType.MAIN, Const.Traits.BRILLIANT, 1)
                .trait(PartType.ROD, Const.Traits.MALLEABLE, 4, new MaterialRatioTraitCondition(0.5f))
                .trait(PartType.MAIN, Const.Traits.ACCELERATE, 1)
                .trait(PartType.TIP, Const.Traits.ACCELERATE, 1)
                .display(PartType.MAIN, PartTextureSet.HIGH_CONTRAST, 0xeeb961)
                .display(PartType.ROD, PartTextureSet.LOW_CONTRAST, 0xeeb961)
                .displayTip(PartTextures.TIP_SHARP, 0xeeb961);
        createBuilder(values, ECTags.Items.INGOTS_FIREITE, 4)
                .categories(MaterialCategories.METAL)
                .namePrefix(Component.translatable("material.elementalcraft.fireite"))
                .stat(PartType.TIP, ItemStats.DURABILITY, 350, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.ARMOR_DURABILITY, 10, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.HARVEST_LEVEL, 3, StatInstance.Operation.MAX)
                .stat(PartType.TIP, ItemStats.HARVEST_SPEED, 3, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.MELEE_DAMAGE, 2, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.MAGIC_DAMAGE, 2, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RANGED_DAMAGE, 0.5f, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RARITY, 40, StatInstance.Operation.ADD)
                .stat(PartType.COATING, ItemStats.DURABILITY, 0.4f, StatInstance.Operation.MUL2)
                .stat(PartType.COATING, ItemStats.DURABILITY, 3, StatInstance.Operation.ADD)
                .stat(PartType.COATING, ItemStats.ARMOR_DURABILITY, 4f / 3f - 1f, StatInstance.Operation.MUL2)
                .stat(PartType.COATING, ItemStats.HARVEST_SPEED, 0.15f, StatInstance.Operation.MUL2)
                .stat(PartType.COATING, ItemStats.HARVEST_LEVEL, 4, StatInstance.Operation.MAX)
                .stat(PartType.COATING, ItemStats.MELEE_DAMAGE, 0.5f, StatInstance.Operation.MUL2)
                .stat(PartType.COATING, ItemStats.MAGIC_DAMAGE, 0.5f, StatInstance.Operation.MUL2)
                .stat(PartType.COATING, ItemStats.ARMOR_TOUGHNESS, 4, StatInstance.Operation.ADD)
                .stat(PartType.COATING, ItemStats.KNOCKBACK_RESISTANCE, 1f, StatInstance.Operation.ADD)
                .stat(PartType.COATING, ItemStats.ENCHANTMENT_VALUE, 7, StatInstance.Operation.ADD)
                .trait(PartType.TIP, Const.Traits.MALLEABLE, 2)
                .trait(PartType.TIP, Const.Traits.FIERY, 2)
                .trait(PartType.TIP, Const.Traits.JAGGED, 1)
                .displayTip(PartTextures.TIP_SHARP, 0x5a383b)
                .displayCoating(PartTextureSet.LOW_CONTRAST, 0x5a383b);
        createBuilder(values, ECItems.AIR_SILK, 1)
                .categories(MaterialCategories.ORGANIC, MaterialCategories.FIBER)
                .stat(PartType.BINDING, ItemStats.DURABILITY, 10, StatInstance.Operation.ADD)
                .stat(PartType.BINDING, ItemStats.HARVEST_SPEED, 0.1f, StatInstance.Operation.MUL1)
                .stat(PartType.CORD, ItemStats.DURABILITY, 0.05f, StatInstance.Operation.MUL1)
                .stat(PartType.CORD, ItemStats.RANGED_DAMAGE, -0.1f, StatInstance.Operation.MUL1)
                .stat(PartType.CORD, ItemStats.RANGED_SPEED, 0.2f, StatInstance.Operation.MUL1)
                .stat(PartType.CORD, ItemStats.RARITY, 10, StatInstance.Operation.ADD)
                .trait(PartType.BINDING, Const.Traits.FLEXIBLE, 2)
                .trait(PartType.CORD, Const.Traits.SYNERGISTIC, 2)
                .trait(PartType.CORD, Const.Traits.FLEXIBLE, 3)
                .display(PartType.BINDING, PartTextureSet.LOW_CONTRAST, 0xB3804B)
                .displayBowstring(0x845E37);
        createBuilder(values, ECTags.Items.HARDENED_RODS, 2)
                .categories(MaterialCategories.ROCK, MaterialCategories.METAL)
                .stat(PartType.ROD, ItemStats.DURABILITY, 0.20f, StatInstance.Operation.MUL2)
                .stat(PartType.ROD, ItemStats.HARVEST_SPEED, 1, StatInstance.Operation.ADD)
                .stat(PartType.ROD, ItemStats.MELEE_DAMAGE, 0.5f, StatInstance.Operation.ADD)
                .stat(PartType.ROD, ItemStats.RARITY, 20)
                .trait(PartType.ROD, Const.Traits.HARD, 2)
                .trait(PartType.ROD, Const.Traits.BRITTLE, 2)
                .display(PartType.ROD, PartTextureSet.LOW_CONTRAST, 0xb1a69b);
        createBuilder(values, ECItems.SPRINGALINE_SHARD, 3)
                .categories(MaterialCategories.GEM)
                .mainStatsCommon(300, 14, 15, 41, 1.4f)
                .mainStatsHarvest(3, 8)
                .mainStatsMelee(2, 3, 0.1f)
                .mainStatsArmor(3, 6, 4, 3, 0, 10)
                .mainStatsRanged(1, 0.1f)
                .mainStatsProjectile(1, 1)
                .trait(PartType.MAIN, Const.Traits.CRUSHING, 3)
                .trait(PartType.MAIN, Const.Traits.JAGGED, 2)
                .trait(PartType.MAIN, Const.Traits.RENEW, 1, new MaterialRatioTraitCondition(0.7f))
                .stat(PartType.TIP, ItemStats.DURABILITY, -0.12f, StatInstance.Operation.MUL2)
                .trait(PartType.TIP, Const.Traits.SILKY, 1, new MaterialRatioTraitCondition(0.66f))
                .stat(PartType.TIP, ItemStats.DURABILITY, 64, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.ARMOR_DURABILITY, 64, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.HARVEST_LEVEL, 2, StatInstance.Operation.MAX)
                .stat(PartType.TIP, ItemStats.HARVEST_SPEED, 2, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.MELEE_DAMAGE, 4, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RANGED_DAMAGE, 1.5f, StatInstance.Operation.ADD)
                .stat(PartType.TIP, ItemStats.RARITY, 20, StatInstance.Operation.ADD)
                .trait(PartType.TIP, Const.Traits.CHIPPING, 1)
                .trait(PartType.TIP, Const.Traits.JAGGED, 3)
                .noStats(PartType.ADORNMENT)
                .trait(PartType.ADORNMENT, Const.Traits.CURSED, 4)
                .trait(PartType.ADORNMENT, Const.Traits.MIGHTY, 2, new MaterialRatioTraitCondition(0.5f))
                .displayAll(PartTextureSet.HIGH_CONTRAST_WITH_HIGHLIGHT, 0x9accfc);
        return values;
    }

    public static MaterialBuilder createBuilder(List<MaterialBuilder> list, RegistryObject<? extends ItemLike> provider, int tiers) {
        Item item = provider.get().asItem();
        MaterialBuilder builder = new MaterialBuilder(provider.getId(), tiers, item);

        list.add(builder);
        return builder;
    }

    public static MaterialBuilder createBuilder(List<MaterialBuilder> list, TagKey<Item> tag, int tiers) {
        MaterialBuilder builder = new MaterialBuilder(ElementalCraft.createRL(tag.location().getPath()), tiers, tag);

        list.add(builder);
        return builder;
    }

}
