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
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public record PlayerHeldItemNbtCondition(CompoundTag tag, @Nullable InteractionHand hand) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.modloc("player_held_nbt");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (hand == null) {
            return player.getMainHandItem().getTag().equals(tag) || player.getOffhandItem().getTag().equals(tag);
        }
        return player.getItemInHand(hand).getTag().equals(tag);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<PlayerHeldItemNbtCondition> {

        @Override
        public PlayerHeldItemNbtCondition deserialize(JsonObject json) {
            var nbt = JsonHelper.readNbt(json.get("nbt"));
            var hand = getHand(json.get("hand"));
            return new PlayerHeldItemNbtCondition(nbt, hand);
        }

        @Override
        public JsonObject serialize(PlayerHeldItemNbtCondition context) {
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
