package dhyces.contextually.client.contexts.conditions.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import static dhyces.contextually.ContextuallyCommon.modloc;

import dhyces.contextually.client.contexts.conditions.Condition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.registries.ForgeRegistries;

public class ConditionSerializers {

    private static InteractionHand getHand(JsonElement handJson) {
        if (handJson == null) return null;
        return switch (handJson.getAsString()) {
            case "main" -> InteractionHand.MAIN_HAND;
            case "offhand" -> InteractionHand.OFF_HAND;
            default -> throw new IllegalArgumentException("Hand specified is invalid.");
        };
    }

    public static final IConditionSerializer PLAYER_HELD_ITEM_SERIALIZER = new IConditionSerializer() {
        @Override
        public Condition deserialize(JsonObject json) {
            var location = ResourceLocation.of(json.get("item").getAsString(), ':');
            var item = ForgeRegistries.ITEMS.getValue(location);
            var handJson = json.get("hand");
            return new Condition.PlayerHeldItemCondition(item, getHand(handJson));
        }

        @Override
        public JsonObject serialize(Condition context) {
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return modloc("player_held_item");
        }
    };

    public static final IConditionSerializer PLAYER_HELD_BLOCK_SERIALIZER = new IConditionSerializer() {
        @Override
        public Condition deserialize(JsonObject json) {
            var handJson = json.get("hand");
            return new Condition.PlayerHeldBlockCondition(getHand(handJson));
        }

        @Override
        public JsonObject serialize(Condition context) {
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return modloc("player_held_block");
        }
    };
}
