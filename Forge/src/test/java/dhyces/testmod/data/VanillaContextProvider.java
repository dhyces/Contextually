package dhyces.testmod.data;

import dhyces.contextually.client.core.conditions.predicates.ItemContextPredicate;
import dhyces.contextually.client.core.contexts.PartialBlockState;
import dhyces.contextually.client.core.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.core.contexts.objects.FluidKeyContext;
import dhyces.contextually.common.data.ContextProvider;
import dhyces.contextually.util.VanillaKeyMappingNames;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class VanillaContextProvider extends ContextProvider {
    public VanillaContextProvider(@Nonnull PackOutput output, @Nonnull ExistingFileHelper fileHelper) {
        super(output, fileHelper, "vanilla");
    }

    @Override
    protected void addContexts(ContextExporter exporter) {
        // Default block attack
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.attack"))
                .export("block/default_break_block", exporter);
        // Bucket fluid
        ContextBuilder.create(FluidKeyContext::new)
                .addIcon(IconFactories.keyIcon("key.use"))
                .addCondition(ConditionFactories.heldItem(ItemContextPredicate.Builder.create(Items.BUCKET).build()))
                .addTarget(Fluids.WATER.getSource(false))
                .addTarget(Fluids.LAVA.getSource(false))
                .export("fluid/bucket_fluid", exporter);
        // Play noteblock
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.attack"))
                .addBlock(Blocks.NOTE_BLOCK)
                .export("block/play_noteblock", exporter);
        // Change note
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addBlock(Blocks.NOTE_BLOCK)
                .export("block/change_note", exporter);
        // Sleep in bed
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addBlocks(
                        List.of(
                                Blocks.WHITE_BED,
                                Blocks.ORANGE_BED,
                                Blocks.MAGENTA_BED,
                                Blocks.LIGHT_BLUE_BED,
                                Blocks.YELLOW_BED,
                                Blocks.LIME_BED,
                                Blocks.PINK_BED,
                                Blocks.GRAY_BED,
                                Blocks.LIGHT_GRAY_BED,
                                Blocks.CYAN_BED,
                                Blocks.PURPLE_BED,
                                Blocks.BLUE_BED,
                                Blocks.BROWN_BED,
                                Blocks.GREEN_BED,
                                Blocks.RED_BED,
                                Blocks.BLACK_BED
                        )
                )
                .export("block/sleep_in_bed", exporter);
        // Place cart on rails
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addIcon(IconFactories.itemIcon(Items.MINECART))
                .addBlocks(
                        List.of(
                                Blocks.RAIL,
                                Blocks.POWERED_RAIL,
                                Blocks.ACTIVATOR_RAIL,
                                Blocks.DETECTOR_RAIL
                        )
                ).export("block/place_minecart", exporter);
        // Ignite TNT
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addIcon(IconFactories.itemIcon(Items.FLINT_AND_STEEL))
                .addBlock(Blocks.TNT)
                .export("block/ignite_tnt", exporter);

        // Place book in chiseled bookshelf

        // Open GUI
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addBlocks(
                        List.of(
                                Blocks.CHEST,
                                Blocks.FURNACE,
                                Blocks.ENCHANTING_TABLE,
                                Blocks.BREWING_STAND,
                                Blocks.ENDER_CHEST,
                                Blocks.BEACON,
                                // TODO: Command blocks, but they require perms
                                Blocks.ANVIL,
                                Blocks.TRAPPED_CHEST,
                                Blocks.DROPPER,
                                Blocks.HOPPER,
                                Blocks.SHULKER_BOX,
                                Blocks.WHITE_SHULKER_BOX,
                                Blocks.ORANGE_SHULKER_BOX,
                                Blocks.MAGENTA_SHULKER_BOX,
                                Blocks.LIGHT_BLUE_SHULKER_BOX,
                                Blocks.YELLOW_SHULKER_BOX,
                                Blocks.LIME_SHULKER_BOX,
                                Blocks.PINK_SHULKER_BOX,
                                Blocks.GRAY_SHULKER_BOX,
                                Blocks.LIGHT_GRAY_SHULKER_BOX,
                                Blocks.CYAN_SHULKER_BOX,
                                Blocks.PURPLE_SHULKER_BOX,
                                Blocks.BLUE_SHULKER_BOX,
                                Blocks.BROWN_SHULKER_BOX,
                                Blocks.GREEN_SHULKER_BOX,
                                Blocks.RED_SHULKER_BOX,
                                Blocks.BLACK_SHULKER_BOX,
                                Blocks.LOOM,
                                Blocks.BARREL,
                                Blocks.SMOKER,
                                Blocks.BLAST_FURNACE,
                                Blocks.CARTOGRAPHY_TABLE,
                                Blocks.GRINDSTONE,
                                Blocks.SMITHING_TABLE,
                                Blocks.STONECUTTER
                                // TODO: Structure blocks
                        )
                ).addPartialState(PartialBlockState.builder(Blocks.LECTERN).with(LecternBlock.HAS_BOOK, true).build())
                .export("block/open", exporter);
        // Hoe farmland
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addCondition(ConditionFactories.heldItem(ItemContextPredicate.Builder.create(Items.WOODEN_HOE, Items.STONE_HOE, Items.GOLDEN_HOE, Items.IRON_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE).build()))
                .addBlocks(
                        List.of(
                                Blocks.DIRT,
                                Blocks.GRASS_BLOCK,
                                Blocks.PODZOL
                        )
                ).export("block/hoe_farmland", exporter);
        // Press button
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addPartialStates(
                        List.of(
                                PartialBlockState.builder(Blocks.STONE_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.POLISHED_BLACKSTONE_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.OAK_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.BIRCH_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.SPRUCE_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.JUNGLE_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.ACACIA_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.DARK_OAK_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.MANGROVE_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.WARPED_BUTTON).with(ButtonBlock.POWERED, false).build(),
                                PartialBlockState.builder(Blocks.CRIMSON_BUTTON).with(ButtonBlock.POWERED, false).build()
                        )
                ).export("block/press_button", exporter);
        // Flick lever
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon("key.use"))
                .addBlock(Blocks.LEVER)
                .export("block/flick_lever", exporter);
        // Carve pumpkin
        // Eat cake + candle cakes
        // Tune repeater
        // Cauldrons (water, lava, powdered snow)
        // Switch comparator
        // Switch daylight sensor mode
        // Light campfires
        // Light candles
        // Place book in lectern
        // Wax/de-wax copper
        // Pick sweetberries/glowberries
        BlockContextBuilder.create()
                .addIcon(IconFactories.keyIcon(VanillaKeyMappingNames.KEY_USE))
                .addPartialState(PartialBlockState.builder(Blocks.SWEET_BERRY_BUSH).with(SweetBerryBushBlock.AGE, 3).build())
                .addPartialState(PartialBlockState.builder(Blocks.CAVE_VINES).with(CaveVines.BERRIES, true).build())
                .export("block/pick_berries", exporter);
    }

    public static class VanillaContextEN_USLangProvider extends LanguageProvider {

        public VanillaContextEN_USLangProvider(PackOutput output) {
            super(output, "vanilla", "en_us");
        }

        @Override
        protected void addTranslations() {
            add("vanilla.contexts.block.default_break_block", "Break block");
            add("vanilla.contexts.fluid.bucket_fluid", "Pickup fluid");
            add("vanilla.contexts.block.play_noteblock", "Play noteblock");
            add("vanilla.contexts.block.change_note", "Change note");
            add("vanilla.contexts.block.sleep_in_bed", "Sleep");
            add("vanilla.contexts.block.place_minecart", "Place minecart");
            add("vanilla.contexts.block.ignite_tnt", "Ignite");
            add("vanilla.contexts.block.open", "Open");
            add("vanilla.contexts.block.hoe_farmland", "Till farmland");
            add("vanilla.contexts.block.press_button", "Press");
            add("vanilla.contexts.block.flick_lever", "Flick");
            add("vanilla.contexts.block.pick_berries", "Pick");
        }
    }
}
