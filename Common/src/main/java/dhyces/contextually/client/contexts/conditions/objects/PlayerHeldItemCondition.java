package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public record PlayerHeldItemCondition(Item item, @Nullable InteractionHand hand) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.id("player_held_item");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (hand == null) {
            return player.getMainHandItem().is(item) || player.getOffhandItem().is(item);
        }
        return player.getItemInHand(hand).is(item);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<PlayerHeldItemCondition> {

        @Override
        public PlayerHeldItemCondition deserialize(JsonObject json) {
            var item = GsonHelper.convertToItem(json.get("item"), "item");
            var handJson = json.get("hand");
            return new PlayerHeldItemCondition(item, getHand(handJson));
        }

        @Override
        public JsonObject serialize(PlayerHeldItemCondition context) {
            var base = createBaseConditionJson();
            base.add("item", new JsonPrimitive(Registry.ITEM.getKey(context.item).toString()));
            var hand = context.hand == null ? null : new JsonPrimitive(context.hand.toString());
            base.add("hand", hand);
            return base;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}