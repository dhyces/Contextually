package dhyces.contextually.client.contexts.conditions;

import net.minecraft.resources.ResourceLocation;

public interface INamedCondition extends IConditionPredicate {
    ResourceLocation getId();
}
