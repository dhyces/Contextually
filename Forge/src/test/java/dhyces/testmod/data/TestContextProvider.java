package dhyces.testmod.data;

import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.conditions.predicates.ItemContextPredicate;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.core.contexts.objects.EntityKeyContext;
import dhyces.contextually.client.core.contexts.objects.GlobalKeyContext;
import dhyces.contextually.client.core.contexts.objects.ItemKeyContext;
import dhyces.contextually.common.data.ContextProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class TestContextProvider extends ContextProvider {

    public TestContextProvider(@NotNull PackOutput output, @NotNull ExistingFileHelper fileHelper, @NotNull String modid) {
        super(output, fileHelper, modid);
    }

    AtomicInteger integer = new AtomicInteger();

    @Override
    protected void addContexts(BiConsumer<String, IKeyContext<?, ?>> exporter) {
//        ContextBuilder.create(ItemKeyContext::new)
//                .addIcon(IconFactories.itemIcon(Items.GRANITE))
//                .export(intName(), exporter);
//
//        ContextBuilder.create(ItemKeyContext::new)
//                .addIcon(IconFactories.itemIcon(Items.ACACIA_PRESSURE_PLATE))
//                .export(intName(), exporter);
//
//        ContextBuilder.<Object, Object, IKeyContext<Object, Object>>create(GlobalKeyContext::new)
//                .addIcon(IconFactories.keyIcon(34))
//                .export(intName(), exporter);
//
//        ContextBuilder.create(BlockKeyContext::new)
//                .addIcon(IconFactories.keyIcon("key.crouch"))
//                .addTarget(Blocks.TARGET.defaultBlockState())
//                .addTarget(Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, Direction.NORTH))
//                .export(intName(), exporter);
//
//        ContextBuilder.create(EntityKeyContext::new)
//                .addIcon(IconFactories.animatedIconBuilder()
//                        .itemIconForTime(Items.BUCKET, 2)
//                        .itemIconForTime(Items.LAVA_BUCKET, 8)
//                        .build())
//                .addCondition(ConditionFactories.heldItem(ItemContextPredicate.Builder.create().betweenCount(3, 5).build()))
//                .export(intName(), exporter);
    }

    private String intName() {
        return String.valueOf(integer.getAndIncrement());
    }
}
