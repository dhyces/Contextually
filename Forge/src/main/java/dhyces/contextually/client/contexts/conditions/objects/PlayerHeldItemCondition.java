package dhyces.contextually.client.contexts.conditions.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import static dhyces.contextually.ContextuallyCommon.modloc;

public record PlayerHeldItemCondition(Item item, @Nullable InteractionHand hand) implements IConditionPredicate {
    @Override
    public boolean test(Object target, ClientLevel level, AbstractClientPlayer player) {
        if (hand == null) {
            return player.getMainHandItem().is(item) || player.getOffhandItem().is(item);
        }
        return player.getItemInHand(hand).is(item);
    }

    public static class Serializer implements IConditionSerializer<PlayerHeldItemCondition> {

        final ResourceLocation id = ContextuallyCommon.modloc("player_held_item");

        @Override
        public PlayerHeldItemCondition deserialize(JsonObject json) {
            var location = ResourceLocation.of(json.get("item").getAsString(), ':');
            var item = ForgeRegistries.ITEMS.getValue(location);
            var handJson = json.get("hand");
            return new PlayerHeldItemCondition(item, getHand(handJson));
        }

        @Override
        public JsonObject serialize(PlayerHeldItemCondition context) {
            // TODO
            return null;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}