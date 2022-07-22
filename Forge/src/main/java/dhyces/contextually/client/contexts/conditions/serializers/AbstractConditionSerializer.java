package dhyces.contextually.client.contexts.conditions.serializers;

import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractConditionSerializer<T extends IConditionPredicate> implements IConditionSerializer<T> {

    final ResourceLocation id;

    public AbstractConditionSerializer(String contextuallyId) {
        this(ContextuallyCommon.modloc(contextuallyId));
    }

    public AbstractConditionSerializer(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
