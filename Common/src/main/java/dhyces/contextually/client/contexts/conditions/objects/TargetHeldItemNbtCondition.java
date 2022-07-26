package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import dhyces.contextually.util.JsonHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public record TargetHeldItemNbtCondition(CompoundTag tag, @Nullable InteractionHand hand) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.modloc("target_held_nbt");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (target instanceof LivingEntity livingEntity) {
            if (hand == null) {
                return livingEntity.getMainHandItem().getTag().equals(tag) || livingEntity.getOffhandItem().getTag().equals(tag);
            }
            return livingEntity.getItemInHand(hand).getTag().equals(tag);
        }
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<TargetHeldItemNbtCondition> {

        @Override
        public TargetHeldItemNbtCondition deserialize(JsonObject json) {
            var nbt = JsonHelper.readNbt(json.get("nbt"));
            var hand = getHand(json.get("hand"));
            return new TargetHeldItemNbtCondition(nbt, hand);
        }

        @Override
        public JsonObject serialize(TargetHeldItemNbtCondition context) {
            var base = createBaseConditionJson();
            base.add("nbt", new JsonPrimitive(context.tag.toString()));
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
