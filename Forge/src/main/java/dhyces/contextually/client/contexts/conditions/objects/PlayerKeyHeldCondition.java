package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import dhyces.contextually.client.contexts.keys.Key;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public record PlayerKeyHeldCondition(Key key) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        return key.getMapping().get().isDown();
    }

    public static class Serializer implements IConditionSerializer<PlayerKeyHeldCondition> {

        final ResourceLocation id = ContextuallyCommon.modloc("player_key_held");

        @Override
        public PlayerKeyHeldCondition deserialize(JsonObject json) {
            var key = KeyContextLoader.deserializeKey(json.getAsJsonObject("key"));
            return new PlayerKeyHeldCondition(key);
        }

        @Override
        public JsonObject serialize(PlayerKeyHeldCondition context) {
            // TODO
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
