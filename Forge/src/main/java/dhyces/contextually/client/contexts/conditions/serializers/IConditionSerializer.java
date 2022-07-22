package dhyces.contextually.client.contexts.conditions.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public interface IConditionSerializer<T extends IConditionPredicate> {

    T deserialize(JsonObject json);

    JsonObject serialize(T context);

    ResourceLocation getId();

    default InteractionHand getHand(JsonElement handJson) {
        if (handJson == null) return null;
        return switch (handJson.getAsString()) {
            case "main" -> InteractionHand.MAIN_HAND;
            case "offhand" -> InteractionHand.OFF_HAND;
            default -> throw new IllegalArgumentException("Hand specified is invalid.");
        };
    }
}
