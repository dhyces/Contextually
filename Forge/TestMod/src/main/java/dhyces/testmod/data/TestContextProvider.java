package dhyces.testmod.data;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.client.core.conditions.predicates.ItemContextPredicate;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.contexts.PartialBlockState;
import dhyces.contextually.client.core.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.core.contexts.objects.EntityKeyContext;
import dhyces.contextually.client.core.contexts.objects.GlobalKeyContext;
import dhyces.contextually.client.core.contexts.objects.ItemKeyContext;
import dhyces.contextually.common.data.ContextProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestContextProvider extends ContextProvider {

    public TestContextProvider(@Nonnull PackOutput output, @Nonnull String modid) {
        super(output, modid);
    }

    AtomicInteger integer = new AtomicInteger();

    @Override
    protected void addContexts(ContextExporter exporter) {
        // 0
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.GRANITE))
                .export(intName(), exporter);

        // 1
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.heldItem(
                        ItemContextPredicate.Builder.create(Items.SUGAR).build()
                ))
                .export(intName(), exporter);

        // 2
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.heldItem(
                        ItemContextPredicate.Builder.create(Items.SUGAR).minCount(4).build()
                ))
                .export(intName(), exporter);

        // 3
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.heldItem(
                        ItemContextPredicate.Builder.create(Items.SUGAR).enchantment(Enchantments.AQUA_AFFINITY).build()
                ))
                .export(intName(), exporter);

        // 4
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.heldItem(
                        ItemContextPredicate.Builder.create(Items.DRAGON_HEAD).build(),
                        InteractionHand.OFF_HAND
                ))
                .export(intName(), exporter);

        // 5
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.targetHeldItem(
                        ItemContextPredicate.Builder.create(Items.SUGAR).exactCount(7).build()
                ))
                .export(intName(), exporter);

        // 6
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.heldItem(
                        ItemContextPredicate.Builder.create().nbt(compoundTag -> compoundTag.putString("Psyche", "lol")).build()
                ))
                .addTarget(Items.SADDLE)
                .export(intName(), exporter);

        // 7
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.heldItem(
                        ItemContextPredicate.Builder.create(Items.SUGAR).potion(Potions.LONG_NIGHT_VISION).build()
                ))
                .export(intName(), exporter);

        // 8
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.heldItem(
                        ItemContextPredicate.Builder.create(Items.SUGAR).minDurability(3).build(),
                        InteractionHand.MAIN_HAND
                ))
                .export(intName(), exporter);

        // 9
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.villagerProfession(VillagerProfession.BUTCHER))
                .export(intName(), exporter);

        // 10
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.not(ConditionFactories.heldKey("key.use")))
                .export(intName(), exporter);

        // 11
        ContextBuilder.create(ItemKeyContext::new)
                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
                .addCondition(ConditionFactories.and(
                        ConditionFactories.heldKey("key.attack"),
                        ConditionFactories.heldKey(InputConstants.KEY_8),
                        ConditionFactories.or(
                                ConditionFactories.heldItem(ItemContextPredicate.Builder.create(Items.BAMBOO_PLANKS).build()),
                                ConditionFactories.heldItem(ItemContextPredicate.Builder.create(Items.ORANGE_BED).build())
                        )
                ))
                .export(intName(), exporter);

        // 12
        ContextBuilder.<Object, Object, IKeyContext<Object, Object>>create(GlobalKeyContext::new)
                .addIcon(IconFactories.keyIcon(34))
                .export(intName(), exporter);

        // 13
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.crouch"))
                .addBlock(Blocks.TARGET)
                .addPartialStates(
                        List.of(
                                PartialBlockState.builder(Blocks.LECTERN).with(LecternBlock.FACING, Direction.NORTH).build(),
                                PartialBlockState.builder(Blocks.LECTERN).with(LecternBlock.FACING, Direction.SOUTH).with(LecternBlock.HAS_BOOK, true).build(),
                                PartialBlockState.builder(Blocks.LECTERN).with(LecternBlock.FACING, Direction.EAST).with(LecternBlock.HAS_BOOK, false).build()
                        )
                )
                .export(intName(), exporter);

        // 14
        ContextBuilder.create(EntityKeyContext::new)
                .addIcon(IconFactories.animatedIconBuilder()
                        .itemIconForTime(Items.BUCKET, 2)
                        .itemIconForTime(Items.LAVA_BUCKET, 8)
                        .build())
                .addCondition(ConditionFactories.heldItem(ItemContextPredicate.Builder.create().betweenCount(3, 5).build()))
                .export(intName(), exporter);

        // 15
        ContextBuilder.create(EntityKeyContext::new)
                .addIcon(IconFactories.animatedIconBuilder()
                        .itemIconForTime(Items.BUCKET, 2)
                        .build())
                .addTarget(EntityType.AXOLOTL)
                .addTarget(EntityType.CAT)
                .export(intName(), exporter);

        // 16
        BlockContextBuilder.create()
                .addIcon(IconFactories.animatedIconBuilder()
                        .iconForTime(
                                IconFactories.textureAtlasIcon(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("item/stone_pickaxe"), 0x0000FF), 1
                        ).iconForTime(
                                IconFactories.textureAtlasIcon(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("item/stone_pickaxe"), 0x0055AA), 1
                        ).iconForTime(
                                IconFactories.textureAtlasIcon(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("item/stone_pickaxe"), 0x00AA55), 1
                        ).iconForTime(
                                IconFactories.textureAtlasIcon(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("item/stone_pickaxe"), 0x00FF00), 1
                        ).iconForTime(
                                IconFactories.textureAtlasIcon(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("item/stone_pickaxe"), 0x55AA00), 1
                        ).iconForTime(
                                IconFactories.textureAtlasIcon(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("item/stone_pickaxe"), 0xAA5500), 1
                        ).iconForTime(
                                IconFactories.textureAtlasIcon(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("item/stone_pickaxe"), 0xFF0000), 1
                        ).build()
                )
                .addBlock(Blocks.GRAY_GLAZED_TERRACOTTA)
                .export(intName(), exporter);

        // 17
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.crouch"))
                .addPartialStates(
                        List.of(
                                PartialBlockState.builder(Blocks.REDSTONE_WIRE).with(RedStoneWireBlock.EAST, RedstoneSide.UP).build(),
                                PartialBlockState.builder(Blocks.REDSTONE_WIRE).with(RedStoneWireBlock.EAST, RedstoneSide.SIDE).build(),
                                PartialBlockState.builder(Blocks.REDSTONE_WIRE).with(RedStoneWireBlock.WEST, RedstoneSide.UP).build()
                        )
                )
                .export(intName(), exporter);

        // 18
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.crouch"))
                .addPartialStates(
                        List.of(
                                PartialBlockState.builder(Blocks.LECTERN).with(LecternBlock.FACING, Direction.EAST).with(LecternBlock.HAS_BOOK, true).with(LecternBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.LECTERN).with(LecternBlock.FACING, Direction.EAST).with(LecternBlock.HAS_BOOK, false).build()
                        )
                )
                .export(intName(), exporter);
    }

    private String intName() {
        return String.valueOf(integer.getAndIncrement());
    }
}
