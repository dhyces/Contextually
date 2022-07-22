package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public record NotCondition(IConditionPredicate condition) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        return !condition.test(target, level, player);
    }

    public static class Serializer implements IConditionSerializer<NotCondition> {

        final ResourceLocation id = ContextuallyCommon.modloc("not");

        @Override
        public NotCondition deserialize(JsonObject json) {
            var wrappedCondition = json.getAsJsonObject("not");
            return new NotCondition(KeyContextLoader.deserializeCondition(wrappedCondition));
        }

        @Override
        public JsonObject serialize(NotCondition context) {
            // TODO
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
