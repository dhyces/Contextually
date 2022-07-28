package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.KeyContextLoader;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.HitResult;

public record NotCondition(IConditionPredicate condition) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.id("not");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        return !condition.test(target, pos, level, player);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<NotCondition> {

        @Override
        public NotCondition deserialize(JsonObject json) {
            var wrappedCondition = json.getAsJsonObject("not");
            return new NotCondition(KeyContextLoader.deserializeCondition(wrappedCondition));
        }

        @Override
        public JsonObject serialize(NotCondition context) {
            var base = createBaseConditionJson();
            try {
                base.add("not", KeyContextLoader.serializeCondition((INamedCondition) context.condition));
            } catch (ClassCastException e) {
                throw new IllegalStateException("Given condition: " + context.condition + " cannot be serialized.");
            }
            return base;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
