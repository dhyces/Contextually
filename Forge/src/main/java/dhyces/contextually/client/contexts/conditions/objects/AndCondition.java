package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.AbstractConditionSerializer;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public record AndCondition(IConditionPredicate condition1, IConditionPredicate condition2) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        return condition1.test(target, level, player) && condition2.test(target, level, player);
    }

    public static class Serializer extends AbstractConditionSerializer<AndCondition> {

        public Serializer() {
            super("and");
        }

        @Override
        public AndCondition deserialize(JsonObject json) {
            var condition1 = KeyContextLoader.deserializeCondition(json.getAsJsonObject("argument1"));
            var condition2 = KeyContextLoader.deserializeCondition(json.getAsJsonObject("argument2"));
            return new AndCondition(condition1, condition2);
        }

        @Override
        public JsonObject serialize(AndCondition context) {
            // TODO
            return null;
        }
    }
}
