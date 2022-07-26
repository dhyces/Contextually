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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public record TargetHeldItemCondition(Item item, @Nullable InteractionHand hand) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.modloc("target_held_item");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof LivingEntity livingEntity) {
            if (hand == null) {
                return livingEntity.getMainHandItem().is(item) || livingEntity.getOffhandItem().is(item);
            }
            return livingEntity.getItemInHand(hand).is(item);
        }
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<TargetHeldItemCondition> {

        @Override
        public TargetHeldItemCondition deserialize(JsonObject json) {
            var item = GsonHelper.convertToItem(json.get("item"), "item");
            var hand = getHand(json.get("hand"));
            return new TargetHeldItemCondition(item, hand);
        }

        @Override
        public JsonObject serialize(TargetHeldItemCondition context) {
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
