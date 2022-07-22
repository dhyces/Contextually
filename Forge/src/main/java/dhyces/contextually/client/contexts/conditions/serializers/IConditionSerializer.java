package dhyces.contextually.client.contexts.conditions.serializers;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.Condition;
import net.minecraft.resources.ResourceLocation;

public interface IConditionSerializer {

    Condition deserialize(JsonObject json);

    JsonObject serialize(Condition context);

    ResourceLocation getId();
}
