package dhyces.contextually.client.contexts.conditions.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public interface IConditionSerializer<T extends INamedCondition> {

    T deserialize(JsonObject json);

    JsonObject serialize(T context);

    ResourceLocation getId();

    default JsonObject createBaseConditionJson() {
        JsonObject json = new JsonObject();
        json.add("condition", new JsonPrimitive(getId().toString()));
        return json;
    }

    default InteractionHand getHand(JsonElement handJson) {
        if (handJson == null) return null;
        return switch (handJson.getAsString()) {
            case "mainhand" -> InteractionHand.MAIN_HAND;
            case "offhand" -> InteractionHand.OFF_HAND;
            default -> throw new IllegalArgumentException("Hand specified is invalid.");
        };
    }
}
