package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.AbstractConditionSerializer;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public record OrCondition(IConditionPredicate condition1, IConditionPredicate condition2) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        return condition1.test(target, level, player) || condition2.test(target, level, player);
    }

    public static class Serializer extends AbstractConditionSerializer<OrCondition> {

        public Serializer() {
            super("or");
        }

        @Override
        public OrCondition deserialize(JsonObject json) {
            var condition1 = KeyContextLoader.deserializeCondition(json.getAsJsonObject("argument1"));
            var condition2 = KeyContextLoader.deserializeCondition(json.getAsJsonObject("argument2"));
            return new OrCondition(condition1, condition2);
        }

        @Override
        public JsonObject serialize(OrCondition context) {
            // TODO
            return null;
        }
    }
}
