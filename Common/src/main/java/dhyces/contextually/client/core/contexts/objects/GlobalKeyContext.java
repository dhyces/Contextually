package dhyces.contextually.client.core.contexts.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.icons.IIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class GlobalKeyContext extends AbstractKeyContext<Object, Object> {
    public static final Codec<GlobalKeyContext> CODEC = RecordCodecBuilder.create(instance ->
            fillBaseParts(instance).apply(instance, GlobalKeyContext::new)
    );

    public GlobalKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<IContextCondition> conditions, @Nullable Set<Object> ignored) {
        this(icons, conditions);
    }

    public GlobalKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<IContextCondition> conditions) {
        super(icons, conditions);
    }

    @Override
    public @NotNull IKeyContextType<?> getType() {
        return IKeyContextType.GLOBAL;
    }
}
