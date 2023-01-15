package dhyces.contextually.client.core.contexts.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.icons.IIcon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class EntityKeyContext extends AbstractKeyContext<EntityType<?>, Entity> {
    public static final Codec<EntityKeyContext> CODEC = RecordCodecBuilder.create(instance ->
            fillBaseParts(instance).and(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().fieldOf("targets").xmap(Set::copyOf, List::copyOf).forGetter(EntityKeyContext::getTargets)
            ).apply(instance, EntityKeyContext::new)
    );

    public EntityKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<IContextCondition> conditions, @NotNull Set<EntityType<?>> targetedTypes) {
        super(icons, targetedTypes, conditions);
    }

    @Override
    @NotNull
    public IKeyContextType<?> getType() {
        return IKeyContextType.ENTITY;
    }
}
