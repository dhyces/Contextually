package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
public record PlayerHeldBlockCondition(@Nullable InteractionHand hand) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        if (hand == null) {
            return player.getMainHandItem().getItem() instanceof BlockItem || player.getOffhandItem().getItem() instanceof BlockItem;
        }
        return player.getItemInHand(hand).getItem() instanceof BlockItem;
    }

    public static class Serializer implements IConditionSerializer<PlayerHeldBlockCondition> {

        final ResourceLocation id = ContextuallyCommon.modloc("player_held_block");

        @Override
        public PlayerHeldBlockCondition deserialize(JsonObject json) {
            var handJson = json.get("hand");
            return new PlayerHeldBlockCondition(getHand(handJson));
        }

        @Override
        public JsonObject serialize(PlayerHeldBlockCondition context) {
            // TODO
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
