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

import java.util.List;
import java.util.Set;

public class ItemKeyContext extends AbstractKeyContext<Item, ItemStack> {
    public static final Codec<ItemKeyContext> CODEC = RecordCodecBuilder.create(instance ->
            fillBaseParts(instance).and(
                    BuiltInRegistries.ITEM.byNameCodec().listOf().xmap(Set::copyOf, List::copyOf).fieldOf("targets").forGetter(ItemKeyContext::getTargets)
            ).apply(instance, ItemKeyContext::new)
    );


    public ItemKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<IContextCondition> conditions, @NotNull Set<Item> targetedItems) {
        super(icons, conditions);
    }

    @Override
    public @NotNull IKeyContextType<?> getType() {
        return IKeyContextType.ITEM;
    }
}
