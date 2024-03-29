package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.HitResult;

public record OrCondition(IConditionPredicate condition1, IConditionPredicate condition2) implements INamedCondition {

    static final ResourceLocation ID = Contextually.id("or");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        return condition1.test(target, pos, level, player) || condition2.test(target, pos, level, player);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<OrCondition> {

        @Override
        public OrCondition deserialize(JsonObject json) {
            var condition1 = KeyContextLoader.deserializeCondition(json.getAsJsonObject("argument1"));
            var condition2 = KeyContextLoader.deserializeCondition(json.getAsJsonObject("argument2"));
            return new OrCondition(condition1, condition2);
        }

        @Override
        public JsonObject serialize(OrCondition context) {
            var json = createBaseConditionJson();
            try {
                json.add("argument1", KeyContextLoader.serializeCondition((INamedCondition)context.condition1));
            } catch (ClassCastException e) {
                throw new IllegalStateException("Given condition: " + context.condition1 + " cannot be serialized.");
            }
            try {
                json.add("argument2", KeyContextLoader.serializeCondition((INamedCondition)context.condition2));
            } catch (ClassCastException e) {
                throw new IllegalStateException("Given condition: " + context.condition2 + " cannot be serialized.");
            }
            return json;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
