package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import dhyces.contextually.util.JsonHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public record TargetHeldItemCondition(Item item, CompoundTag tag, @Nullable InteractionHand hand) implements INamedCondition {

    static final ResourceLocation ID = Contextually.id("target_held_item");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof LivingEntity livingEntity) {
            if (InteractionHand.MAIN_HAND.equals(hand) || hand == null) {
                var handItem = livingEntity.getMainHandItem();
                if (item != null && !handItem.is(item)) {
                    return false;
                }
                if (tag != null && !handItem.getTag().equals(tag)) {
                    return false;
                }
            }
            if (InteractionHand.OFF_HAND.equals(hand) || hand == null) {
                var handItem = livingEntity.getOffhandItem();
                if (item != null && !handItem.is(item)) {
                    return false;
                }
                if (tag != null && !handItem.getTag().equals(tag)) {
                    return false;
                }
            }
            return true;
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
            var item = json.get("item") == null ? null : GsonHelper.convertToItem(json.get("item"), "item");
            var nbt = json.get("nbt") == null ? null : JsonHelper.readNbt(json.get("nbt"));
            var hand = getHand(json.get("hand"));
            if (item == null && nbt == null) {
                throw new JsonSyntaxException("Keys \"item\" and \"nbt\" cannot both be null.");
            }
            return new TargetHeldItemCondition(item, nbt, hand);
        }

        @Override
        public JsonObject serialize(TargetHeldItemCondition context) {
            var base = createBaseConditionJson();
            var item = context.item != null ? new JsonPrimitive(BuiltInRegistries.ITEM.getKey(context.item).toString()) : null;
            base.add("item", item);
            var hand = context.hand != null ? new JsonPrimitive(context.hand.toString()) : null;
            base.add("hand", hand);
            var nbt = context.tag != null ? new JsonPrimitive(context.tag.toString()) : null;
            base.add("nbt", nbt);
            return base;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
