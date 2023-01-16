package dhyces.contextually.client.core.contexts.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.icons.IIcon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class ItemKeyContext extends AbstractKeyContext<Item, ItemStack> {
    public static final Codec<ItemKeyContext> CODEC = RecordCodecBuilder.create(instance ->
            fillBaseParts(instance).and(
                    BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("targets").xmap(Set::copyOf, List::copyOf).forGetter(ItemKeyContext::getTargets)
            ).apply(instance, ItemKeyContext::new)
    );


    public ItemKeyContext(@Nonnull Set<IIcon> icons, @Nonnull Set<IContextCondition> conditions, @Nonnull Set<Item> targetedItems) {
        super(icons, targetedItems, conditions);
    }

    @Override
    public @Nonnull IKeyContextType<?> getType() {
        return IKeyContextType.ITEM;
    }
}
