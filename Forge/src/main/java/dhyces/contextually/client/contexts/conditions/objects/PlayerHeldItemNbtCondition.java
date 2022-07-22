package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.AbstractConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public record PlayerHeldItemNbtCondition(CompoundTag tag, @Nullable InteractionHand hand) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        if (hand == null) {
            return player.getMainHandItem().getTag().equals(tag) || player.getOffhandItem().getTag().equals(tag);
        }
        return player.getItemInHand(hand).getTag().equals(tag);
    }

    public static class Serializer extends AbstractConditionSerializer<PlayerHeldItemNbtCondition> {

        public Serializer() {
            super("player_held_nbt");
        }

        @Override
        public PlayerHeldItemNbtCondition deserialize(JsonObject json) {
            var nbt = CraftingHelper.getNBT(json.get("nbt"));
            var hand = getHand(json.get("hand"));
            return new PlayerHeldItemNbtCondition(nbt, hand);
        }

        @Override
        public JsonObject serialize(PlayerHeldItemNbtCondition context) {
            // TODO
            return null;
        }
    }
}
