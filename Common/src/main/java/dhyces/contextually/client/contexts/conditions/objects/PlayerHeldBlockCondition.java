package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
public record PlayerHeldBlockCondition(@Nullable InteractionHand hand) implements INamedCondition {

    static final ResourceLocation ID = ContextuallyCommon.id("player_held_block");

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        if (pos instanceof BlockHitResult blockHitResult) {
            if (hand == null) {
                if (player.getMainHandItem().getItem() instanceof BlockItem blockItem) {
                    var state = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(player, InteractionHand.MAIN_HAND, player.getMainHandItem(), blockHitResult));
                    if (state != null) {
                        return state.canSurvive(level, blockHitResult.getBlockPos());
                    }
                    return false;
                }
                if (player.getOffhandItem().getItem() instanceof BlockItem blockItem) {
                    var state = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(player, InteractionHand.OFF_HAND, player.getOffhandItem(), blockHitResult));
                    if (state != null) {
                        return state.canSurvive(level, blockHitResult.getBlockPos());
                    }
                    return false;
                }
                return false;
            }
            if (player.getItemInHand(hand).getItem() instanceof BlockItem blockItem) {
                var state = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(player, hand, player.getItemInHand(hand), blockHitResult));
                if (state != null) {
                    return state.canSurvive(level, blockHitResult.getBlockPos());
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Serializer implements IConditionSerializer<PlayerHeldBlockCondition> {

        @Override
        public PlayerHeldBlockCondition deserialize(JsonObject json) {
            var handJson = json.get("hand");
            return new PlayerHeldBlockCondition(getHand(handJson));
        }

        @Override
        public JsonObject serialize(PlayerHeldBlockCondition context) {
            var base = createBaseConditionJson();
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
